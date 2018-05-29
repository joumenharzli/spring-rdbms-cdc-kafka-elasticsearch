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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.github.joumenharzli.cdc.query.repository.UserRepository;
import com.github.joumenharzli.cdc.query.service.dto.QueryParameter;
import com.github.joumenharzli.cdc.query.service.dto.UserDto;
import com.github.joumenharzli.cdc.query.service.mapper.UserMapper;
import com.github.joumenharzli.cdc.query.util.SearchQueryBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Implementation for {@link UserService}
 *
 * @author Joumen Harzli
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  /**
   * Find users using the provided parameters
   *
   * @param parameters filters to use
   * @param pageable   page number, size and sorting
   * @return the found users in a page
   * @throws IllegalArgumentException if any given argument is invalid
   */
  @Override
  public Mono<Page<UserDto>> findByCriteria(List<QueryParameter> parameters, Pageable pageable) {
    LOGGER.debug("Request to search for users with parameters {} and {}", parameters, pageable);

    Assert.notNull(parameters, "List of parameters cannot be null");
    Assert.notNull(pageable, "Pageable cannot be null");

    //@formatter:off
    return Mono.fromSupplier(() -> SearchQueryBuilder.fromParameters(parameters)
                                                        .withPageable(pageable)
                                                        .build())
               .flatMap(searchQuery -> Mono.just(userRepository.search(searchQuery)))
               .subscribeOn(Schedulers.elastic())
               .map(page -> new PageImpl<>(userMapper.toDtos(page.getContent()), page.getPageable(), page.getTotalElements()));
    //@formatter:on
  }


}
