package com.xtestw.graphql.demo.schema.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Create by xuwei on 2019/8/3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "create")
public class Pagination implements Serializable {

  private Integer index;
  private Integer size;

  public Pageable toPageable() {
    return PageRequest.of(index, size);
  }
}
