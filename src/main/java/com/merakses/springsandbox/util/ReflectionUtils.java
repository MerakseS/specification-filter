package com.merakses.springsandbox.util;

import com.merakses.springsandbox.specification.SpecificationFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class ReflectionUtils {

  public static Class<?> getSpecificationTypeParameter(Field field) {
    if (!Specification.class.isAssignableFrom(field.getType())) {
      throw new IllegalArgumentException("Field type must implement Specification");
    }

    var genericType = (ParameterizedType) field.getGenericType();
    return (Class<?>) genericType.getActualTypeArguments()[0];
  }

  public Class<? extends Annotation> getAnnotationTypeParameter(SpecificationFilter target) {
    for (Type type : target.getClass().getGenericInterfaces()) {
      if (type instanceof ParameterizedType parameterizedType &&
          parameterizedType.getRawType() == SpecificationFilter.class) {
        return (Class<? extends Annotation>) parameterizedType.getActualTypeArguments()[0];
      }
    }

    return null;
  }

  public static Object getFieldValue(Object target, Field field) {
    org.springframework.util.ReflectionUtils.makeAccessible(field);
    return org.springframework.util.ReflectionUtils.getField(field, target);
  }

  public static void setFieldValue(Field field, Object target, Object value) {
    field.setAccessible(true);
    org.springframework.util.ReflectionUtils.setField(field, target, value);
  }
}
