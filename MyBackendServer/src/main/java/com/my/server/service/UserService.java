package com.my.server.service;


import my.spring.boot.annotation.MyService;
import com.my.server.mapper.UserMapper;
import com.my.server.model.entity.UserEntity;
import com.my.server.model.resource.UserResource;

import java.util.List;
import java.util.stream.Collectors;

import static my.spring.boot.stream.StreamWrapper.mapWrapper;


@MyService
public class UserService {
    private final List<UserEntity> users = List.of(new UserEntity(1L, "Java"));

    public List<UserResource> getUsers() {
        return users.stream()
                .map(mapWrapper(UserMapper::toResource))
                .collect(Collectors.toList());
    }
}
