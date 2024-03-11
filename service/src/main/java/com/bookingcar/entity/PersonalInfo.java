package com.bookingcar.entity;

import com.bookingcar.entity.enums.DriverLicenseCategories;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
@Entity
public class PersonalInfo implements IdentifiableEntity<Long> {

    @Id
    @Column(name = "user_id")
    private Long id;

    private LocalDate dateOfBirth;

    private String driverLicenseSurname;

    private String driverLicenseName;

    private String driverLicensePlaceOfBirth;

    private LocalDate driverLicenseDateOfIssue;

    private LocalDate driverLicenseDateOfExpire;

    private String driverLicenseIssuedBy;

    private String driverLicenseCode;

    private String driverLicenseResidence;

    @Enumerated(EnumType.STRING)
    private List<DriverLicenseCategories> driverLicenseCategories;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    public void setUser(User user) {
        user.setPersonalInfo(this);
        this.user = user;
        this.id = user.getId();
    }
}
