package com.bookingcar.dto;

import com.bookingcar.entity.enums.Role;
import lombok.Value;

@Value
public class UserCreateEditDto {
    String username;
    String password;
    String firstname;
    String lastname;
    Role role;
}
