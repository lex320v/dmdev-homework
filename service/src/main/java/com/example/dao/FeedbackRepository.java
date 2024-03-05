package com.example.dao;

import com.example.entity.Feedback;
import jakarta.persistence.EntityManager;

public class FeedbackRepository extends BaseRepository<Long, Feedback> {

    public FeedbackRepository(EntityManager entityManager) {
        super(Feedback.class, entityManager);
    }
}
