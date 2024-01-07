package com.merakses.springsandbox.util;

import com.merakses.springsandbox.specification.SpecificationFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

@UtilityClass
public class GenericReflectionUtils {

  public static Class<?> getSpecificationTypeParameter(Field field) {
    if (!Specification.class.isAssignableFrom(field.getType())) {
      throw new IllegalArgumentException("Field type must implement Specification");
    }

    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
    return (Class<?>) genericType.getActualTypeArguments()[0];
  }

  public Class<? extends Annotation> getAnnotationTypeParameter(
      SpecificationFilter specificationFilter
  ) {
    for (Type type : specificationFilter.getClass().getGenericInterfaces()) {
      if (type instanceof ParameterizedType parameterizedType &&
          parameterizedType.getRawType() == SpecificationFilter.class) {
        return (Class<? extends Annotation>) parameterizedType.getActualTypeArguments()[0];
      }
    }

    return null;
  }

  public static Object getFieldValue(Object filter, Field field) {
    ReflectionUtils.makeAccessible(field);
    return ReflectionUtils.getField(field, filter);
  }
}
