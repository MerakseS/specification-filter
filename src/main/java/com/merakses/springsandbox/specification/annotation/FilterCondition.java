package com.merakses.springsandbox.specification.annotation;

import com.merakses.springsandbox.specification.SpecificationFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterCondition {

  Class<? extends SpecificationFilter<?, ?>>[] filteredBy() default {};
}
