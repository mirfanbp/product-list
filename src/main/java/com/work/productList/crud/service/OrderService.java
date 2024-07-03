package com.work.productList.crud.service;

import com.work.productList.crud.model.*;

import java.util.List;

public interface OrderService {

    OrderItemResponse addOrderCartItem(OrderCartItemRequest request);

    OrderCartResponse getAllCarts();
    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrder(String orderId);

    List<OrderResponse> getAllOrders();
}
