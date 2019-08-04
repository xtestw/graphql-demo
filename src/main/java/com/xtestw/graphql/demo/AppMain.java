package com.xtestw.graphql.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Create by xuwei on 2019/8/3
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.xtestw.graphql.demo"})
@EnableJpaRepositories(basePackages = {"com.xtestw.graphql.demo.storage"})
@EnableJpaAuditing
@EnableScheduling
@EntityScan(basePackages = {"com.xtestw.graphql.demo"})
public class AppMain {

  public static void main(String[] args) {
    SpringApplication.run(AppMain.class, args);
  }
}
