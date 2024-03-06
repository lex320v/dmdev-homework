package com.example.dto;

import com.example.entity.enums.DriverLicenseCategories;

import java.time.LocalDate;
import java.util.List;

public record PersonalInfoReadDto(Long id,
                                  LocalDate dateOfBirth,
                                  String driverLicenseSurname,
                                  String driverLicenseName,
                                  String driverLicensePlaceOfBirth,
                                  LocalDate driverLicenseDateOfIssue,
                                  LocalDate driverLicenseDateOfExpire,
                                  String driverLicenseIssuedBy,
                                  String driverLicenseCode,
                                  String driverLicenseResidence,
                                  List<DriverLicenseCategories> driverLicenseCategories) {
}
