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

package com.github.joumenharzli.cdc.denormalizer.config;

import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;
import java.util.UUID;

import static org.springframework.kafka.listener.AbstractMessageListenerContainer.AckMode.MANUAL;

/**
 * Kafka Consumer Configuration
 *
 * @author Joumen Harzli
 */
@Configuration
@EnableKafka
public class KafkaConfiguration {

  @Value("${application.kafka.bootstrapServers}")
  private String bootstrapServers;

  @Value("${application.kafka.groupId}")
  private String groupId;

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, DebeziumEvent>
  kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, DebeziumEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

    factory.setConsumerFactory(consumerFactory());
    factory.getContainerProperties().setAckMode(MANUAL);
    factory.setBatchListener(true);

    return factory;
  }

  @Bean
  public ConsumerFactory<String, DebeziumEvent> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs(),
                                             new StringDeserializer(),
                                             new JsonDeserializer<>(DebeziumEvent.class));
  }

  @Bean
  public Map<String, Object> consumerConfigs() {
    return ImmutableMap.<String, Object>builder()
        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers)
        .put(ConsumerConfig.GROUP_ID_CONFIG, groupId + UUID.randomUUID().toString()+"123")
        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class)
        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        .put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
        .put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10")
        .build();
  }

}
