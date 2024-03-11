package com.bookingcar.repository;

import com.bookingcar.entity.Feedback;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackRepository extends BaseRepository<Long, Feedback> {

    public FeedbackRepository(EntityManager entityManager) {
        super(Feedback.class, entityManager);
    }
}
