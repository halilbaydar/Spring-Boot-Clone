package com.my.server.model.entity;

public class ProductEntity {
    private final Long id;
    private final String name;
    private final Double price;

    public ProductEntity(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "{ \"id\" : \"" + id +
                "\", \"price\" :  \"" + price +
                "\", \"name\" :  \"" + name +
                "\" }";
    }
}
