package com.xtestw.graphql.demo.schema.wiring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.xtestw.graphql.demo.schema.model.NewStudent;
import com.xtestw.graphql.demo.schema.model.Pagination;
import com.xtestw.graphql.demo.storage.Student;
import com.xtestw.graphql.demo.storage.Student.Sex;
import com.xtestw.graphql.demo.storage.repository.StudentRepository;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.MapEnumValuesProvider;
import graphql.schema.idl.TypeRuntimeWiring;
import graphql.schema.idl.TypeRuntimeWiring.Builder;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Create by xuwei on 2019/8/4
 */
@Component
public class StudentWiring implements Wiring {

  @Autowired
  StudentRepository studentRepository;
  ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<TypeRuntimeWiring> wireTypes() {
    return Collections.singletonList(
        TypeRuntimeWiring.newTypeWiring("Sex")
            .enumValues(new MapEnumValuesProvider(
                ImmutableMap.of(
                    "MALE", Sex.MALE,
                    "FEMALE", Sex.FEMALE
                )))
            .build());
  }

  @Override
  public void wireQueries(Builder queryBuilder) {
    queryBuilder.dataFetcher("student", this::fetchStudentById)
        .dataFetcher("students", this::fetchStudents);
  }

  private List<Student> fetchStudents(DataFetchingEnvironment dataFetchingEnvironment) {
    Pagination pagination = mapper
        .convertValue(dataFetchingEnvironment.getArgument("pagination"), Pagination.class);
    if (pagination == null) {
      pagination = Pagination.create(0, 20);
    }
    return studentRepository.findAll(pagination.toPageable()).getContent();
  }

  private Student fetchStudentById(DataFetchingEnvironment dataFetchingEnvironment) {
    Integer id = dataFetchingEnvironment.getArgument("id");
    return studentRepository.findById(id).orElse(null);
  }

  @Override
  public void wireMutations(Builder mutationBuilder) {
    mutationBuilder.dataFetcher("add", this::addNewStudent);
  }

  private Student addNewStudent(DataFetchingEnvironment dataFetchingEnvironment) {
    NewStudent newStudent = mapper
        .convertValue(dataFetchingEnvironment.getArgument("newStudent"), NewStudent.class);
    return studentRepository.save(newStudent.toStudent());
  }
}
