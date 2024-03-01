package com.example.dto;

import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;

public record UserReadDto(Long id,
                          String username,
                          String firstname,
                          String lastname,
                          Role role,
                          Gender gender,
                          UserStatus status,
                          PersonalInfoReadDto personalInfo) {
}
