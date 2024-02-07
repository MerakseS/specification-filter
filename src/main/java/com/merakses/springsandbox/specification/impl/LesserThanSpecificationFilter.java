package com.merakses.springsandbox.specification.impl;

import com.merakses.springsandbox.specification.SpecificationFilter;
import com.merakses.springsandbox.specification.annotation.impl.LesserThan;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class LesserThanSpecificationFilter
    implements SpecificationFilter<LesserThan, Comparable> {

  @Override
  public <S> Specification<S> generate(LesserThan annotation, Comparable value) {
    String fieldName = annotation.value();
    if (StringUtils.isBlank(fieldName)) {
      return null;
    }

    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), value);
  }
}
