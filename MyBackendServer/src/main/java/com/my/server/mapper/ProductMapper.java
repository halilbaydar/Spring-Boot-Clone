package com.my.server.mapper;


import com.my.server.model.entity.ProductEntity;
import com.my.server.model.resource.ProductResource;

public class ProductMapper {
    public static ProductResource toResource(ProductEntity productEntity) {
        return CustomMapper.map(productEntity, ProductResource.class);
    }
}
