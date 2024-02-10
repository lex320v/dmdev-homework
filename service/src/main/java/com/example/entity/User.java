package com.example.entity;

import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private Long balance;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Gender gender;
    private UserStatus status;
    private LocalDateTime createdAt;
}
