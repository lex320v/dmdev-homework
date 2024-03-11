package com.bookingcar.mapper;

import com.bookingcar.dto.UserReadDto;
import com.bookingcar.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final PersonalInfoReadMapper personalInfoReadMapper;

    @Override
    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getFirstname(),
                object.getLastname(),
                object.getRole(),
                object.getGender(),
                object.getStatus(),
                Optional.ofNullable(object.getPersonalInfo())
                        .map(personalInfoReadMapper::mapFrom)
                        .orElse(null)
        );
    }
}
