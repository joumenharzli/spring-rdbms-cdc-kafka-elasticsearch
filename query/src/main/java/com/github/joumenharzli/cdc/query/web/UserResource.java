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

package com.github.joumenharzli.cdc.query.web;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;
import com.github.joumenharzli.cdc.query.domain.User;
import com.github.joumenharzli.cdc.query.service.UserService;
import com.github.joumenharzli.cdc.query.service.dto.QueryParameter;
import com.github.joumenharzli.cdc.query.util.QueryUtils;
import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Reactive REST resource for the index {@link User}
 *
 * @author Joumen Harzli
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserResource {

  private final UserService userService;

  /**
   * GET /users/search/:parameters: get users using multiple criteria
   * <p>
   * example: /users/search/name=joumen&job.name=engineer&age>20&page=0&size=10
   *
   * @param parameters filters to use separated by "&"
   * @param page       number of the page
   * @param size       size of the page
   * @return the ResponseEntity with status 200 (OK) and with body containing the page with the found results
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @GetMapping(value = "/search/{parameters}")
  @Timed
  public Page<User> search(@PathVariable(value = "parameters", required = false) String parameters,
                           @RequestParam(name = "page") int page,
                           @RequestParam(name = "size") int size) {

    LOGGER.debug("REST request to search for users with parameters {} and page {} and size {}", parameters, page, size);

    //@formatter:off
    List<QueryParameter> parameterList = Optional.ofNullable(parameters)
                                                 .map(QueryUtils::parseURLParameters)
                                                 .orElseGet(Lists::newArrayList);
    //@formatter:on

    return userService.findByCriteria(parameterList, PageRequest.of(page, size));
  }

}
