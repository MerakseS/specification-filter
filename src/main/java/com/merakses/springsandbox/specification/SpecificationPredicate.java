package com.merakses.springsandbox.specification;

import java.lang.annotation.Annotation;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationPredicate {

  <T> Specification<T> generate(String fieldName, Object value);

  Class<? extends Annotation> getAnnotation();
}
