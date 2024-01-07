package com.merakses.springsandbox.service;

import com.merakses.springsandbox.dto.ProductFilterDto;
import com.merakses.springsandbox.entity.Product;
import com.merakses.springsandbox.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureTestDatabase
@SpringBootTest
public class ProductServiceImplTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  @BeforeEach
  public void setUp() {
    productRepository.saveAllAndFlush(List.of(
        new Product()
            .setName("Phone")
            .setPrice(100),
        new Product()
            .setName("Laptop")
            .setPrice(300)
    ));
  }

  @AfterEach
  public void tearDown() {
    productRepository.deleteAll();
  }

  @Test
  public void testRepository() {
    List<Product> products = productService.findAll();
    Assertions.assertEquals(2, products.size());
  }

  @Test
  public void testInject() {
    ProductFilterDto filter = ProductFilterDto.builder()
        .minPrice(200)
        .build();
    List<Product> products = productService.search(filter);

    System.out.println(products);
    Assertions.assertEquals(1, products.size());
  }
}