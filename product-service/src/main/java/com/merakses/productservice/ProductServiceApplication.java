package com.merakses.productservice;

import com.merakses.specificationfilterlib.config.EnableSpecificationFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableSpecificationFilter
public class ProductServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }

}
