package com.merakses.productservice.service;

import com.merakses.productservice.dto.ProductFilterDto;
import com.merakses.productservice.entity.Product;
import java.util.List;

public interface ProductService {

  List<Product> findAll();

  List<Product> search(ProductFilterDto filter);
}
