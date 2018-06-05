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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;

import java.io.IOException;

/**
 * Create a custom elastic search template that supports JSR-310
 *
 * @author Joumen Harzli
 */
@Configuration
public class ElasticsearchConfiguration {

  @Bean
  public ElasticsearchTemplate elasticsearchTemplate(Client client, ObjectMapper objectMapper) {
    return new ElasticsearchTemplate(client, new CustomEntityMapper(objectMapper));
  }

  /**
   * A custom entity mapper inspired from {@link DefaultEntityMapper}
   */
  class CustomEntityMapper implements EntityMapper {

    private final ObjectMapper objectMapper;

    CustomEntityMapper(ObjectMapper objectMapper) {
      // clone the current object mapper that have JSR-310 modules registred
      this.objectMapper = objectMapper.copy();

      this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      this.objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
      this.objectMapper.registerModule(new CustomGeoModule());
    }

    @Override
    public String mapToString(Object object) throws IOException {
      return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
      return objectMapper.readValue(source, clazz);
    }
  }

}