package com.github.joumenharzli.cdc.denormalizer.listener;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventHandlerFactory {

  private final AddressEventHandler addressEventHandler;
  private final JobEventHandler jobEventHandler;
  private final UserEventHandler userEventHandler;

  private final Map<String, EventHandler> handlers = Maps.newConcurrentMap();

  @PostConstruct
  public void init() {
    LOGGER.debug("Initializing events handlers");

    handlers.put("users", userEventHandler);
    handlers.put("jobs", jobEventHandler);
    handlers.put("addresses", addressEventHandler);
  }

  public EventHandler getHandler(String topicName) {
    Assert.hasText(topicName, "Topic name cannot be null/empty");

    String tableName = StringUtils.substringAfterLast(topicName, ".").toLowerCase();

    return Optional.ofNullable(handlers.get(tableName))
        .orElseThrow(() -> new IllegalArgumentException("No suitable handler was found for the topic " + topicName));
  }

}
