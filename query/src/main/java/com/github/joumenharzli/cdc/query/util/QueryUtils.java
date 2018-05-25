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

package com.github.joumenharzli.cdc.query.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.github.joumenharzli.cdc.query.service.dto.QueryOperator;
import com.github.joumenharzli.cdc.query.service.dto.QueryParameter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Query Utils
 *
 * @author Joumen Harzli
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QueryUtils {

  /**
   * Convert a list of URL parameters to a list of {@link QueryParameter}
   *
   * @param parameters url parameters
   * @return list of {@link QueryParameter}
   * @throws IllegalArgumentException if any given argument is invalid
   */
  public static List<QueryParameter> parseURLParameters(String parameters) {
    Assert.hasText(parameters, "url parameters cannot be null/empty");

    //@formatter:off
    return Arrays.stream(parameters.split("&"))
                 .map(QueryUtils::parseURLParameter)
                 .collect(Collectors.toList());
    //@formatter:on
  }

  /**
   * Extract {@link QueryParameter} from text
   *
   * @param text text that contains a valid format of a query parameter like name=Joumen
   * @return the parsed query parameter
   */
  private static QueryParameter parseURLParameter(String text) {

    QueryOperator operator = getQueryOperator(text);
    String[] values = StringUtils.split(text, operator.getOperation());

    //@formatter:off
    return QueryParameter.builder()
                         .field(values[0])
                         .value(values[1])
                         .operator(operator)
                         .build();
    //@formatter:on
  }

  /**
   * Get query operator from text
   *
   * @param text text that contains a valid format of a query parameter like name=Joumen
   * @return the found operator
   * @throws IllegalArgumentException if the text don't have a valid operator
   */
  private static QueryOperator getQueryOperator(String text) {

    //@formatter:off
    return Arrays.stream(QueryOperator.values())
                 .filter(op -> StringUtils.contains(text, op.getOperation()))
                 .findFirst()
                 .orElseThrow(
                     () -> new IllegalArgumentException(String.format("No supported operator was found in the text %s", text)));
    //@formatter:on
  }

}
