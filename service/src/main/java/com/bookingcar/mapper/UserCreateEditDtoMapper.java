package com.bookingcar.mapper;

import com.bookingcar.dto.UserCreateEditDto;
import com.bookingcar.entity.User;
import com.bookingcar.entity.enums.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserCreateEditDtoMapper implements Mapper<UserCreateEditDto, User> {

    @Override
    public User map(UserCreateEditDto object) {
        var user = new User();
        copy(object, user);

        return user;
    }

    @Override
    public User map(UserCreateEditDto fromObject, User toObject) {
        copy(fromObject, toObject);

        return toObject;
    }

    private void copy(UserCreateEditDto object, User user) {
        user.setUsername(object.getUsername());
        user.setFirstname(object.getFirstname());
        user.setLastname(object.getLastname());
        user.setRole(object.getRole());
        user.setGender(object.getGender());
        user.setStatus(UserStatus.ACTIVE);
        user.setDateOfBirth(object.getBirthDate());

        if (object.getPassword() != null) {
            user.setPassword(object.getPassword());
        }
    }
}
