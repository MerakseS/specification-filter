package com.merakses.springsandbox.specification;

import java.lang.annotation.Annotation;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationPredicate<A extends Annotation, T> {

  <S> Specification<S> generate(T value);

  void initialize(A annotation);
}
