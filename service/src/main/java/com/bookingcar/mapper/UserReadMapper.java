package com.bookingcar.mapper;

import com.bookingcar.dto.UserReadDto;
import com.bookingcar.entity.User;
import com.bookingcar.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final PersonalInfoReadMapper personalInfoReadMapper;
    private final PersonalInfoRepository personalInfoRepository;

    @Override
    public UserReadDto map(User object) {
        var personalInfo = personalInfoRepository.findById(object.getId())
                .map(personalInfoReadMapper::map)
                .orElse(null);

        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getFirstname(),
                object.getLastname(),
                object.getRole(),
                object.getGender(),
                personalInfo
        );
    }
}
