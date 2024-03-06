package com.example.dto;

import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;

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
