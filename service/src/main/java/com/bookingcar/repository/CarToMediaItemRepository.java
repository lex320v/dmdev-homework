package com.bookingcar.repository;

import com.bookingcar.entity.CarToMediaItem;
import com.bookingcar.entity.CarToMediaItemId;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CarToMediaItemRepository extends BaseRepository<CarToMediaItemId, CarToMediaItem> {

    public CarToMediaItemRepository(EntityManager entityManager) {
        super(CarToMediaItem.class, entityManager);
    }
}
