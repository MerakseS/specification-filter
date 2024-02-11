package com.merakses.productservice.controller;

import com.merakses.productservice.dto.ProductFilterDto;
import com.merakses.productservice.entity.Product;
import com.merakses.productservice.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product create(@RequestBody Product product) {
    return productService.create(product);
  }

  @GetMapping
  public List<Product> getAll() {
    return productService.findAll();
  }

  @GetMapping("/search")
  public List<Product> search(ProductFilterDto filter) {
    return productService.search(filter);
  }
}
