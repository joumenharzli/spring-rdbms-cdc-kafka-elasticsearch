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

package com.github.joumenharzli.cdc.command.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import com.github.joumenharzli.cdc.command.domain.User;
import com.github.joumenharzli.cdc.command.service.dto.UserDto;

/**
 * UserMapper
 *
 * @author Joumen Harzli
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, JobMapper.class},
    nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

  @Mapping(target = "id", expression = "java( user.getId() == null ? null : java.util.UUID.fromString(user.getId()) )")
  User toEntity(UserDto user);

  @Mapping(target = "id", expression = "java( user.getId() == null ? null : user.getId().toString() )")
  UserDto toDto(User user);

}
