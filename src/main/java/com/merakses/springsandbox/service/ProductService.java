package com.merakses.springsandbox.service;

import com.merakses.springsandbox.dto.ProductFilterDto;
import com.merakses.springsandbox.entity.Product;
import java.util.List;

public interface ProductService {

  List<Product> findAll();

  List<Product> search(ProductFilterDto filter);
}
