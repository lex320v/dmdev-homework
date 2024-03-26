package com.bookingcar.mapper;

import com.bookingcar.dto.UserReadDto;
import com.bookingcar.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final PersonalInfoReadMapper personalInfoReadMapper;

    @Override
    public UserReadDto map(User object) {
        // todo: personal info null
//        var personalInfo = Optional.ofNullable(object.getPersonalInfo())
//                .map(personalInfoReadMapper::map)
//                .orElse(null);

        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getFirstname(),
                object.getLastname(),
                object.getRole(),
                object.getGender(),
                null
        );
    }
}
