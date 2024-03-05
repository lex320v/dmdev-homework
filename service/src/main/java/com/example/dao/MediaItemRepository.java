package com.example.dao;

import com.example.entity.MediaItem;
import jakarta.persistence.EntityManager;

public class MediaItemRepository extends BaseRepository<Long, MediaItem> {

    public MediaItemRepository(EntityManager entityManager) {
        super(MediaItem.class, entityManager);
    }
}
