package com.merakses.springsandbox.dto;

import com.merakses.springsandbox.annotation.EntityFilter;
import com.merakses.springsandbox.entity.Product;
import com.merakses.springsandbox.entity.ProductType;
import com.merakses.springsandbox.specification.annotation.impl.Equal;
import com.merakses.springsandbox.specification.annotation.impl.GreaterThan;
import com.merakses.springsandbox.specification.annotation.impl.LesserThan;
import com.merakses.springsandbox.specification.annotation.impl.Like;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityFilter(Product.class)
public class ProductFilterDto {

  @Like(value = Product.Fields.name, prefix = "%", postfix = "%")
  private String name;

  @GreaterThan(Product.Fields.price)
  private Integer minPrice;

  @LesserThan(Product.Fields.price)
  private Integer maxPrice;

  @Equal(Product.Fields.productType)
  private List<ProductType> productTypes;

  @GreaterThan(Product.Fields.launchDate)
  private LocalDate launchDateFrom;

  @Equal(Product.Fields.isAvailable)
  private Boolean isAvailable;
}
