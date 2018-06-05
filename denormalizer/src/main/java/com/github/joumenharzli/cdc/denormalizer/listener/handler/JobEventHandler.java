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

package com.github.joumenharzli.cdc.denormalizer.listener.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joumenharzli.cdc.denormalizer.listener.EventHandler;
import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent.DebeziumEventPayloadOperation;
import com.github.joumenharzli.cdc.denormalizer.service.UserService;
import com.github.joumenharzli.cdc.denormalizer.service.dto.JobDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Job Event Handler
 *
 * @author Joumen Harzli
 */
@Component
@Slf4j
public class JobEventHandler extends AbstractSimpleEventHandler<JobDto> implements EventHandler {

  private final UserService userService;

  public JobEventHandler(ObjectMapper mapper, UserService userService) {
    super(mapper);
    this.userService = userService;
  }

  @PostConstruct
  public void init() {
    initActions();
  }

  @Override
  public void initActions() {
    actions.put(DebeziumEventPayloadOperation.CREATE, (before, after) -> userService.saveUserJob(after));
    actions.put(DebeziumEventPayloadOperation.UPDATE, (before, after) -> userService.saveUserJob(after));
    actions.put(DebeziumEventPayloadOperation.DELETE, (before, after) -> userService.deleteUserJob(before));
  }

}
