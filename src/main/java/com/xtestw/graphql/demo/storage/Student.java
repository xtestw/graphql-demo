package com.xtestw.graphql.demo.storage;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Create by xuwei on 2019/8/3
 */
@Data
@Entity
@Table(name = "student", indexes = {
    @Index(name = "name", columnList = "name", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
public class Student implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(length = 128)
  private String name;
  private Sex sex;

  @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  private Date creation;

  public enum Sex {
    MALE, FEMALE
  }
}


