package com.bookingcar.mapper;

import com.bookingcar.dto.PersonalInfoReadDto;
import com.bookingcar.entity.PersonalInfo;

public class PersonalInfoReadMapper implements Mapper<PersonalInfo, PersonalInfoReadDto>{
    @Override
    public PersonalInfoReadDto mapFrom(PersonalInfo object) {
        return new PersonalInfoReadDto(
                object.getId(),
                object.getDateOfBirth(),
                object.getDriverLicenseSurname(),
                object.getDriverLicenseName(),
                object.getDriverLicensePlaceOfBirth(),
                object.getDriverLicenseDateOfIssue(),
                object.getDriverLicenseDateOfExpire(),
                object.getDriverLicenseIssuedBy(),
                object.getDriverLicenseCode(),
                object.getDriverLicenseResidence(),
                object.getDriverLicenseCategories()
        );
    }
}
