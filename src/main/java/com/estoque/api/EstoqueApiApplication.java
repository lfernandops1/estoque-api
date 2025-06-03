package com.estoque.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.estoque.api")
public class EstoqueApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(EstoqueApiApplication.class, args);
  }
}
