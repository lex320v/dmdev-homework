package com.bookingcar.repository;

import com.bookingcar.entity.CarToMediaItem;
import com.bookingcar.entity.CarToMediaItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarToMediaItemRepository extends JpaRepository<CarToMediaItem, CarToMediaItemId> {

}
