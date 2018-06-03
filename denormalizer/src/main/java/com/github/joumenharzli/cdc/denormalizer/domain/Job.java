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

package com.github.joumenharzli.cdc.denormalizer.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Date;

/**
 * Job Type
 *
 * @author Joumen Harzli
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class Job implements Serializable {

  private String id;

  private String name;

  private String description;

  @Field(type = Date, format = DateFormat.date_time)
  private Date startDate;


  @Field(type = Date, format = DateFormat.date_time)
  private Date endDate;

}
