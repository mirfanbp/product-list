package com.work.productList.crud.service;

import ch.qos.logback.core.util.StringUtil;
import com.work.productList.crud.domain.Product;
import com.work.productList.crud.insfrastructure.IDGen;
import com.work.productList.crud.model.ProductRequest;
import com.work.productList.crud.model.ProductResponse;
import com.work.productList.crud.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ValidationService validationService;

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("name")));
        return productRepository.findAll(pageable);
    }



    @Override
    @Transactional(readOnly = true)
    public ProductResponse get(String id) {
        Product contact = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toProductResponse(contact);
    }

    @Override
    @Transactional
    public ProductResponse addProduct(ProductRequest request) {
        log.info("## add product ## request: {}", request);
        validationService.validate(request);

        if (productRepository.findFirstByName(request.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product is already exists");
        }

        Product product = new Product();
        product.setId(IDGen.generate());
        product.setName(request.getName());
        product.setType(request.getType());
        product.setPrice(request.getPrice());

        log.info("## success add product: ## new product: {}", product );
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(ProductRequest request) {
        log.info("## update product ## request: {}", request);
        validationService.validate(request);

        if (StringUtil.isNullOrEmpty(request.getId())
                ||StringUtil.isNullOrEmpty(request.getName())
                || StringUtil.isNullOrEmpty(request.getType())
                || request.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        product.setName(request.getName());
        product.setType(request.getType());
        product.setPrice(request.getPrice());

        productRepository.save(product);
        log.info("## success update product: ## new product: {}", product);
        return toProductResponse(product);
    }


    @Override
    @Transactional
    public void deleteProduct(String productId) {
        if (StringUtil.isNullOrEmpty(productId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product ID cannot be null");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .type(product.getType())
                .price(product.getPrice())
                .build();
    }

}
