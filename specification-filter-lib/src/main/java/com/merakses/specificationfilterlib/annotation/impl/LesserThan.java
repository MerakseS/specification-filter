package com.merakses.specificationfilterlib.annotation.impl;

import com.merakses.specificationfilterlib.annotation.FilterCondition;
import com.merakses.specificationfilterlib.specification.impl.LesserThanSpecificationFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@FilterCondition(filteredBy = LesserThanSpecificationFilter.class)
public @interface LesserThan {

  String value();
}
