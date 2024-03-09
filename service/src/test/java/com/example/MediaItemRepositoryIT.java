package com.example;

import com.example.config.ApplicationTestConfiguration;
import com.example.entity.MediaItem;
import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.MediaItemType;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.repository.MediaItemRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MediaItemRepositoryIT {

    private static AnnotationConfigApplicationContext context;
    private static EntityManager entityManager;
    private static UserRepository userRepository;
    private static MediaItemRepository mediaItemRepository;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(ApplicationTestConfiguration.class);
        entityManager = context.getBean(EntityManager.class);
        userRepository = context.getBean(UserRepository.class);
        mediaItemRepository = context.getBean(MediaItemRepository.class);
    }

    @BeforeEach
    void prepare() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeConnection() {
        entityManager.getTransaction().rollback();
    }

    @AfterAll
    static void closeSessionFactory() {
        context.close();
    }

    @Test
    void createAndReadMediaItem() {
        var user = buildUser();
        var mediaItem = buildMediaItem();
        user.addMediaItem(mediaItem);

        userRepository.save(user);
        mediaItemRepository.save(mediaItem);
        entityManager.detach(mediaItem);

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
