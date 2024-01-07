package com.merakses.springsandbox.specification;

import java.lang.annotation.Annotation;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationFilter<A extends Annotation, T> {

  <S> Specification<S> generate(A annotation, T value);
}
