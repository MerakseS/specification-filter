package com.merakses.springsandbox.specification.impl;

import com.merakses.springsandbox.specification.SpecificationFilter;
import com.merakses.springsandbox.specification.annotation.LesserThan;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class LesserThanSpecificationFilter implements SpecificationFilter<LesserThan, Number> {

  @Override
  public <S> Specification<S> generate(LesserThan annotation, Number value) {
    String fieldName = annotation.value();
    if (value == null || StringUtils.isBlank(fieldName)) {
      return null;
    }

    return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get(fieldName), value);
  }
}
