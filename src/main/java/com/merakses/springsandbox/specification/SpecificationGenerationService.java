package com.merakses.springsandbox.specification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
public class SpecificationGenerationService {

  private final Map<Class<? extends Annotation>, SpecificationPredicate<? extends Annotation, ?>> predicateMap;

  public SpecificationGenerationService(List<SpecificationPredicate<?, ?>> predicates) {
    predicateMap = predicates.stream()
        .collect(Collectors.toMap(
            it -> getAnnotationClass(it.getClass()),
            Function.identity()
        ));
  }

  public <T> Specification<T> generate(Object filter) {
//    Specification<T> result = Specification.where(null);
    List<Specification<T>> specifications = new ArrayList<>();

    for (Field field : filter.getClass().getDeclaredFields()) {
      for (Annotation fieldAnnotation : field.getAnnotations()) {
        if (predicateMap.containsKey(fieldAnnotation.annotationType())) {
          Object fieldValue = getFieldValue(filter, field);
          Specification<T> specification = generatePredicate(fieldAnnotation, fieldValue);
//          result.and(specification);
          specifications.add(specification);
        }
      }
    }

//    return result;
    return Specification.allOf(specifications);
  }

  private static Object getFieldValue(Object filter, Field field) {
    ReflectionUtils.makeAccessible(field);
    return ReflectionUtils.getField(field, filter);
  }

  private <T> Specification<T> generatePredicate(Annotation annotation, Object value) {
    SpecificationPredicate specificationPredicate = predicateMap.get(annotation.annotationType());
    specificationPredicate.initialize(annotation);
    return specificationPredicate.generate(value);
  }

  private static Class<? extends Annotation> getAnnotationClass(
      Class<? extends SpecificationPredicate> clazz
  ) {
    for (Type type : clazz.getGenericInterfaces()) {
      if (type instanceof ParameterizedType parameterizedType &&
          parameterizedType.getRawType() == SpecificationPredicate.class) {
        return (Class<? extends Annotation>) parameterizedType.getActualTypeArguments()[0];
      }
    }
    return null;
  }
}
