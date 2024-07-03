package com.work.productList.crud.controller;

import com.work.productList.crud.domain.Product;
import com.work.productList.crud.model.PagingResponse;
import com.work.productList.crud.model.ProductRequest;
import com.work.productList.crud.model.ProductResponse;
import com.work.productList.crud.model.WebResponse;
import com.work.productList.crud.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> add(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.addProduct(request);
        return WebResponse.<ProductResponse>builder().data(productResponse).build();
    }

    @GetMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> get(@PathVariable("productId") String productId) {
        ProductResponse productResponse = productService.get(productId);
        return WebResponse.<ProductResponse>builder().data(productResponse).build();
    }

    @GetMapping(
            path = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<Product>> getAllProducts(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "2") Integer size
    ) {
        Page<Product> productPage = productService.getAllProducts(page, size);
        return WebResponse.<List<Product>>builder()
                .data(productPage.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productPage.getNumber())
                        .totalPage(productPage.getTotalPages())
                        .size(productPage.getSize())
                        .build())
                .build();
    }

    @PatchMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<ProductResponse> update(@PathVariable("productId") String productId,
                                               @RequestBody ProductRequest request){

        request.setId(productId);
        ProductResponse productResponse = productService.updateProduct(request);
        return WebResponse.<ProductResponse>builder()
                .data(productResponse)
                .build();
    }

    @DeleteMapping(
            path = "/{productId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(@PathVariable("productId") String productId) {
        productService.deleteProduct(productId);
        return WebResponse.<String>builder().data("OK").build();
    }
}
