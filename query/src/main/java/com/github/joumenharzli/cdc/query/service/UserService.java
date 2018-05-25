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

package com.github.joumenharzli.cdc.query.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.github.joumenharzli.cdc.query.domain.User;
import com.github.joumenharzli.cdc.query.service.dto.QueryParameter;

import reactor.core.publisher.Mono;

/**
 * Search Service for the index {@link User}
 *
 * @author Joumen Harzli
 */
public interface UserService {

  /**
   * Find users using the provided parameters
   *
   * @param parameters filters to use
   * @param pageable   page number, size and sorting
   * @return the found users in a page
   * @throws IllegalArgumentException if any given argument is invalid
   */
  Mono<Page<User>> findByCriteria(List<QueryParameter> parameters, Pageable pageable);

}
