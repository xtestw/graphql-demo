package com.xtestw.graphql.demo.config;

import com.xtestw.graphql.demo.schema.ExtendedScalars;
import com.xtestw.graphql.demo.schema.wiring.Wiring;
import graphql.GraphQL;
import graphql.GraphQLException;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.schema.idl.errors.SchemaProblem;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Create by xuwei on 2019/8/3
 */
@Configuration
@Slf4j
public class GraphQLConfig {

  @Value("classpath*:schemas/*.graphqls")
  private Resource[] files;

  @Bean
  GraphQL graphQL(@Autowired GraphQLSchema graphQLSchema) {

    return GraphQL.newGraphQL(graphQLSchema)
        .build();
  }


  @Bean
  GraphQLSchema graphQLSchema(@Autowired RuntimeWiring wiring) {
    SchemaParser parser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = Arrays.stream(files).map(file -> {
      try {
        return file.getInputStream();
      } catch (IOException e) {
        log.error("Load graphql file error: {} - {}", file, e);
      }
      return null;
    }).filter(Objects::nonNull)
        .map(inputStream -> {
          try {
            return parser.parse(new InputStreamReader(inputStream));
          } catch (SchemaProblem e) {
            throw new GraphQLException(
                String.format("Compile schema '%s' failed: %s", inputStream,
                    e.getErrors().stream().map(Object::toString).collect(Collectors.toList())), e);
          }
        }).reduce(new TypeDefinitionRegistry(), (all, cur) -> {
          all.merge(cur);
          return all;
        });
    return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, wiring);
  }

  @Bean
  RuntimeWiring wiring(@Autowired List<GraphQLScalarType> scalarTypes,
      @Autowired List<TypeRuntimeWiring> types) {
    RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();
    if (scalarTypes != null) {
      scalarTypes.forEach(builder::scalar);
    }
    if (types != null) {
      types.forEach(builder::type);
    }
    return builder.build();
  }

  @Bean
  List<GraphQLScalarType> scalarTypes() {
    return Collections.singletonList(ExtendedScalars.GraphQLDate);
  }

  @Bean
  List<TypeRuntimeWiring> types(@Autowired List<Wiring> wirings) {

    TypeRuntimeWiring.Builder queryBuilder = TypeRuntimeWiring.newTypeWiring("Query");
    TypeRuntimeWiring.Builder mutationBuilder = TypeRuntimeWiring.newTypeWiring("Mutation");
    return wirings.stream().map(wiring -> {
      wiring.wireQueries(queryBuilder);
      wiring.wireMutations(mutationBuilder);
      return wiring.wireTypes();
    })
        .reduce(new ArrayList<>(Arrays.asList(queryBuilder.build(), mutationBuilder.build())),
            (all, cur) -> {
              all.addAll(cur);
              return all;
            });
  }

}
