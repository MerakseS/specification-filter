package com.merakses.specificationfilterlib.specification.impl;

import com.merakses.specificationfilterlib.annotation.impl.Like;
import com.merakses.specificationfilterlib.specification.SpecificationFilter;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class LikeSpecificationFilter implements SpecificationFilter<Like, String> {

  private static final String LIKE_FORMAT = "%s%s%s";

  @Override
  public <S> Specification<S> generate(Like annotation, String value) {
    String fieldName = annotation.value();
    if (StringUtils.isBlank(fieldName)) {
      return null;
    }

    String likeValue = String.format(LIKE_FORMAT, annotation.prefix(), value, annotation.postfix());

    return (root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get(fieldName), likeValue);
  }
}
