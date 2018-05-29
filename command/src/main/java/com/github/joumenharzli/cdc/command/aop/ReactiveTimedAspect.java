/*
 * Copyright (C) 2018 Joumen Harzli
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.github.joumenharzli.cdc.command.aop;

import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Aspect for intercepting methods annotated with {@link Timed}
 * <p>
 * It also provide supports for the types {@link Mono} and {@link Flux}
 *
 * @author Joumen Harzli
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ReactiveTimedAspect {

  private final MeterRegistry registry;

  @Around("execution (@io.micrometer.core.annotation.Timed * *.*(..))")
  public Object timedMethod(ProceedingJoinPoint joinPoint) throws Throwable {

    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

    String className = methodSignature.getDeclaringType().getSimpleName();
    String methodName = methodSignature.getName();
    String metricName = String.format("%s.%s", className, methodName);

    Timer.Sample timer = Timer.start(registry);

    //@formatter:off
    return Try.of(joinPoint::proceed)
              .map(value ->
                  Match(value).of(
                      Case($(instanceOf(Mono.class)),
                          m -> m.doOnTerminate(() -> stopTimer(className, methodName, metricName, timer))),

                      Case($(instanceOf(Flux.class)),
                          f -> f.doOnTerminate(() -> stopTimer(className, methodName, metricName, timer))),

                      Case($(), res -> {
                        stopTimer(className, methodName, metricName, timer);
                        return res;
                      })
                  ))
              .get();
    //@formatter:on
  }

  private void stopTimer(String className, String methodName, String metricName, Timer.Sample timer) {
    long elapsedTime = timer.stop(getTimer(className, methodName, metricName));
    long elapsedTimeInSeconds = TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);

    LOGGER.debug("Execution of method : {} took : {} milliseconds", metricName, elapsedTimeInSeconds);
  }

  private Timer getTimer(String className, String methodName, String metricName) {
    //@formatter:off
    return Timer.builder(metricName)
                .tags(Tags.of("class", className,
                              "method", methodName))
                .publishPercentiles(0.75, 0.95, 0.99)
                .register(registry);
    //@formatter:on
  }

}
