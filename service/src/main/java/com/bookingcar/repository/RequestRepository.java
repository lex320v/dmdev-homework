package com.bookingcar.repository;

import com.bookingcar.entity.Request;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RequestRepository extends BaseRepository<Long, Request> {

    public RequestRepository(EntityManager entityManager) {
        super(Request.class, entityManager);
    }
}
