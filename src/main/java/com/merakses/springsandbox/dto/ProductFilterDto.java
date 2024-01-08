package com.merakses.springsandbox.dto;

import com.merakses.springsandbox.annotation.EntityFilter;
import com.merakses.springsandbox.entity.Product;
import com.merakses.springsandbox.entity.ProductType;
import com.merakses.springsandbox.specification.annotation.Equals;
import com.merakses.springsandbox.specification.annotation.GreaterThan;
import com.merakses.springsandbox.specification.annotation.LesserThan;
import com.merakses.springsandbox.specification.annotation.Like;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@EntityFilter(Product.class)
public class ProductFilterDto {

  @Like
  private String name;

  @GreaterThan(Product.Fields.price)
  private Integer minPrice;

  @LesserThan(Product.Fields.price)
  private Integer maxPrice;

  @Equals
  private List<ProductType> productTypes;
}
