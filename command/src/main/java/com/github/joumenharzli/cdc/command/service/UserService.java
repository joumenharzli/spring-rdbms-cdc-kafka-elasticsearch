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

package com.github.joumenharzli.cdc.command.service;

import com.github.joumenharzli.cdc.command.domain.User;
import com.github.joumenharzli.cdc.command.exception.EntityNotFoundException;
import com.github.joumenharzli.cdc.command.service.dto.UserDto;

import reactor.core.publisher.Mono;

/**
 * Reactive service for the entity {@link User}
 *
 * @author Joumen Harzli
 */
public interface UserService {

  /**
   * Create a user
   *
   * @param userDto entity to create
   * @return the created user
   * @throws IllegalArgumentException when any given argument is invalid
   */
  Mono<UserDto> create(UserDto userDto);

  /**
   * Update a user
   *
   * @param userDto entity to update
   * @return the updated user
   * @throws EntityNotFoundException  if no entity was found
   * @throws IllegalArgumentException when any given argument is invalid
   */
  Mono<UserDto> update(UserDto userDto);

  /**
   * Delete a user using his id
   *
   * @param userId id of the user to delete
   * @return an empty Mono if success
   * @throws EntityNotFoundException  if no entity was found
   * @throws IllegalArgumentException when any given argument is invalid
   */
  Mono<Void> deleteById(String userId);
}
