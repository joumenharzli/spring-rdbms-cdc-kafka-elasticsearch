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

package com.github.joumenharzli.cdc.command.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * Job Entity
 *
 * @author Joumen Harzli
 */
@Entity
@Data
@Table(name = "JOBS")
public class Job implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "ID", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "START_DATE")
  private Instant startDate;

  @Column(name = "END_DATE")
  private Instant endDate;

  @Column(name = "USER_ID")
  private UUID userId;
}
