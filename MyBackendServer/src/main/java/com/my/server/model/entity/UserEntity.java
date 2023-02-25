package com.my.server.model.entity;

public class UserEntity {
    private final Long id;
    private final String name;

    public UserEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "{ \"id\" : \"" + id +
                "\", \"name\" :  \"" + name + "\" }";
    }
}
