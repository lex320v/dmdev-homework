package com.example.repository;

import com.example.entity.MediaItem;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class MediaItemRepository extends BaseRepository<Long, MediaItem> {

    public MediaItemRepository(EntityManager entityManager) {
        super(MediaItem.class, entityManager);
    }
}
