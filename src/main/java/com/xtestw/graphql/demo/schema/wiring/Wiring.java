package com.xtestw.graphql.demo.schema.wiring;

import graphql.schema.idl.TypeRuntimeWiring;
import java.util.List;

/**
 * Create by xuwei on 2019/8/3
 */
public interface Wiring {

  List<TypeRuntimeWiring> wireTypes();

  void wireQueries(TypeRuntimeWiring.Builder queryBuilder);

  void wireMutations(TypeRuntimeWiring.Builder mutationBuilder);
}
