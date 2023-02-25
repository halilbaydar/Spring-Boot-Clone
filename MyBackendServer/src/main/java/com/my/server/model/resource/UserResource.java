package com.my.server.model.resource;

public class UserResource {
    private String name;

    @Override
    public String toString() {
        return "{ \"name\" : \"" + name + "\" }";
    }
}
