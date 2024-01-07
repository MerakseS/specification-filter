package com.merakses.springsandbox.processor;

import com.merakses.springsandbox.annotation.EntityFilter;
import com.merakses.springsandbox.annotation.Filtered;
import com.merakses.springsandbox.model.FiltrationInfo;
import com.merakses.springsandbox.specification.SpecificationGenerationService;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
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
import org.springframework.util.ReflectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilteredAnnotationBeanPostProcessor implements BeanPostProcessor {

  private final Map<String, FiltrationInfo> filtrationInfoMap = new HashMap<>();

  private final SpecificationGenerationService specificationGenerationService;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    Class<?> beanClass = bean.getClass();
    Arrays.stream(beanClass.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(Filtered.class))
        .map(field -> FiltrationInfo.builder()
            .beanName(beanName)
            .beanClass(beanClass)
            .speciticationField(field)
            .filteredClass(getFilteredClass(field))
            .build())
        .forEach(filtrationInfo -> filtrationInfoMap.put(beanName, filtrationInfo));

    return bean;
  }

  private static Class<?> getFilteredClass(Field field) {
    if (!Specification.class.isAssignableFrom(field.getType())) {
      throw new IllegalArgumentException("Field type must implement Specification");
    }

    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
    return (Class<?>) genericType.getActualTypeArguments()[0];
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    FiltrationInfo filtrationInfo = filtrationInfoMap.get(beanName);
    if (filtrationInfo == null) {
      return bean;
    }

    Class<?> beanClass = filtrationInfo.getBeanClass();
    return Proxy.newProxyInstance(
        beanClass.getClassLoader(),
        beanClass.getInterfaces(),
        getInvocationHandler(bean, filtrationInfo)
    );
  }

  private InvocationHandler getInvocationHandler(Object bean, FiltrationInfo filtrationInfo) {
    return (proxy, method, args) -> {
      Object parameter = findFilter(args, filtrationInfo.getFilteredClass());
      if (parameter == null) {
        return method.invoke(bean, args);
      }

      Specification<?> specification = specificationGenerationService.generate(parameter);

      Field speciticationField = filtrationInfo.getSpeciticationField();
      speciticationField.setAccessible(true);
      ReflectionUtils.setField(speciticationField, bean, specification);

      return method.invoke(bean, args);
    };
  }

  private static Object findFilter(Object[] args, Class<?> filteredClass) {
    if (args == null) {
      return null;
    }

    for (Object parameter : args) {
      Class<?> parameterType = parameter.getClass();
      if (parameterType.isAnnotationPresent(EntityFilter.class)) {
        EntityFilter entityFilterAnnotation = parameterType.getAnnotation(EntityFilter.class);
        if (entityFilterAnnotation.value() == filteredClass) {
          return parameter;
        }
      }
    }

    return null;
  }
}
