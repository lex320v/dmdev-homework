package com.bookingcar.dto;

import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.Role;
import lombok.Value;

import java.time.LocalDate;

@Value
public class UserReadDto {
    Long id;
    String username;
    String firstname;
    String lastname;
    LocalDate birthDate;
    Role role;
    Gender gender;
    PersonalInfoReadDto personalInfoReadDto;
}
