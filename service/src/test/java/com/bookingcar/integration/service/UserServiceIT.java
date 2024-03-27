package com.bookingcar.integration.service;

import com.bookingcar.BaseIntegrationTest;
import com.bookingcar.dto.UserCreateEditDto;
import com.bookingcar.entity.User;
import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.Role;
import com.bookingcar.entity.enums.UserStatus;
import com.bookingcar.repository.UserRepository;
import com.bookingcar.service.UserService;
import com.bookingcar.util.TestDataImporter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserServiceIT extends BaseIntegrationTest {

    private final UserService userService;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Test
    void findAll() {
        TestDataImporter.importData(entityManager);

        var result = userService.findAll();
        assertThat(result).hasSize(10);
    }

    @Test
    void findById() {
        var savedUser = userRepository.save(buildUser());
        entityManager.detach(savedUser);

        var maybeUser = userService.findById(savedUser.getId());

        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals("test_username", user.getUsername()));
    }

    @Test
    void create() {
        var userCreateEditDto = new UserCreateEditDto(
                "new_user",
                "test",
                "test",
                "test",
                Role.ADMIN,
                Gender.MALE,
                LocalDate.of(2001, 1, 1)
        );

        var createdUser = userService.create(userCreateEditDto);
        assertEquals(userCreateEditDto.getUsername(), createdUser.getUsername());
        assertEquals(userCreateEditDto.getFirstname(), createdUser.getFirstname());
        assertEquals(userCreateEditDto.getLastname(), createdUser.getLastname());
        assertSame(userCreateEditDto.getRole(), createdUser.getRole());
        assertSame(userCreateEditDto.getGender(), createdUser.getGender());
    }

    @Test
    void update() {
        var savedUser = userRepository.save(buildUser());
        entityManager.detach(savedUser);

        var userCreateEditDto = new UserCreateEditDto(
                "new_username",
                "test",
                "test",
                "test",
                Role.ADMIN,
                Gender.MALE,
                LocalDate.of(2001, 1, 1)
        );

        var updatedUser = userService.update(savedUser.getId(), userCreateEditDto);

        assertTrue(updatedUser.isPresent());
        updatedUser.ifPresent(user -> {
            assertEquals(userCreateEditDto.getUsername(), user.getUsername());
            assertEquals(userCreateEditDto.getFirstname(), user.getFirstname());
            assertEquals(userCreateEditDto.getLastname(), user.getLastname());
            assertSame(userCreateEditDto.getRole(), user.getRole());
            assertSame(userCreateEditDto.getGender(), user.getGender());
        });
    }

    @Test
    void delete() {
        var savedUser = userRepository.save(buildUser());
        entityManager.detach(savedUser);

        assertTrue(userService.delete(savedUser.getId()));
        assertFalse(userService.delete(-999L));
    }

    private User buildUser() {
        return User.builder()
                .username("test_username")
                .firstname("Александр")
                .lastname("Кузьмин")
                .password("qwerty")
                .status(UserStatus.ACTIVE)
                .gender(Gender.MALE)
                .role(Role.SUPER_ADMIN)
                .dateOfBirth(LocalDate.of(2001, 1, 1))
                .build();
    }
}