package com.merakses.springsandbox.processor;

import com.merakses.springsandbox.annotation.EntityFilter;
import com.merakses.springsandbox.model.FiltrationInfo;
import com.merakses.springsandbox.specification.SpecificationFilterService;
import com.merakses.springsandbox.util.ReflectionUtils;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class SpecificationFilterDynamicProxy implements InvocationHandler {

  private final Object target;
  private final FiltrationInfo filtrationInfo;

  private final SpecificationFilterService specificationFilterService;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object parameter = findFilter(args, filtrationInfo.getFilteredClass());
    if (parameter == null) {
      return method.invoke(target, args);
    }

    Specification<?> specification = specificationFilterService.create(parameter);
    ReflectionUtils.setFieldValue(filtrationInfo.getSpeciticationField(),
        target, specification);

    return method.invoke(target, args);
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
