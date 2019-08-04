package com.xtestw.graphql.demo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.xtestw.graphql.demo.schema.model.Query;
import graphql.GraphQL;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by xuwei on 2019/8/3
 */
@RestController
@Slf4j
@RequestMapping("/graphql")
@CrossOrigin
public class GraphQLController {

  @Resource
  GraphQL graphQL;

  @PostMapping(path = {""})
  private Object query(@RequestBody String queryStr) throws IOException {
    Query query = getQuery(queryStr);
    return graphQL.execute(query.toExecutionInput());
  }

  private static ObjectMapper mapper = new ObjectMapper();
  private static final MapType VARIABLES_TYPE = mapper.getTypeFactory()
      .constructMapType(HashMap.class,
          String.class, Object.class);

  private Query getQuery(String queryText) throws IOException {
    String operationName = null;
    String fullQueryText = queryText;
    Map<String, Object> variables = null;
    JsonNode jsonBody = mapper.readTree(queryText);
    if (jsonBody != null) {
      JsonNode queryNode = jsonBody.get("query");
      if (queryNode != null && queryNode.isTextual()) {
        queryText = queryNode.asText();
      }
      JsonNode operationNameNode = jsonBody.get("operationName");
      if (operationNameNode != null && operationNameNode.isTextual()) {
        operationName = operationNameNode.asText();
      }
      JsonNode variablesNode = jsonBody.get("variables");
      if (variablesNode != null) {
        if (variablesNode.isTextual()) {
          String variablesJson = variablesNode.asText();
          variables = mapper.convertValue(mapper.readTree(variablesJson), VARIABLES_TYPE);
        } else if (variablesNode.isObject()) {
          variables = mapper.convertValue(variablesNode, VARIABLES_TYPE);
        }
      }
    }
    if (variables == null) {
      variables = Collections.emptyMap();
    }
    return new Query(fullQueryText, queryText, operationName, variables);
  }
}
