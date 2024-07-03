package com.work.productList.crud.repository;

import com.work.productList.crud.domain.OrderItem;
import com.work.productList.crud.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    List<OrderItem> findByIsInCart(Boolean isInCart);
    List<OrderItem> findByIsInCartAndProduct_Id(Boolean isInCart, String productId);
    OrderItem findFirstByIsInCartAndProduct_Id(Boolean isInCart, String productId);
}
