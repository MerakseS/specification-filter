package com.merakses.springsandbox.specification;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
public class SpecificationGenerationService {

  private final Map<Class<? extends Annotation>, SpecificationPredicate> predicateMap;

  public SpecificationGenerationService(List<SpecificationPredicate> predicates) {
    predicateMap = predicates.stream()
        .collect(Collectors.toMap(
            SpecificationPredicate::getAnnotation,
            Function.identity()
        ));
  }

  public <T> Specification<T> generate(Object filter) {
    Specification<T> result = Specification.where(null);

    for (Field field : filter.getClass().getFields()) {
      for (Annotation fieldAnnotation : field.getAnnotations()) {
        Class<? extends Annotation> fieldAnnotationClass = fieldAnnotation.getClass();
        if (predicateMap.containsKey(fieldAnnotationClass)) {
          SpecificationPredicate specificationPredicate = predicateMap.get(fieldAnnotationClass);
          Specification<T> predicate = specificationPredicate.generate(field.getName(),
              ReflectionUtils.getField(field, filter));
          result.and(predicate);
        }
      }
    }

    return result;
  }
}
