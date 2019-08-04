package com.xtestw.graphql.demo.schema.model;

import graphql.ExecutionInput;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huangdong
 */
@Data
@AllArgsConstructor
public class Query {

  private String body;
  private String query;
  private String operationName;
  private Map<String, Object> variables;

  public ExecutionInput toExecutionInput() {
    return ExecutionInput.newExecutionInput()
        .query(query)
        .operationName(operationName)
        .variables(variables)
        .build();
  }
}
