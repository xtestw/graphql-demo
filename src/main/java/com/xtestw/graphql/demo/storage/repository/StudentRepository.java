package com.xtestw.graphql.demo.storage.repository;

import com.xtestw.graphql.demo.storage.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Create by xuwei on 2019/8/4
 */
@Component
public interface StudentRepository extends JpaRepository<Student, Integer> {

}
