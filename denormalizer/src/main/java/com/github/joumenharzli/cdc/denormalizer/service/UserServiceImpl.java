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

package com.github.joumenharzli.cdc.denormalizer.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.joumenharzli.cdc.denormalizer.domain.User;
import com.github.joumenharzli.cdc.denormalizer.exception.EntityNotFoundException;
import com.github.joumenharzli.cdc.denormalizer.repository.UserRepository;
import com.github.joumenharzli.cdc.denormalizer.service.dto.AddressDto;
import com.github.joumenharzli.cdc.denormalizer.service.dto.JobDto;
import com.github.joumenharzli.cdc.denormalizer.service.dto.UserDto;
import com.github.joumenharzli.cdc.denormalizer.service.mapper.AddressMapper;
import com.github.joumenharzli.cdc.denormalizer.service.mapper.JobMapper;
import com.github.joumenharzli.cdc.denormalizer.service.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * User Service
 *
 * @author Joumen Harzli
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AddressMapper addressMapper;
  private final JobMapper jobMapper;

  @Override
  public Mono<Void> save(UserDto userDto) {
    LOGGER.debug("Request to save user : {}", userDto);

    Assert.notNull(userDto, "User cannot be null");

    //@formatter:off
    return Mono.fromSupplier(() -> userMapper.toEntity(userDto))
               .publishOn(Schedulers.parallel())
               .doOnNext(userRepository::save)
               .then();
    //@formatter:on
  }

  @Override
  public Mono<Void> delete(UserDto userDto) {
    LOGGER.debug("Request to delete the user : {}", userDto);

    Assert.notNull(userDto, "User cannot be null");
    Assert.hasText(userDto.getId(), "User id cannot be null/empty");

    //@formatter:off
    return this.findById(userDto.getId()).publishOn(Schedulers.parallel())
								.doOnNext(userRepository::delete)
								.then();
	//@formatter:on
  }

  private Mono<User> findById(String id) {
    LOGGER.debug("Request to find the user with id : {}", id);

    //@formatter:off
    return Mono.defer(() -> Mono.just(userRepository.findById(id)
								.orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id %s was not found", id)))))
			   .subscribeOn(Schedulers.elastic());
	//@formatter:on
  }

  @Override
  public Mono<Void> saveUserAddress(AddressDto addressDto) {
    LOGGER.debug("Request to add address {} to the user", addressDto);

    Assert.notNull(addressDto, "Address cannot be null");

    //@formatter:off
    return Mono.justOrEmpty(addressDto.getUserId())
               .flatMap(this::findById)
               .doOnNext(user -> user.getAddresses().add(addressMapper.toEntity(addressDto)))
               .publishOn(Schedulers.parallel())
               .doOnNext(userRepository::save)
               .then();
    //@formatter:on
  }

  @Override
  public Mono<Void> deleteUserAddress(AddressDto addressDto) {
    LOGGER.debug("Request to delete address {} of the user", addressDto);

    Assert.notNull(addressDto, "Address cannot be null");

    //@formatter:off
    return Mono.justOrEmpty(addressDto.getUserId())
               .flatMap(this::findById)
               .doOnNext(user -> user.getAddresses().remove(addressMapper.toEntity(addressDto)))
               .publishOn(Schedulers.parallel())
               .doOnNext(userRepository::save)
               .then();
    //@formatter:on
  }

  @Override
  public Mono<Void> saveUserJob(JobDto jobDto) {
    LOGGER.debug("Request to add job {} to the user", jobDto);

    Assert.notNull(jobDto, "Job cannot be null");

    //@formatter:off
    return Mono.justOrEmpty(jobDto.getUserId())
               .flatMap(this::findById)
               .doOnNext(user -> user.getJobs().add(jobMapper.toEntity(jobDto)))
               .publishOn(Schedulers.parallel())
               .doOnNext(userRepository::save)
               .then();
    //@formatter:on
  }

  @Override
  public Mono<Void> deleteUserJob(JobDto jobDto) {
    LOGGER.debug("Request to delete job {} of the user", jobDto);

    Assert.notNull(jobDto, "Address cannot be null");

    //@formatter:off
    return Mono.justOrEmpty(jobDto.getUserId())
               .flatMap(this::findById)
               .doOnNext(user -> user.getJobs().remove(jobMapper.toEntity(jobDto)))
               .publishOn(Schedulers.parallel())
               .doOnNext(userRepository::save)
               .then();
    //@formatter:on
  }


}
