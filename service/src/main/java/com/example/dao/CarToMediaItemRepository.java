package com.example.dao;

import com.example.entity.CarToMediaItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CarToMediaItemRepository {

    private final EntityManager entityManager;

    public CarToMediaItem save(CarToMediaItem entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    public void delete(CarToMediaItem entity) {
        entityManager.remove(entity);
        entityManager.flush();
    }

    public void update(CarToMediaItem entity) {
        entityManager.merge(entity);
        entityManager.flush();
    }

    public Optional<CarToMediaItem> findByCompositeId(Long carId, Long mediaItemId) {
        CarToMediaItem singleResult = null;
        try {
            singleResult = entityManager.createQuery("""
                        select ctmi from CarToMediaItem ctmi
                        where ctmi.car.id = :carId and ctmi.mediaItem.id = :mediaItemId
                        """, CarToMediaItem.class)
                    .setParameter("carId", carId)
                    .setParameter("mediaItemId", mediaItemId).getSingleResult();
        } catch (NoResultException exception) {
        }

        return Optional.ofNullable(singleResult);
    }
}
