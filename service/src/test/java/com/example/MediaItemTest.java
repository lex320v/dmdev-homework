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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MediaItemTest {

    SessionFactory sessionFactory;
    Session openedSession;

    @BeforeEach
    void prepare() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        openedSession = sessionFactory.openSession();
    }

    @Test
    void createMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        sessionFactory.inTransaction(session -> {
            session.persist(user);
        });

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void readMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        sessionFactory.inTransaction(session -> {
            session.persist(user);
        });

        var mediaItemFromDb = openedSession.get(MediaItem.class, mediaItem.getId());

        assertThat(mediaItemFromDb).isNotNull();
    }

    @Test
    void deleteMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            user.getMediaItems().remove(mediaItem);
            session.remove(mediaItem);
        });

        var mediaItemFromDb = openedSession.get(MediaItem.class, mediaItem.getId());

        assertThat(mediaItemFromDb).isNull();
    }

    @AfterEach
    void closeConnection() {
        openedSession.close();
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
