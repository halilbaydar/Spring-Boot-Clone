package com.my.server.service;

import my.spring.boot.annotation.MyService;
import com.my.server.mapper.ProductMapper;
import com.my.server.model.entity.ProductEntity;
import com.my.server.model.resource.ProductResource;

import java.util.List;
import java.util.stream.Collectors;

import static my.spring.boot.stream.StreamWrapper.mapWrapper;


@MyService
public class ProductService {
    private final List<ProductEntity> products = List.of(new ProductEntity(1L, "p1", 1D));

    public List<ProductResource> getProducts() {
        return products.stream()
                .map(mapWrapper(ProductMapper::toResource))
                .collect(Collectors.toList());
    }
}
