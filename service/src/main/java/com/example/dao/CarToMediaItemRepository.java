package com.example.dao;

import com.example.entity.CarToMediaItem;
import com.example.entity.CarToMediaItemId;
import jakarta.persistence.EntityManager;

public class CarToMediaItemRepository extends BaseRepository<CarToMediaItemId, CarToMediaItem> {

    public CarToMediaItemRepository(EntityManager entityManager) {
        super(CarToMediaItem.class, entityManager);
    }
}
