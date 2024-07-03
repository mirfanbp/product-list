package com.work.productList.crud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {
    private String id;
    private String productId;
    private String productName;
    private String productType;
    private BigDecimal productPrice;
    private int quantity;
    private BigDecimal total;
    protected Boolean isInCart;
    private String orderId;
}
