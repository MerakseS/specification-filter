package com.merakses.specificationfilterlib.service;

import static com.merakses.specificationfilterlib.util.ReflectionUtils.getFieldValue;

import com.merakses.specificationfilterlib.annotation.FilterCondition;
import com.merakses.specificationfilterlib.specification.SpecificationFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
      handleField(filter, field, specificationList);
    }

    return Specification.allOf(specificationList);
  }

  private <T> void handleField(
      Object filter,
      Field field,
      List<Specification<T>> specificationList
  ) {
    Object fieldValue = getFieldValue(filter, field);
    if (fieldValue == null) {
      return;
    }

    for (Annotation annotation : field.getAnnotations()) {
      handleAnnotation(fieldValue, annotation, specificationList);
    }
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

    Arrays.stream(filterCondition.filteredBy())
        .filter(specificationFilters::containsKey)
        .map(specificationFilters::get)
        .map(it -> it.generate(annotation, fieldValue))
        .forEach(specificationList::add);
  }
}
