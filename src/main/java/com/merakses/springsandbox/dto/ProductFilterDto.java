package com.merakses.springsandbox.dto;

import com.merakses.springsandbox.entity.ProductType;
import java.util.List;
import lombok.Data;

@Data
public class ProductFilterDto {

  private String name;

  private int minPrice;

  private int maxPrice;

  private List<ProductType> productTypes;
}
