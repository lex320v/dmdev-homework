package com.example.dao;

import com.example.entity.Request;
import jakarta.persistence.EntityManager;

public class RequestRepository extends BaseRepository<Long, Request> {

    public RequestRepository(EntityManager entityManager) {
        super(Request.class, entityManager);
    }
}
