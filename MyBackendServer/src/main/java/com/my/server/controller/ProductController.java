package com.my.server.controller;


import myspring.annotation.MyAutowired;
import myspring.annotation.MyGetMapping;
import myspring.annotation.MyRestController;
import com.my.server.model.resource.ProductResource;
import com.my.server.service.ProductService;

import java.util.List;

@MyRestController
public class ProductController {
    @MyAutowired
    private ProductService productService;

    @MyGetMapping("/products")
    public List<ProductResource> getProducts() {
        return productService.getProducts();
    }
}
