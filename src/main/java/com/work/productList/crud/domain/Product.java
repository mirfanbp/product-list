package com.work.productList.crud.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String type;

    @Column(scale = 2, precision = 10)
    private BigDecimal price;
}
