package com.example;

import com.example.dao.MediaItemRepository;
import com.example.dao.UserRepository;
import com.example.entity.MediaItem;
import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.MediaItemType;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MediaItemRepositoryIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private UserRepository userRepository;
    private MediaItemRepository mediaItemRepository;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void prepare() {
        session = sessionFactory.getCurrentSession();
        userRepository = new UserRepository(session);
        mediaItemRepository = new MediaItemRepository(session);
        session.beginTransaction();
    }

    @AfterEach
    void closeConnection() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void createAndReadMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        userRepository.save(user);
        mediaItemRepository.save(mediaItem);
        session.evict(mediaItem);

        Optional<MediaItem> mediaItemFromDb = mediaItemRepository.findById(mediaItem.getId());

        assertTrue(mediaItemFromDb.isPresent());
        assertThat(mediaItemFromDb.get()).isEqualTo(mediaItem);
    }

    @Test
    void deleteMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        userRepository.save(user);
        mediaItemRepository.save(mediaItem);
        mediaItemRepository.delete(mediaItem);

        var mediaItemFromDb = mediaItemRepository.findById(mediaItem.getId());

        assertTrue(mediaItemFromDb.isEmpty());
    }

    private MediaItem buildMediaItem() {
        return MediaItem.builder()
                .type(MediaItemType.AVATAR)
                .mimeType("mime_type")
                .link("link")
                .previewLink("preview_link")
                .build();
    }

    private User buildUser() {
        return User.builder()
                .username("username")
                .firstname("firstname")
                .lastname("lastname")
                .password("password")
                .status(UserStatus.ACTIVE)
                .gender(Gender.MALE)
                .role(Role.SUPER_ADMIN)
                .build();
    }
}
