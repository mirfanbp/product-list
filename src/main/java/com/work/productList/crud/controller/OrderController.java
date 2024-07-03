package com.work.productList.crud.controller;

import com.work.productList.crud.model.*;
import com.work.productList.crud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    // CART
    @PostMapping(
            path = "/cart",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderItemResponse> addOrderCartItem(@RequestBody OrderCartItemRequest request) {
        OrderItemResponse orderResponse = orderService.addOrderCartItem(request);
        return WebResponse.<OrderItemResponse>builder().data(orderResponse).build();
    }

    @GetMapping(
            path = "/cart/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderCartResponse> getAllCarts() {
        OrderCartResponse order = orderService.getAllCarts();
        return WebResponse.<OrderCartResponse>builder().data(order).build();
    }


    // ORDER
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createOrder(request);
        return WebResponse.<OrderResponse>builder().data(orderResponse).build();
    }

    @GetMapping(
            path = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<OrderResponse>> getAllProducts() {
        List<OrderResponse> order = orderService.getAllOrders();
        return WebResponse.<List<OrderResponse>>builder().data(order).build();
    }

    @GetMapping(
            path = "/{orderId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<OrderResponse> getOrder(@PathVariable("orderId") String orderId) {
        OrderResponse orderResponse = orderService.getOrder(orderId);
        return WebResponse.<OrderResponse>builder().data(orderResponse).build();
    }
}
