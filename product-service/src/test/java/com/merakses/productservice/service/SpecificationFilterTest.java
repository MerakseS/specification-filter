package com.merakses.productservice.service;

import com.merakses.productservice.dto.ProductFilterDto;
import com.merakses.productservice.entity.Product;
import com.merakses.productservice.entity.ProductType;
import com.merakses.productservice.repository.ProductRepository;
import java.time.LocalDate;
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

  private static final LocalDate NOW = LocalDate.now();

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductService productService;

  private final List<Product> products = List.of(
      new Product().setName("Earphones")
          .setPrice(100)
          .setProductType(ProductType.EARPHONES)
          .setLaunchDate(NOW)
          .setAvailable(true),
      new Product()
          .setName("Smartphone")
          .setPrice(200)
          .setProductType(ProductType.PHONE)
          .setLaunchDate(NOW.plusYears(1))
          .setAvailable(true),
      new Product()
          .setName("Laptop")
          .setPrice(300)
          .setProductType(ProductType.COMPUTER)
          .setLaunchDate(NOW.minusYears(1))
          .setAvailable(false)
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
  public void nullFilter() {
    List<Product> actual = productService.search(null);
    System.out.println(actual);
    Assertions.assertEquals(products, actual);
  }

  @Test
  public void emptyFilter() {
    List<Product> actual = productService.search(new ProductFilterDto());
    Assertions.assertEquals(products, actual);
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

  @Test
  public void launchDateIsAfter() {
    ProductFilterDto filter = ProductFilterDto.builder()
        .launchDateFrom(NOW)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .map(Product::getLaunchDate)
        .noneMatch(launchDate -> launchDate.isBefore(NOW)));
  }

  @Test
  public void like() {
    String searchName = "phone";
    ProductFilterDto filter = ProductFilterDto.builder()
        .name(searchName)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .map(Product::getName)
        .allMatch(name -> name.contains(searchName)));
  }

  @Test
  public void equalsAvailable() {
    ProductFilterDto filter = ProductFilterDto.builder()
        .isAvailable(true)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .allMatch(Product::isAvailable));
  }

  @Test
  public void equalsIterable() {
    List<ProductType> productTypes = List.of(ProductType.PHONE, ProductType.COMPUTER);
    ProductFilterDto filter = ProductFilterDto.builder()
        .productTypes(productTypes)
        .build();

    List<Product> actual = productService.search(filter);

    Assertions.assertTrue(actual.stream()
        .map(Product::getProductType)
        .allMatch(productTypes::contains));
  }
}