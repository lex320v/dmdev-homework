package com.bookingcar.mapper;

import com.bookingcar.dto.PersonalInfoReadDto;
import com.bookingcar.entity.PersonalInfo;
import org.springframework.stereotype.Component;

@Component
public class PersonalInfoReadMapper implements Mapper<PersonalInfo, PersonalInfoReadDto> {
    @Override
    public PersonalInfoReadDto map(PersonalInfo object) {
        return new PersonalInfoReadDto(
                object.getId(),
                object.getDateOfBirth()
        );
    }
}
