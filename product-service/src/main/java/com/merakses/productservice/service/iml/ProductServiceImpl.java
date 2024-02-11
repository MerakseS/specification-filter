package com.merakses.productservice.service.iml;

import com.merakses.productservice.dto.ProductFilterDto;
import com.merakses.productservice.entity.Product;
import com.merakses.productservice.repository.ProductRepository;
import com.merakses.productservice.service.ProductService;
import com.merakses.specificationfilterlib.annotation.SpecificationFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @SpecificationFilter
  private final Specification<Product> searchSpec = Specification.where(null);

  @Override
  public Product create(Product product) {
    productRepository.save(product);
    log.info("Successfully created product with id {}", product.getId());
    return product;
  }

  @Override
  public List<Product> findAll() {
    log.info("Finding all products");
    return productRepository.findAll();
  }

  @Override
  public List<Product> search(ProductFilterDto filter) {
    log.info("Searching products by filter {}", filter);
    List<Product> products = productRepository.findAll(searchSpec);
    log.info("Found products: {}", products);
    return products;
  }
}
