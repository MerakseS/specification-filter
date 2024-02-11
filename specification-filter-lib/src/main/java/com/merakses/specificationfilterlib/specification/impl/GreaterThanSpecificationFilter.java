package com.merakses.specificationfilterlib.specification.impl;

import com.merakses.specificationfilterlib.annotation.impl.GreaterThan;
import com.merakses.specificationfilterlib.specification.SpecificationFilter;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GreaterThanSpecificationFilter
    implements SpecificationFilter<GreaterThan, Comparable> {

  @Override
  public <S> Specification<S> generate(GreaterThan annotation, Comparable value) {
    String fieldName = annotation.value();
    if (StringUtils.isBlank(fieldName)) {
      return null;
    }

    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), value);
  }
}
