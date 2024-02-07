package com.merakses.springsandbox.specification.annotation.impl;

import com.merakses.springsandbox.specification.annotation.FilterCondition;
import com.merakses.springsandbox.specification.impl.EqualSpecificationFilter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@FilterCondition(filteredBy = EqualSpecificationFilter.class)
public @interface Equal {

  String value();
}
