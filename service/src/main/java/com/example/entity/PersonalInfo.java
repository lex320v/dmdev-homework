package com.example.entity;

import com.example.entity.enums.DriverLicenseCategories;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Id
    private Long userId;

    private String driverLicenseSurname;
    private String driverLicenseName;
    private LocalDate driverLicenseDateOfBirth;
    private String driverLicensePlaceOfBirth;
    private LocalDate driverLicenseDateOfIssue;
    private LocalDate driverLicenseDateOfExpire;
    private String driverLicenseIssuedBy;
    private String driverLicenseCode;
    private String driverLicenseResidence;
    private List<DriverLicenseCategories> driverLicenseCategories;
}
