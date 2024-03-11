package com.bookingcar.repository;

import com.bookingcar.entity.Car;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class CarRepository extends BaseRepository<Long, Car> {

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }
}
