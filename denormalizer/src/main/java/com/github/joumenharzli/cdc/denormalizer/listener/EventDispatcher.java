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

import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * User Listener
 *
 * @author Joumen Harzli
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventDispatcher {

  private final EventHandlerFactory handlerFactory;

  @KafkaListener(topics = {"mysqlcdc.cdc.USERS", "mysqlcdc.cdc.JOBS", "mysqlcdc.cdc.ADDRESSES"})
  public void handleUserEvent(@Payload DebeziumEvent event,
                              @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                              Acknowledgment acknowledgment) {

    LOGGER.debug("Request to handle an event in the topic : {}", topic);

    handlerFactory.getHandler(topic).process(event);

    acknowledgment.acknowledge();
  }

}
