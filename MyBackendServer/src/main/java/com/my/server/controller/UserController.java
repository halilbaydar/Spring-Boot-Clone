package com.my.server.controller;


import my.spring.boot.annotation.MyAutowired;
import my.spring.boot.annotation.MyGetMapping;
import my.spring.boot.annotation.MyRestController;
import com.my.server.model.resource.UserResource;
import com.my.server.service.UserService;

import java.util.List;

@MyRestController
public class UserController {

    @MyAutowired
    private UserService userService;

    @MyGetMapping("/users")
    public List<UserResource> getUsers() {
        return userService.getUsers();
    }
}
