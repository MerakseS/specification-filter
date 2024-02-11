package com.merakses.specificationfilterlib.specification.impl;

import com.merakses.specificationfilterlib.annotation.impl.Equal;
import com.merakses.specificationfilterlib.specification.SpecificationFilter;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import java.util.stream.StreamSupport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class EqualSpecificationFilter implements SpecificationFilter<Equal, Object> {

  @Override
  public <S> Specification<S> generate(Equal annotation, Object value) {
    String fieldName = annotation.value();
    if (StringUtils.isBlank(fieldName)) {
      return null;
    }

    if (value instanceof Iterable<?> iterable) {
      return handleIterable(iterable, fieldName);
    }

    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(fieldName), value);
  }

  private static <S> Specification<S> handleIterable(Iterable<?> iterable, String fieldName) {
    return (root, query, criteriaBuilder) -> {
      Predicate[] predicates = StreamSupport.stream(iterable.spliterator(), false)
          .map(it -> criteriaBuilder.equal(root.get(fieldName), it))
          .toArray(Predicate[]::new);

      return criteriaBuilder.or(predicates);
    };
  }
}
