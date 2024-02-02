package com.merakses.springsandbox.util;

import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
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

  public static Object getFieldValue(Object target, Field field) {
    makeAccessible(field);
    return getField(field, target);
  }

  public static void setFieldValue(Field field, Object target, Object value) {
    makeAccessible(field);
    setField(field, target, value);
  }
}
