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

package com.github.joumenharzli.cdc.denormalizer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent;
import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent.DebeziumEventPayload;
import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent.DebeziumEventPayloadOperation;
import com.github.joumenharzli.cdc.denormalizer.service.UserService;
import com.github.joumenharzli.cdc.denormalizer.service.dto.AddressDto;
import com.google.common.collect.Maps;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Address Listener
 *
 * @author Joumen Harzli
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AddressEventHandler implements EventHandler {


  private final UserService userService;
  private final Map<DebeziumEventPayloadOperation, BiConsumer<AddressDto, AddressDto>> userActions = Maps.newConcurrentMap();

  @PostConstruct
  public void init() {
    userActions.put(DebeziumEventPayloadOperation.CREATE, (before, after) -> userService.saveUserAddress(after));
    userActions.put(DebeziumEventPayloadOperation.UPDATE, (before, after) -> userService.saveUserAddress(after));
    userActions.put(DebeziumEventPayloadOperation.DELETE, (before, after) -> userService.deleteUserAddress(before));
  }

  @Timed
  @Override
  public void process(DebeziumEvent event) {
    DebeziumEventPayload payload = event.getPayload();

    LOGGER.debug("Request to handle event with payload : {}", payload);

    DebeziumEventPayloadOperation operation = payload.getOperation();

    ObjectMapper mapper = new ObjectMapper();
    AddressDto before = mapper.convertValue(payload.getBefore(), AddressDto.class);
    AddressDto after = mapper.convertValue(payload.getAfter(), AddressDto.class);

    userActions.get(operation).accept(before, after);
  }
}
