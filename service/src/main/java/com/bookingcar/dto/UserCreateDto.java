package com.bookingcar.dto;

import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.Role;
import com.bookingcar.entity.enums.UserStatus;

public record UserCreateDto(
        String username,
        String password,
        String firstname,
        String lastname,
        Role role,
        Gender gender,
        UserStatus status
) {
}
