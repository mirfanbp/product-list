package com.work.productList.crud.repository;

import com.work.productList.crud.domain.Order;
import com.work.productList.crud.domain.OrderItem;
import com.work.productList.crud.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {


}
