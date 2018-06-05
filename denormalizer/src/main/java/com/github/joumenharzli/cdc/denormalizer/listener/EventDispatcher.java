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
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Event Dispatcher
 *
 * @author Joumen Harzli
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EventDispatcher {

  private final EventHandlerFactory handlerFactory;

  @KafkaListener(topics = {
      "mysqlcdc.cdc.USERS",
      "mysqlcdc.cdc.JOBS",
      "mysqlcdc.cdc.ADDRESSES"})
  @Timed
  public void handleEvents(List<ConsumerRecord<String, DebeziumEvent>> records,
                              Acknowledgment acknowledgment) {

    LOGGER.debug("Request to process {} records", records.size());

    List<ConsumerRecord<String, DebeziumEvent>> sortedRecords = records.stream()
        .sorted(Comparator.comparing(r -> r.value().getPayload().getDate()))
        .collect(Collectors.toList());

    sortedRecords.forEach(record -> {

      LOGGER.debug("Request to handle {} event in the topic {}", record.value().getPayload().getOperation(), record.topic());

      handlerFactory.getHandler(record.topic()).process(record.value());

    });

    acknowledgment.acknowledge();
  }

}
