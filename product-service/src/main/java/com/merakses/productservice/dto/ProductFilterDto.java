package com.merakses.productservice.dto;

import com.merakses.productservice.entity.Product;
import com.merakses.productservice.entity.ProductType;
import com.merakses.specificationfilterlib.annotation.EntityFilter;
import com.merakses.specificationfilterlib.annotation.impl.Equal;
import com.merakses.specificationfilterlib.annotation.impl.GreaterThan;
import com.merakses.specificationfilterlib.annotation.impl.LesserThan;
import com.merakses.specificationfilterlib.annotation.impl.Like;
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
