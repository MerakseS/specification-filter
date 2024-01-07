package com.merakses.springsandbox.specification;

import static com.merakses.springsandbox.util.GenericReflectionUtils.getFieldValue;

import com.merakses.springsandbox.util.GenericReflectionUtils;
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
            GenericReflectionUtils::getAnnotationTypeParameter,
            Function.identity()
        ));
  }

  public <T> Specification<T> create(Object filter) {
//    Specification<T> result = Specification.where(null); TODO Why it doesn't work
    List<Specification<T>> specifications = new ArrayList<>();

    for (Field field : filter.getClass().getDeclaredFields()) {
      for (Annotation annotation : field.getAnnotations()) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (predicateMap.containsKey(annotationType)) {
          Object fieldValue = getFieldValue(filter, field);
          SpecificationFilter specificationFilter = predicateMap.get(annotationType);
          Specification<T> specification = specificationFilter.generate(annotation, fieldValue);

//          result.and(specification);
          if (specification != null) {
            specifications.add(specification);
          }
        }
      }
    }

//    return result;
    return Specification.allOf(specifications);
  }
}
