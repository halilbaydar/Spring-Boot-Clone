package com.my.server.controller;


import myspring.annotation.MyAutowired;
import myspring.annotation.MyGetMapping;
import myspring.annotation.MyRestController;
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
