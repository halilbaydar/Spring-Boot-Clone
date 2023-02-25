package com.my.server.mapper;


import com.my.server.model.entity.UserEntity;
import com.my.server.model.resource.UserResource;

public class UserMapper {
    public static UserResource toResource(UserEntity userEntity) {
        return CustomMapper.map(userEntity, UserResource.class);
    }
}
