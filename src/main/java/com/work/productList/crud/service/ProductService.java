package com.work.productList.crud.service;

import com.work.productList.crud.domain.Product;
import com.work.productList.crud.model.ProductRequest;
import com.work.productList.crud.model.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {


    Page<Product> getAllProducts(int page, int size);

    ProductResponse get(String id);

    ProductResponse addProduct(ProductRequest request);

    ProductResponse updateProduct(ProductRequest request);

    void deleteProduct(String productId);
}
