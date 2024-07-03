package com.work.productList.crud.model;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCartItemRequest {

    @NotBlank
    @Size(max = 100)
    private String productId;

    @NotNull
    @Min(value = 1)
    @Max(value = 100)
    @Positive
    private int quantity;

}
