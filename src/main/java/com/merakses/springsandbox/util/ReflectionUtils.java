package com.merakses.springsandbox.util;

import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;

import java.lang.reflect.Field;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ReflectionUtils {

  public static Object getFieldValue(Object target, Field field) {
    makeAccessible(field);
    return getField(field, target);
  }

  public static void setFieldValue(Field field, Object target, Object value) {
    makeAccessible(field);
    setField(field, target, value);
  }
}
