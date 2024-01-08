package com.merakses.springsandbox.specification;

import static com.merakses.springsandbox.util.ReflectionUtils.getFieldValue;

import com.merakses.springsandbox.util.ReflectionUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SpecificationFilterService {

  private final Map<Class<? extends Annotation>, SpecificationFilter> predicateMap;

  public SpecificationFilterService(List<SpecificationFilter> predicates) {
    predicateMap = predicates.stream()
        .collect(Collectors.toMap(
            ReflectionUtils::getAnnotationTypeParameter,
            Function.identity()
        ));
  }

  public <T> Specification<T> create(Object filter) {
//    Specification<T> result = Specification.where(null); TODO Why it doesn't work
    List<Specification<T>> specifications = new ArrayList<>();

    for (Field field : filter.getClass().getDeclaredFields()) {
      for (Annotation annotation : field.getAnnotations()) {
        if (predicateMap.containsKey(annotation.annotationType())) {
          handleFilter(getFieldValue(filter, field), annotation, specifications);
        }
      }
    }

//    return result;
    return Specification.allOf(specifications);
  }

  private <T> void handleFilter(
      Object fieldValue,
      Annotation annotation,
      List<Specification<T>> specifications
  ) {
    if (fieldValue == null) {
      return;
    }

    SpecificationFilter specificationFilter = predicateMap.get(annotation.annotationType());
    Specification<T> specification = specificationFilter.generate(annotation, fieldValue);

    if (specification != null) {
//              result.and(specification);
      specifications.add(specification);
    }
  }
}
