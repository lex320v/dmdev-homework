package com.bookingcar;

import com.bookingcar.annotation.IT;
import com.bookingcar.entity.MediaItem;
import com.bookingcar.entity.User;
import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.MediaItemType;
import com.bookingcar.entity.enums.Role;
import com.bookingcar.entity.enums.UserStatus;
import com.bookingcar.repository.MediaItemRepository;
import com.bookingcar.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class MediaItemRepositoryIT {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final MediaItemRepository mediaItemRepository;

    @BeforeEach
    void prepare() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeConnection() {
        entityManager.getTransaction().rollback();
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
