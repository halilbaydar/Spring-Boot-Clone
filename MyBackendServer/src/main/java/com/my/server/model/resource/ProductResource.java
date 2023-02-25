package com.my.server.model.resource;

public class ProductResource {
    private Long id;
    private String name;
    private Double price;

    @Override
    public String toString() {
        return "{ \"id\" : \"" + id +
                "\", \"name\" :  \"" + name +
                "\", \"price\" :  \"" + price +
                "\" }";
    }
}
