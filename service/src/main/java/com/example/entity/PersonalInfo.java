package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "personal_info")
public class PersonalInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer passportSeries;
    private Integer passportNumber;
    private String passportPlaceOfBirth;
    private String passportDepartmentCode;
    private Date passportDateOfIssue;
    private String passportIssuedBy;

    private String driverLicenseSurname;
    private String driverLicenseName;
    private String driverLicenseDateOfBirth;
    private String driverLicensePlaceOfBirth;
    private String driverLicenseDateOfIssue;
    private String driverLicenseDateOfExpire;
    private String driverLicenseIssuedBy;
    private String driverLicenseCode;
    private String driverLicenseResidence;
    private List<String> driverLicenseCategories;
}
