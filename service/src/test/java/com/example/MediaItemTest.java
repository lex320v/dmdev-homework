package com.example;

import com.example.entity.MediaItem;
import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.MediaItemType;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MediaItemTest {

    static SessionFactory sessionFactory;
    static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @BeforeEach
    public void prepare() {
        session.beginTransaction();
    }

    @Test
    void createMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        session.persist(user);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void readMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        session.persist(user);

        var mediaItemFromDb = session.get(MediaItem.class, mediaItem.getId());

        assertThat(mediaItemFromDb).isNotNull();
    }

    @Test
    void deleteMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        session.persist(user);
        user.getMediaItems().remove(mediaItem);
        session.remove(mediaItem);

        var mediaItemFromDb = session.get(MediaItem.class, mediaItem.getId());

        assertThat(mediaItemFromDb).isNull();
    }

    @AfterEach
    void closeConnection() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void closeSessionFactory() {
        session.close();
        sessionFactory.close();
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
