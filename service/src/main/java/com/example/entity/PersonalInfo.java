package com.example.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "personal_info")
public class PersonalInfo {
    private Long userId;

    private Integer passportSeries;
    private Integer passportNumber;
    private String passportPlaceOfBirth;
    private String passportDepartmentCode;
    private LocalDate passportDateOfIssue;
    private String passportIssuedBy;

    private String driverLicenseSurname;
    private String driverLicenseName;
    private LocalDate driverLicenseDateOfBirth;
    private String driverLicensePlaceOfBirth;
    private LocalDate driverLicenseDateOfIssue;
    private LocalDate driverLicenseDateOfExpire;
    private String driverLicenseIssuedBy;
    private String driverLicenseCode;
    private String driverLicenseResidence;
    private List<String> driverLicenseCategories;
}
