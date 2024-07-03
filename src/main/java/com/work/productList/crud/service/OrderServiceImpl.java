package com.work.productList.crud.service;

import ch.qos.logback.core.util.StringUtil;
import com.work.productList.crud.domain.Order;
import com.work.productList.crud.domain.OrderItem;
import com.work.productList.crud.domain.Product;
import com.work.productList.crud.insfrastructure.IDGen;
import com.work.productList.crud.model.*;
import com.work.productList.crud.repository.OrderItemRepository;
import com.work.productList.crud.repository.OrderRepository;
import com.work.productList.crud.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ValidationService validationService;


    @Override
    @Transactional
    public OrderItemResponse addOrderCartItem(OrderCartItemRequest request) {
        log.info("## add order to cart ## request: {}", request);
        validationService.validate(request);

        if (StringUtil.isNullOrEmpty(request.getProductId()) || request.getQuantity() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order item request");
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        OrderItem existOrderItem = orderItemRepository.findFirstByIsInCartAndProduct_Id(true, product.getId());

        if (existOrderItem != null) {
            existOrderItem.setQuantity(request.getQuantity() + existOrderItem.getQuantity());
            existOrderItem.setTotal(product.getPrice().multiply(BigDecimal.valueOf(existOrderItem.getQuantity())));
            log.info("## success add to cart: ## new cart: {}", existOrderItem);
            orderItemRepository.save(existOrderItem);
            return toOrderCartItemResponse(existOrderItem);
        } else {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(IDGen.generate());
            orderItem.setProduct(product);
            orderItem.setQuantity(request.getQuantity());
            orderItem.setTotal(product.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            orderItem.setIsInCart(true);
            orderItemRepository.save(orderItem);
            log.info("## success add to cart: ## new cart: {}", orderItem);
            return toOrderCartItemResponse(orderItem);
        }
    }

    @Override
    public OrderCartResponse getAllCarts() {
        OrderCartResponse cartResponse = new OrderCartResponse();
        BigDecimal totalAllItem = BigDecimal.ZERO;
        List<OrderItem> orders = orderItemRepository.findByIsInCart(true);

        if (orders.isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart is empty");
        }

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem order : orders) {
            OrderItemResponse orderResponse = toOrderCartItemResponse(order);
            orderItemResponses.add(orderResponse);
            totalAllItem = totalAllItem.add(order.getTotal());
        }

        cartResponse.setOrderItems(orderItemResponses);
        cartResponse.setTotal(totalAllItem);
        return cartResponse;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        log.info("## create order ## request: {}", request);
        validationService.validate(request);

        if (request == null ||
                StringUtil.isNullOrEmpty(request.getCustomerName()) ||
                StringUtil.isNullOrEmpty(request.getCustomerAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order request");
        }

        List<OrderItem> orderItems = orderItemRepository.findByIsInCart(true);

        if (orderItems == null || orderItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No items in the cart to place an order");
        }

        BigDecimal totalAllItem = BigDecimal.ZERO;
        Order order = new Order();
        order.setId(IDGen.generate());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {
            orderItem.setIsInCart(false);
            orderItem.setOrder(order);
            BigDecimal itemTotal = orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            totalAllItem = totalAllItem.add(itemTotal);
            orderItemRepository.save(orderItem);
        }


        order.setOrderItems(orderItems);
        order.setTotal(totalAllItem);
        orderRepository.save(order);

        return toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        if (orders.isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No order found");
        }

        for (Order order : orders) {
            OrderResponse orderResponse = toOrderResponse(order);
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return toOrderResponse(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerAddress(order.getCustomerAddress())
                .orderItems(order.getOrderItems().stream()
                        .map(this::toOrderItemResponse)
                        .collect(Collectors.toList()))
                .total(order.getTotal())
                .build();
    }

    private OrderItemResponse toOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .productType(orderItem.getProduct().getType())
                .productPrice(orderItem.getProduct().getPrice())
                .total(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                .quantity(orderItem.getQuantity())
                .orderId(orderItem.getOrder().getId())
                .build();
    }

    private OrderItemResponse toOrderCartItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .productType(orderItem.getProduct().getType())
                .productPrice(orderItem.getProduct().getPrice())
                .isInCart(orderItem.getIsInCart())
                .total(orderItem.getTotal())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
