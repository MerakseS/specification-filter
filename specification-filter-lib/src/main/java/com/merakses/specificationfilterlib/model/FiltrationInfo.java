package com.merakses.specificationfilterlib.model;

import java.lang.reflect.Field;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FiltrationInfo {

  private String beanName;

  private Class<?> beanClass;

  private Field speciticationField;

  private Class<?> filteredClass;
}
