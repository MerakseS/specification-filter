package com.merakses.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@FieldNameConstants
@Accessors(chain = true)
@Table(name = "products")
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "product_type")
  @Enumerated(EnumType.STRING)
  private ProductType productType;

  @Column(name = "launch_date")
  private LocalDate launchDate;

  @Column(name = "is_available", nullable = false)
  private boolean isAvailable;
}