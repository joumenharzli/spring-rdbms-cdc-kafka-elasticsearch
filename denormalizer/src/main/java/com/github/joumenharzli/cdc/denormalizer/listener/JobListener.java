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

package com.github.joumenharzli.cdc.denormalizer.listener;

import java.util.Map;
import java.util.function.Function;
import javax.annotation.PostConstruct;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent;
import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent.DebeziumEventPayload;
import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent.DebeziumEventPayloadOperation;
import com.github.joumenharzli.cdc.denormalizer.service.UserService;
import com.github.joumenharzli.cdc.denormalizer.service.dto.JobDto;
import com.google.common.collect.Maps;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Job Listener
 *
 * @author Joumen Harzli
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JobListener {

  private final UserService userService;
  private final Map<DebeziumEventPayloadOperation, Function<DebeziumEventPayload<JobDto>, Mono<Void>>> jobActions = Maps.newConcurrentMap();

  @PostConstruct
  public void init() {
    jobActions.put(DebeziumEventPayloadOperation.CREATE, payload -> userService.saveUserJob(payload.getAfter()));
    jobActions.put(DebeziumEventPayloadOperation.UPDATE, payload -> userService.saveUserJob(payload.getAfter()));
    jobActions.put(DebeziumEventPayloadOperation.DELETE, payload -> userService.deleteUserJob(payload.getBefore()));
  }

  @KafkaListener(topics = "mysqlcdc.cdc.jobs")
  public void handleJobEvent(DebeziumEvent<JobDto> event) {

    DebeziumEventPayload<JobDto> payload = event.getPayload();

    LOGGER.debug("Handling job event with payload : {}", payload);

    process(payload).subscribe();
  }

  @Timed
  private Mono<Void> process(DebeziumEventPayload<JobDto> payload) {
    DebeziumEventPayloadOperation operation = payload.getOperation();
    return jobActions.get(operation).apply(payload);
  }


}
