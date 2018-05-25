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

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.util.Assert;

import com.github.joumenharzli.cdc.query.service.dto.QueryParameter;

import static com.github.joumenharzli.cdc.query.service.dto.QueryOperator.*;
import static io.vavr.API.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Builder of {@link SearchQuery}
 *
 * @author Joumen Harzli
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class SearchQueryBuilder {

  private static final String DOT = ".";

  private final List<QueryParameter> parameters;
  private final NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();

  private Pageable pageable;

  /**
   * Create an instance of {@link SearchQueryBuilder}
   *
   * @param parameters filters to use
   * @return an instance of {@link SearchQueryBuilder}
   * @throws IllegalArgumentException if parameters list is invalid
   */
  public static SearchQueryBuilder fromParameters(List<QueryParameter> parameters) {
    Assert.notNull(parameters, "List of parameters cannot be null");

    return new SearchQueryBuilder(parameters);
  }

  /**
   * Add pagination
   *
   * @param pageable specifications of the page
   * @return an instance of {@link SearchQueryBuilder}
   */
  public SearchQueryBuilder withPageable(Pageable pageable) {
    this.pageable = pageable;
    return this;
  }

  /**
   * @return build and return the search query
   */
  public SearchQuery build() {
    LOGGER.debug("Request to build a search query using parameters {} and pageable {}", parameters, pageable);

    parseParameters();

    if (Objects.nonNull(pageable)) {
      searchQuery.withPageable(pageable);
    }

    return searchQuery.build();
  }

  /**
   * Parse parameters and create queries
   */
  private void parseParameters() {
    parameters.forEach(
        parameter ->
            Match(parameter.getOperator())
                .of(
                    Case($(EQUALS), () -> run(() -> addEqualsQuery(parameter))),
                    Case($(DIFFERENT), () -> run(() -> addDifferentQuery(parameter))),
                    Case($(GREATER), () -> run(() -> addGreaterQuery(parameter))),
                    Case($(LESS), () -> run(() -> addLessQuery(parameter)))
                ));
  }

  /**
   * Add "equals" query
   *
   * @param parameter query parameters
   * @return an instance of {@link NativeSearchQueryBuilder}
   */
  private NativeSearchQueryBuilder addEqualsQuery(QueryParameter parameter) {
    return
        getQuery(
            parameter,
            () -> boolQuery().must(matchQuery(parameter.getField(), parameter.getValue())));
  }

  /**
   * Add "different" query
   *
   * @param parameter query parameters
   * @return an instance of {@link NativeSearchQueryBuilder}
   */
  private NativeSearchQueryBuilder addDifferentQuery(QueryParameter parameter) {
    return
        getQuery(
            parameter,
            () -> boolQuery()
                .mustNot(matchQuery(parameter.getField(), parameter.getValue())));
  }

  /**
   * Add "greater" query
   *
   * @param parameter query parameters
   * @return an instance of {@link NativeSearchQueryBuilder}
   */
  private NativeSearchQueryBuilder addGreaterQuery(QueryParameter parameter) {
    return
        getQuery(
            parameter,
            () -> rangeQuery(parameter.getField()).gt(parameter.getValue()));
  }

  /**
   * Add "less" query
   *
   * @param parameter query parameters
   * @return an instance of {@link NativeSearchQueryBuilder}
   */
  private NativeSearchQueryBuilder addLessQuery(QueryParameter parameter) {
    return
        getQuery(
            parameter,
            () -> rangeQuery(parameter.getField()).lt(parameter.getValue()));
  }

  /**
   * Add query or nested query depending on the provided field.
   * if the field name contains a dot "." then we should extract the index name
   *
   * @param parameter query parameters
   * @param function  a supplier that defines the query that will be added
   * @return an instance of {@link NativeSearchQueryBuilder}
   */
  private NativeSearchQueryBuilder getQuery(QueryParameter parameter,
                                            Supplier<AbstractQueryBuilder> function) {

    if (StringUtils.contains(parameter.getField(), DOT)) {
      String indexName = StringUtils.substringBeforeLast(parameter.getField(), DOT);
      return searchQuery.withQuery(nestedQuery(indexName, function.get(), ScoreMode.None));
    } else {
      return searchQuery.withQuery(function.get());
    }

  }

}
