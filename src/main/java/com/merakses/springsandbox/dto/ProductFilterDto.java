package com.merakses.springsandbox.dto;

import com.merakses.springsandbox.annotation.EntityFilter;
import com.merakses.springsandbox.entity.Product;
import com.merakses.springsandbox.entity.ProductType;
import com.merakses.springsandbox.specification.annotation.Equals;
import com.merakses.springsandbox.specification.annotation.GreaterThan;
import com.merakses.springsandbox.specification.annotation.LesserThan;
import com.merakses.springsandbox.specification.annotation.Like;
import java.util.List;
import lombok.Data;

@Data
@EntityFilter(Product.class)
public class ProductFilterDto {

  @Like
  private String name;

  @GreaterThan
  private int minPrice;

  @LesserThan
  private int maxPrice;

  @Equals
  private List<ProductType> productTypes;
}
