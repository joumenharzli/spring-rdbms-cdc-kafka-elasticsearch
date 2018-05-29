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

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.joumenharzli.cdc.query.domain.User;
import com.github.joumenharzli.cdc.query.service.UserService;
import com.github.joumenharzli.cdc.query.util.QueryUtils;
import com.google.common.collect.Lists;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
  @GetMapping(value = {"/search/{parameters}", "/search"})
  @Timed
  public Mono<ResponseEntity<Page<User>>> search(@PathVariable Optional<String> parameters,
                                                 @RequestParam(name = "page") int page,
                                                 @RequestParam(name = "size") int size) {

    LOGGER.debug("REST request to search for users with parameters {} and page {} and size {}", parameters, page, size);

    //@formatter:off
    return Mono.fromSupplier(() -> parameters.map(QueryUtils::parseURLParameters)
                                             .orElse(Lists.newArrayList()))
               .flatMap(parameterList -> userService.findByCriteria(parameterList, PageRequest.of(page, size)))
               .map(ResponseEntity::ok);
    //@formatter:on
  }

}
