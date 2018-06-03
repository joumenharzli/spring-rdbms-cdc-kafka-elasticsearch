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
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.function.Supplier;

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
  public void save(UserDto userDto) {
    LOGGER.debug("Request to save user : {}", userDto);

    Assert.notNull(userDto, "User cannot be null");

    userRepository.save(userMapper.toEntity(userDto));
  }

  @Override
  public void delete(UserDto userDto) {
    LOGGER.debug("Request to delete the user : {}", userDto);

    Assert.notNull(userDto, "User cannot be null");
    Assert.hasText(userDto.getId(), "User id cannot be null/empty");

    userRepository.delete(findById(userDto.getId()));
  }

  @Override
  public void saveUserAddress(AddressDto addressDto) {
    LOGGER.debug("Request to save address {} of the user", addressDto);

    Assert.notNull(addressDto, "Address cannot be null");

    String userId = addressDto.getUserId();

    if (StringUtils.isEmpty(userId)) {
      LOGGER.debug("Request to save address {} of the user is ignored", addressDto);
      return;
    }

    User user = findById(userId);
    user.getAddresses().add(addressMapper.toEntity(addressDto));
    userRepository.save(user);
  }

  @Override
  public void deleteUserAddress(AddressDto addressDto) {
    LOGGER.debug("Request to delete address {} of the user", addressDto);

    Assert.notNull(addressDto, "Address cannot be null");

    String userId = addressDto.getUserId();

    if (StringUtils.isEmpty(userId)) {
      LOGGER.debug("Request to remove address {} of the user is ignored", addressDto);
      return;
    }

    User user = findById(userId);
    user.getAddresses().remove(addressMapper.toEntity(addressDto));
    userRepository.save(user);
  }

  @Override
  public void saveUserJob(JobDto jobDto) {
    LOGGER.debug("Request to save job {} of the user", jobDto);

    Assert.notNull(jobDto, "Job cannot be null");

    String userId = jobDto.getUserId();

    if (StringUtils.isEmpty(userId)) {
      LOGGER.debug("Request to save job {} of the user is ignored", jobDto);
      return;
    }

    User user = findById(userId);
    user.getJobs().add(jobMapper.toEntity(jobDto));
    userRepository.save(user);
  }

  @Override
  public void deleteUserJob(JobDto jobDto) {
    LOGGER.debug("Request to delete job {} of the user", jobDto);

    Assert.notNull(jobDto, "Job cannot be null");

    String userId = jobDto.getUserId();

    if (StringUtils.isEmpty(userId)) {
      LOGGER.debug("Request to remove job {} of the user is ignored", jobDto);
      return;
    }

    User user = findById(userId);
    user.getJobs().remove(jobMapper.toEntity(jobDto));
    userRepository.save(user);
  }

  private User findById(String id) {
    LOGGER.debug("Request to find the user with id : {}", id);

    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format("Entity with id %s was not found", id)));
  }


}
