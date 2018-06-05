package com.github.joumenharzli.cdc.denormalizer.listener;

import com.github.joumenharzli.cdc.denormalizer.listener.support.DebeziumEvent;

/**
 * Event Handler
 *
 * @author Joumen Harzli
 */
public interface EventHandler {

  void process(DebeziumEvent event);

}
