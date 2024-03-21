package com.bookingcar.repository;

import com.bookingcar.dto.UserFilterDto;
import com.bookingcar.entity.User;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilterDto userFilterDto);
}
