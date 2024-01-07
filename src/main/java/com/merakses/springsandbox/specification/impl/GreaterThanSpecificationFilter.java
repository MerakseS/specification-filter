package com.merakses.springsandbox.specification.impl;

import com.merakses.springsandbox.specification.SpecificationFilter;
import com.merakses.springsandbox.specification.annotation.GreaterThan;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GreaterThanSpecificationFilter implements SpecificationFilter<GreaterThan, Number> {

  @Override
  public <S> Specification<S> generate(GreaterThan annotation, Number value) {
    String fieldName = annotation.value();
    if (value == null || StringUtils.isBlank(fieldName)) {
      return null;
    }

    return (root, query, criteriaBuilder) -> criteriaBuilder.gt(root.get(fieldName), value);
  }
}
