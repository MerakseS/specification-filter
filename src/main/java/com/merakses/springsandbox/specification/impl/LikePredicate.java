package com.merakses.springsandbox.specification.impl;

import com.merakses.springsandbox.specification.SpecificationPredicate;
import com.merakses.springsandbox.specification.annotation.Like;
import java.lang.annotation.Annotation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class LikePredicate implements SpecificationPredicate {

  @Override
  public <T> Specification<T> generate(String fieldName, Object value) {
    return (root, query, criteriaBuilder) -> {
      if (root.get(fieldName).getJavaType() != String.class) {
        return null;
      }
      return criteriaBuilder.like(root.get(fieldName), String.format("%%%s%%", value));
    };
  }

  @Override
  public Class<? extends Annotation> getAnnotation() {
    return Like.class;
  }
}
