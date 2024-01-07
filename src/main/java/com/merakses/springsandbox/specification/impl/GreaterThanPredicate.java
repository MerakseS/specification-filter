package com.merakses.springsandbox.specification.impl;

import com.merakses.springsandbox.specification.SpecificationPredicate;
import com.merakses.springsandbox.specification.annotation.GreaterThan;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GreaterThanPredicate implements SpecificationPredicate<GreaterThan, Number> {

  private String fieldName;

  @Override
  public void initialize(GreaterThan annotation) {
    fieldName = annotation.value();
  }

  @Override
  public <S> Specification<S> generate(Number value) {
    if (value == null || StringUtils.isBlank(fieldName)) {
      return null;
    }

    return (root, query, criteriaBuilder) ->
        criteriaBuilder.gt(root.get(fieldName), value);
  }
}
