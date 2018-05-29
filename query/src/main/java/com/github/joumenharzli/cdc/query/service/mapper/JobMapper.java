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

package com.github.joumenharzli.cdc.query.service.mapper;


import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import com.github.joumenharzli.cdc.query.domain.Job;
import com.github.joumenharzli.cdc.query.service.dto.JobDto;

/**
 * JobMapper
 *
 * @author Joumen Harzli
 */
@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface JobMapper {

  @Mapping(target = "id",
      expression = "java( job.getId() == null ? null : job.getId().toString() )",
      ignore = true)
  JobDto toDto(Job job);

  List<JobDto> toDtos(List<Job> job);

}
