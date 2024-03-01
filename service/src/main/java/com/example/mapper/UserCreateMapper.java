package com.example.mapper;

import com.example.dto.UserCreateDto;
import com.example.entity.User;

public class UserCreateMapper implements Mapper<UserCreateDto, User> {
    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .username(object.username())
                .firstname(object.firstname())
                .password(object.password())
                .lastname(object.lastname())
                .role(object.role())
                .status(object.status())
                .gender(object.gender())
                .build();
    }
}
