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

package com.github.joumenharzli.cdc.denormalizer.service.dto;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.joumenharzli.cdc.denormalizer.service.support.BytesToUUIDDeserializer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Job Dto
 *
 * @author Joumen Harzli
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class JobDto implements Serializable {

  @JsonProperty("id")
  @JsonDeserialize(using = BytesToUUIDDeserializer.class)
  private String id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("description")
  private String description;

  @JsonProperty("start_date")
  private Instant startDate;

  @JsonProperty("end_date")
  private Instant endDate;

  @JsonProperty("user_id")
  @JsonDeserialize(using = BytesToUUIDDeserializer.class)
  private String userId;
}
