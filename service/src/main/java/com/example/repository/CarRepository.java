package com.example.repository;

import com.example.entity.Car;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CarRepository extends BaseRepository<Long, Car> {

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }
}
