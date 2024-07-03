package com.work.productList.crud.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {

    @NotBlank
    @Size(max = 100)
    private String productId;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Positive
    private int quantity;

    @NotBlank
    @Size(max = 100)
    private String orderId;
}
