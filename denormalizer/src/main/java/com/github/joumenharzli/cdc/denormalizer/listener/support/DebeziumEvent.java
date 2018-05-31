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

package com.github.joumenharzli.cdc.denormalizer.listener.support;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Debezium Event
 *
 * @author Joumen Harzli
 */
@Data
public class DebeziumEvent<T> {

  private Map<String, Object> schema;
  private DebeziumEventPayload<T> payload;

  @RequiredArgsConstructor
  public enum DebeziumEventPayloadOperation {
    CREATE("c"), UPDATE("u"), DELETE("r");
    private final String value;
  }

  @Data
  public static class DebeziumEventPayload<T> {
    private T before;
    private T after;
    private Map<String, Object> source;

    @JsonProperty("op")
    private DebeziumEventPayloadOperation operation;

    @JsonProperty("ts_ms")
    private Instant date;

  }


}
