package com.bookingcar.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
