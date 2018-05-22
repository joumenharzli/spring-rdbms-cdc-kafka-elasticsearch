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

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.joumenharzli.cdc.command.domain.User;
import com.github.joumenharzli.cdc.command.exception.EntityNotFoundException;
import com.github.joumenharzli.cdc.command.repository.UserRepository;
import com.github.joumenharzli.cdc.command.service.dto.UserDto;
import com.github.joumenharzli.cdc.command.service.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Implementation of {@link UserService}
 *
 * @author Joumen Harzli
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  /**
   * Create a user
   *
   * @param userDto entity to create
   * @return the created user
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @Override
  public Mono<UserDto> create(UserDto userDto) {
    LOGGER.debug("Request to create user : {}", userDto);
    Assert.notNull(userDto, "User cannot be null");
    Assert.isNull(userDto.getId(), "User should not have an id");

    return Mono.fromSupplier(() -> userMapper.toEntity(userDto))
        .map(userRepository::save)
        .map(userMapper::toDto);
  }

  /**
   * Update a user
   *
   * @param userDto entity to update
   * @return the updated user
   * @throws EntityNotFoundException  if no entity was found
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @Override
  public Mono<UserDto> update(UserDto userDto) {
    LOGGER.debug("Request update an existing user : {}", userDto);
    Assert.notNull(userDto, "User cannot be null");
    Assert.hasText(userDto.getId(), "Id of the user cannot be null/empty");

    return Mono.fromSupplier(() -> userMapper.toEntity(userDto))
        .map(user -> {
          // assert that the user exists
          findById(user.getId());

          return userRepository.save(user);
        })
        .map(userMapper::toDto);
  }

  /**
   * Delete a user using his id
   *
   * @param userId id of the user to delete
   * @return an empty Mono if success
   * @throws EntityNotFoundException  if no entity was found
   * @throws IllegalArgumentException when any given argument is invalid
   */
  @Override
  public Mono<Void> deleteById(String userId) {
    LOGGER.debug("Request to delete user with id {}", userId);
    Assert.hasText(userId, "Id of the user cannot be null/empty");

    return Mono.fromSupplier(() -> UUID.fromString(userId))
        .map(this::findById)
        .map(this::delete)
        .then();
  }

  /**
   * Find user by his id
   *
   * @param id id of the user to find
   * @return the found user
   * @throws EntityNotFoundException if no entity was found
   */
  private User findById(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id %s was not found", id)));
  }

  /**
   * Delete a user
   *
   * @param user user to delete
   * @return an empty Mono
   */
  private Mono<Void> delete(User user) {
    userRepository.delete(user);
    return Mono.empty();
  }

}
