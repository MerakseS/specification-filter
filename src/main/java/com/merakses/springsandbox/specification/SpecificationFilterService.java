package com.merakses.springsandbox.specification;

import static com.merakses.springsandbox.util.ReflectionUtils.getFieldValue;

import com.merakses.springsandbox.specification.annotation.FilterCondition;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SpecificationFilterService {

  private final Map<Class<?>, SpecificationFilter> specificationFilters;

  public SpecificationFilterService(List<SpecificationFilter<?, ?>> predicates) {
    specificationFilters = predicates.stream()
        .collect(Collectors.toMap(Object::getClass, Function.identity()));
  }

  public <T> Specification<T> create(Object filter) {
    if (filter == null) {
      return null;
    }

    List<Specification<T>> specificationList = new ArrayList<>();
    for (Field field : filter.getClass().getDeclaredFields()) {
      Object fieldValue = getFieldValue(filter, field);
      for (Annotation annotation : field.getAnnotations()) {
        handleAnnotation(fieldValue, annotation, specificationList);
      }
    }

    return Specification.allOf(specificationList);
  }

  private <T> void handleAnnotation(
      Object fieldValue,
      Annotation annotation,
      List<Specification<T>> specificationList
  ) {
    var filterCondition = AnnotationUtils.getAnnotation(annotation, FilterCondition.class);
    if (filterCondition == null) {
      return;
    }

    for (var filterClass : filterCondition.filteredBy()) {
      if (specificationFilters.containsKey(filterClass)) {
        var specificationFilter = specificationFilters.get(filterClass);
        Specification<T> specification = specificationFilter.generate(annotation, fieldValue);
        specificationList.add(specification);
      }
    }
  }
}
