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

import com.github.joumenharzli.cdc.denormalizer.listener.handler.AddressEventHandler;
import com.github.joumenharzli.cdc.denormalizer.listener.handler.JobEventHandler;
import com.github.joumenharzli.cdc.denormalizer.listener.handler.UserEventHandler;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

/**
 * Event Handler Factory
 *
 * @author Joumen Harzli
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandlerFactory {

  private final AddressEventHandler addressEventHandler;
  private final JobEventHandler jobEventHandler;
  private final UserEventHandler userEventHandler;

  private final Map<String, EventHandler> handlers = Maps.newConcurrentMap();

  @PostConstruct
  public void init() {
    LOGGER.debug("Initializing events handlers");

    handlers.put("users", userEventHandler);
    handlers.put("jobs", jobEventHandler);
    handlers.put("addresses", addressEventHandler);
  }

  public EventHandler getHandler(String topicName) {
    Assert.hasText(topicName, "Topic name cannot be null/empty");

    String tableName = StringUtils.substringAfterLast(topicName, ".").toLowerCase();

    LOGGER.debug("Request to use a handler for the table {}", tableName);

    return Optional.ofNullable(handlers.get(tableName))
        .orElseThrow(() -> new IllegalArgumentException("No suitable handler was found for the topic " + topicName));
  }

}
