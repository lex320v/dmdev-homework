package com.bookingcar.dto;

import java.time.LocalDate;

public record PersonalInfoReadDto(Long id,
                                  LocalDate dateOfBirth) {
}
