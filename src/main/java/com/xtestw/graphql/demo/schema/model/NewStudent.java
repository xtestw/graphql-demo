package com.xtestw.graphql.demo.schema.model;

import com.xtestw.graphql.demo.storage.Student;
import com.xtestw.graphql.demo.storage.Student.Sex;
import java.io.Serializable;
import lombok.Data;

/**
 * Create by xuwei on 2019/8/4
 */
@Data
public class NewStudent implements Serializable {

  String name;
  Sex sex;

  public Student toStudent() {
    Student student = new Student();
    student.setName(name);
    student.setSex(sex);
    return student;
  }
}
