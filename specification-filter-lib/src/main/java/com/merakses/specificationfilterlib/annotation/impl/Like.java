package com.merakses.specificationfilterlib.annotation.impl;

import com.merakses.specificationfilterlib.annotation.FilterCondition;
import com.merakses.specificationfilterlib.specification.impl.LikeSpecificationFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@FilterCondition(filteredBy = LikeSpecificationFilter.class)
public @interface Like {

  String value();

  String prefix() default "";

  String postfix() default "";
}
