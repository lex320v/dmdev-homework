package com.bookingcar.mapper;

import com.bookingcar.dto.UserCreateDto;
import com.bookingcar.entity.User;

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
