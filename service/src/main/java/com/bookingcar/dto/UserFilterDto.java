package com.bookingcar.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserFilterDto {
    String firstname;
    String lastname;
    String username;
}
