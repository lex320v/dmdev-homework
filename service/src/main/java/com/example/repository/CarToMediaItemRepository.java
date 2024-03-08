package com.example.repository;

import com.example.entity.CarToMediaItem;
import com.example.entity.CarToMediaItemId;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CarToMediaItemRepository extends BaseRepository<CarToMediaItemId, CarToMediaItem> {

    public CarToMediaItemRepository(EntityManager entityManager) {
        super(CarToMediaItem.class, entityManager);
    }
}
