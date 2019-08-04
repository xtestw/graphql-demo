package com.xtestw.graphql.demo.schema;

import graphql.GraphQLException;
import graphql.language.IntValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import java.math.BigInteger;
import java.util.Date;

public class ExtendedScalars {

  private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

  public static final GraphQLScalarType GraphQLDate =
      new GraphQLScalarType("Date", "java.util.Date", new Coercing() {
        @Override
        public Long serialize(Object input) {
          Long value = null;
          if (input != null) {
            if (input instanceof Date) {
              value = ((Date) input).getTime();
            } else if (input instanceof Integer) {
              value = ((Integer) input).longValue();
            } else if (input instanceof Long) {
              value = (Long) input;
            }
          }
          return value;
        }

        @Override
        public Long parseValue(Object input) {
          return serialize(input);
        }

        @Override
        public Object parseLiteral(Object input) {
          if (!(input instanceof IntValue)) {
            return null;
          }
          BigInteger value = ((IntValue) input).getValue();
          if (value.compareTo(BigInteger.ZERO) < 0 || value.compareTo(LONG_MAX) > 0) {
            throw new GraphQLException("Time literal is less than 0 or too big for a Long");
          }
          return new Date(value.longValue());
        }
      });
}