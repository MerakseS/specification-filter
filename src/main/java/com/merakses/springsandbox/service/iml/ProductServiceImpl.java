package com.merakses.springsandbox.service.iml;

import com.merakses.springsandbox.annotation.Filtered;
import com.merakses.springsandbox.dto.ProductFilterDto;
import com.merakses.springsandbox.entity.Product;
import com.merakses.springsandbox.repository.ProductRepository;
import com.merakses.springsandbox.service.ProductService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Filtered
  private Specification<Product> searchSpec;

  @Override
  public List<Product> findAll() {
    log.info("Finding all products");
    return productRepository.findAll();
  }

  @Override
  @Filtered
  public List<Product> search(ProductFilterDto filter) {
    return productRepository.findAll(searchSpec);
  }
}
