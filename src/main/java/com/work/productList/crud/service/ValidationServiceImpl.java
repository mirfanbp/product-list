package com.work.productList.crud.service;

import ch.qos.logback.core.util.StringUtil;
import com.work.productList.crud.model.OrderItemRequest;
import com.work.productList.crud.model.OrderRequest;
import com.work.productList.crud.model.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Autowired
    private Validator validator;

    public void validate(Object request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

//    public void validate(ProductRequest request) {
//        if (request == null ||
//                StringUtil.isNullOrEmpty(request.getName()) ||
//                StringUtil.isNullOrEmpty(request.getType()) ||
//                request.getPrice() == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid product request");
//        }
//    }
//
//    public void validate(OrderRequest request) {
//        if (request == null ||
//                StringUtil.isNullOrEmpty(request.getCustomerName()) ||
//                StringUtil.isNullOrEmpty(request.getCustomerAddress()) ||
//                request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order request");
//        }
//
//        for (OrderItemRequest item : request.getOrderItems()) {
//            if (StringUtil.isNullOrEmpty(item.getProductId()) || item.getQuantity() <= 0) {
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order item request");
//            }
//        }
//    }
}
