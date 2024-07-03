package com.work.productList.crud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(length = 100, name = "customer_name", nullable = false)
    private String customerName;

    @Column(length = 100, name = "customer_address", nullable = false)
    private String customerAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Column(scale = 2, precision = 10)
    private BigDecimal total;

}
