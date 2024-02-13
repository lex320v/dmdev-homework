package com.example.entity;

import com.example.entity.enums.RequestStatus;
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

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer carId;
    private Integer clientId;
    private LocalDateTime dateTimeFrom;
    private LocalDateTime dateTimeTo;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private String comment;
    private Instant createdAt;
}
