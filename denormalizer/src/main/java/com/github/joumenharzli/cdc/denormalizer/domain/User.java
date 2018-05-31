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
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.google.common.collect.Sets;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User Index
 *
 * @author Joumen Harzli
 */
@Document(indexName = "users", type = "users")
@Data
@EqualsAndHashCode(of = {"id"})
public class User implements Serializable {

  @Id
  private String id;

  private String name;

  private Integer age;

  @Field(type = FieldType.Nested)
  private Set<Address> addresses = Sets.newHashSet();

  @Field(type = FieldType.Nested)
  private Set<Job> jobs = Sets.newHashSet();

}
