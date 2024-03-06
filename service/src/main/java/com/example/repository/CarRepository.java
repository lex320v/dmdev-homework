package com.example.repository;

import com.example.entity.Car;
import jakarta.persistence.EntityManager;

public class CarRepository extends BaseRepository<Long, Car> {

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }
}
