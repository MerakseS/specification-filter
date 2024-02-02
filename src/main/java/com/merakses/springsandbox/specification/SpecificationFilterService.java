package com.merakses.springsandbox.specification;

import static com.merakses.springsandbox.util.ReflectionUtils.getFieldValue;

import com.merakses.springsandbox.specification.annotation.FilterCondition;
import java.lang.annotation.Annotation;
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

  public SpecificationFilterService(List<SpecificationFilter> predicates) {
    specificationFilters = predicates.stream()
        .collect(Collectors.toMap(Object::getClass, Function.identity()));
  }

  public <T> Specification<T> create(Object filter) {
//    Specification<T> result = Specification.where(null); TODO Why it doesn't work
    List<Specification<T>> specificationList = new ArrayList<>();

    for (var field : filter.getClass().getDeclaredFields()) {
      Object fieldValue = getFieldValue(filter, field);
      for (var annotation : field.getAnnotations()) {
        handleAnnotations(fieldValue, annotation, specificationList);
      }
    }

//    return result;
    return Specification.allOf(specificationList);
  }

  private <T> void handleAnnotations(
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
        Specification<T> specification = handleFilterClass(fieldValue, annotation, filterClass);
        if (specification != null) {
          specificationList.add(specification);
        }
      }
    }
  }

  private <T> Specification<T> handleFilterClass(
      Object fieldValue,
      Annotation annotation,
      Class<? extends SpecificationFilter<?, ?>> filterClass
  ) {
    if (fieldValue == null) {
      return null;
    }

    var specificationFilter = specificationFilters.get(filterClass);
    return specificationFilter.generate(annotation, fieldValue);
  }
}
