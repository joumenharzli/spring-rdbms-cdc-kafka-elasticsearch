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

package com.github.joumenharzli.cdc.command.web;

import java.net.URI;
import java.util.Objects;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.annotation.Timed;
import com.github.joumenharzli.cdc.command.domain.User;
import com.github.joumenharzli.cdc.command.service.UserService;
import com.github.joumenharzli.cdc.command.service.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Reactive REST resource for the entity {@link User}
 *
 * @author Joumen Harzli
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

  private final UserService userService;

  /**
   * POST /users: create a new user
   *
   * @param userDto entity to save
   * @return the ResponseEntity with status 201 (Created) and with body the new user
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @PostMapping
  @Timed
  Mono<ResponseEntity<UserDto>> create(@Valid @RequestBody UserDto userDto) {
    LOGGER.debug("REST request to create a new user : {}", userDto);

    if (Objects.nonNull(userDto.getId())) {
      throw new IllegalArgumentException("A new user cannot already have an ID");
    }

    //@formatter:off
    return userService.create(userDto)
					  .map(createdUser -> ResponseEntity.created(URI.create(String.format("/api/v1/users/%s", createdUser.getId())))
														.body(createdUser));
	//@formatter:on
  }

  /**
   * PUT /users: update an existing user
   *
   * @param userDto entity to save
   * @return the ResponseEntity with status 200 (OK) and with body the updated user
   * or the ResponseEntity with status 404 (NOT FOUND) if the user was not found
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @PutMapping
  @Timed
  Mono<ResponseEntity<UserDto>> update(@Valid @RequestBody UserDto userDto) {
    LOGGER.debug("REST request update an existing user : {}", userDto);

    if (Objects.isNull(userDto.getId())) {
      throw new IllegalArgumentException("The provided user should have an id");
    }

    //@formatter:off
    return userService.update(userDto)
					  .map(updatedUser -> ResponseEntity.ok().body(updatedUser));
	//@formatter:on
  }

  /**
   * DELETE /users/:userId: delete an existing user
   *
   * @param userId id of the user
   * @return the ResponseEntity with status 200 (OK) if deleted
   * or the ResponseEntity with status 404 (NOT FOUND) if the user was not found
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @DeleteMapping("/{userId}")
  @Timed
  Mono<ResponseEntity<Void>> deleteById(@PathVariable("userId") String userId) {
    LOGGER.debug("REST request to delete the user with id : {}", userId);

    if (StringUtils.isEmpty(userId)) {
      throw new IllegalArgumentException("The provided id is invalid");
    }

    //@formatter:off
    return userService.deleteById(userId)
					  .map(v -> ResponseEntity.ok().build());
	//@formatter:on
  }


}
