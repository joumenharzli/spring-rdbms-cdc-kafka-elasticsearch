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
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User Entity
 *
 * @author Joumen Harzli
 */
@Entity
@Table(name = "USERS")
@Data
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "ID", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "AGE")
  private Integer age;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
  private List<Address> addresses = Lists.newArrayList();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
  private List<Job> jobs = Lists.newArrayList();

}
