package com.example.dto;

import lombok.Value;

@Value
public class CarDto {
    Long id;
    String manufacturer;
    String model;
    Double rating;
    Integer sum;
}
