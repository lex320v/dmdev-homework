package com.bookingcar.dto;

import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.Role;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;

@Value
@FieldNameConstants
public class UserCreateEditDto {
    String username;
    String password;
    String firstname;
    String lastname;
    Role role;
    Gender gender;
    LocalDate birthDate;
}
