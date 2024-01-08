package com.merakses.springsandbox.service;

import com.merakses.springsandbox.dto.ProductFilterDto;
import com.merakses.springsandbox.entity.Product;
import com.merakses.springsandbox.entity.ProductType;
import com.merakses.springsandbox.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class SpecificationFilterTest {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  private final List<Product> products = List.of(
      new Product().setName("Earphones")
          .setPrice(100)
          .setProductType(ProductType.EARPHONES),
      new Product()
          .setName("Smartphone")
          .setPrice(200)
          .setProductType(ProductType.PHONE),
      new Product()
          .setName("Laptop")
          .setPrice(300)
          .setProductType(ProductType.COMPUTER)
  );

  @BeforeEach
  public void setUp() {
    productRepository.saveAllAndFlush(products);
  }

  @AfterEach
  public void tearDown() {
    productRepository.deleteAll();
  }

  @Test
  public void testRepository() {
    List<Product> actualProducts = productService.findAll();
    Assertions.assertEquals(products.size(), actualProducts.size());
  }

  @Test
  public void greaterThan() {
    int minPrice = 200;
    ProductFilterDto filter = ProductFilterDto.builder()
        .minPrice(minPrice)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .map(Product::getPrice)
        .allMatch(price -> price >= minPrice));
  }

  @Test
  public void lesserThan() {
    int maxPrice = 200;
    ProductFilterDto filter = ProductFilterDto.builder()
        .maxPrice(maxPrice)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .map(Product::getPrice)
        .allMatch(price -> price <= maxPrice));
  }

  @Test
  void inRange() {
    int minPrice = 150;
    int maxPrice = 250;
    ProductFilterDto filter = ProductFilterDto.builder()
        .minPrice(minPrice)
        .maxPrice(maxPrice)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .map(Product::getPrice)
        .allMatch(price -> (price >= minPrice) && (price <= maxPrice)));
  }
}