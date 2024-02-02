package com.merakses.springsandbox.processor;

import com.merakses.springsandbox.annotation.SpecificationFilter;
import com.merakses.springsandbox.model.FiltrationInfo;
import com.merakses.springsandbox.specification.SpecificationFilterService;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilteredAnnotationBeanPostProcessor implements BeanPostProcessor {

  private final SpecificationFilterService specificationFilterService;

  private final Map<String, FiltrationInfo> filtrationInfoMap = new HashMap<>();

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    Class<?> beanClass = bean.getClass();
    Arrays.stream(beanClass.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(SpecificationFilter.class))
        .map(field -> FiltrationInfo.builder()
            .beanName(beanName)
            .beanClass(beanClass)
            .speciticationField(field)
            .filteredClass(getSpecificationTypeParameter(field))
            .build())
        .forEach(filtrationInfo -> filtrationInfoMap.put(beanName, filtrationInfo));

    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    FiltrationInfo filtrationInfo = filtrationInfoMap.get(beanName);
    if (filtrationInfo == null) {
      return bean;
    }

    return Proxy.newProxyInstance(
        filtrationInfo.getBeanClass().getClassLoader(),
        filtrationInfo.getBeanClass().getInterfaces(),
        new SpecificationFilterDynamicProxy(bean, filtrationInfo,
            specificationFilterService)
    );
  }

  public static Class<?> getSpecificationTypeParameter(Field field) {
    if (!Specification.class.isAssignableFrom(field.getType())) {
      throw new IllegalArgumentException("Field type must implement Specification");
    }

    var genericType = (ParameterizedType) field.getGenericType();
    return (Class<?>) Arrays.stream(genericType.getActualTypeArguments())
        .findFirst()
        .orElse(null);
  }
}
