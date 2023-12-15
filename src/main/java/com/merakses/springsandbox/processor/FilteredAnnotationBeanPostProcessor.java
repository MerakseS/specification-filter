package com.merakses.springsandbox.processor;

import com.merakses.springsandbox.annotation.Filtered;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Component
public class FilteredAnnotationBeanPostProcessor implements BeanPostProcessor {

  private final Map<String, Class<?>> originalClasses = new HashMap<>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    Class<?> beanClass = bean.getClass();
    Arrays.stream(beanClass.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Filtered.class))
        .forEach(field -> originalClasses.put(beanName, beanClass));

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> beanClass = originalClasses.get(beanName);
    if (beanClass != null) {
      return Proxy.newProxyInstance(
          beanClass.getClassLoader(),
          beanClass.getInterfaces(),
          (proxy, method, args) -> {
            Method originalMethod = beanClass.getMethod(method.getName(),
                method.getParameterTypes());

            Arrays.stream(method.getParameters()).filter(parameter -> parameter.getClass() == )

            if (originalMethod.isAnnotationPresent(Filtered.class)) {
              Filtered annotation = originalMethod.getAnnotation(Filtered.class);
              String specFieldName = annotation.value();
              Field specField = beanClass.getDeclaredField(specFieldName);
              specField.setAccessible(true);
              ReflectionUtils.setField(specField, bean, "Hello");
            }

            for (Parameter parameter : method.getParameters()) {
              parameter.getType() == P
            }

            return method.invoke(bean, args);
          });
    }
    return bean;
  }
}
