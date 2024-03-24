package com.bookingcar;

import com.bookingcar.dto.UserFilterDto;
import com.bookingcar.entity.User;
import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.Role;
import com.bookingcar.entity.enums.UserStatus;
import com.bookingcar.repository.UserRepository;
import com.bookingcar.util.TestDataImporter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserRepositoryIT extends BaseIntegrationTest {

    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Test
    void findAllByFilterQueryDsl() {
        var user1 = buildUser("aaa", "Александр", "Кузьмин");
        var user2 = buildUser("bbb", "Алексей", "Кузьмиченко");
        var user3 = buildUser("ссс", "Андрей", "Кузьмин");

        userRepository.saveAll(List.of(user1, user2, user3));

        var filter = UserFilterDto.builder().firstname("лекс").build();
        var users = userRepository.findAllByFilterQueryDsl(filter);

        assertEquals(2, users.size());
    }

    @Test
    void findAllByFilterCriteria() {
        var user1 = buildUser("aaa", "Александр", "Кузьмин");
        var user2 = buildUser("bbb", "Алексей", "Кузьмиченко");
        var user3 = buildUser("ссс", "Андрей", "Кузьмин");

        userRepository.saveAll(List.of(user1, user2, user3));

        var filter = UserFilterDto.builder().firstname("лекс").build();
        var users = userRepository.findAllByFilterCriteria(filter);

        assertEquals(2, users.size());
    }

    @Test
    void findAllByFirstnameAndLastname() {
        var user1 = buildUser("aaa", "Александр", "Кузьмин");
        var user2 = buildUser("bbb", "Алексей", "Кузьмиченко");
        var user3 = buildUser("ссс", "Андрей", "Кузьмин");

        userRepository.saveAll(List.of(user1, user2, user3));

        var searchFirstname = "алекс";
        var searchLastname = "куз";
        var result = userRepository.findAllBy(searchFirstname, searchLastname);

        assertThat(result.size()).isEqualTo(2);

        var result1 = result.get(0);
        var result2 = result.get(1);

        assertTrue(result1.getFirstname().toLowerCase().contains(searchFirstname));
        assertTrue(result1.getLastname().toLowerCase().contains(searchLastname));

        assertTrue(result2.getFirstname().toLowerCase().contains(searchFirstname));
        assertTrue(result2.getLastname().toLowerCase().contains(searchLastname));
    }

    @Test
    void findAllByUsername() {
        var user1 = buildUser("aaa", "Александр", "Кузьмин");
        var user2 = buildUser("bbb", "Алексей", "Кузьмиченко");

        userRepository.saveAll(List.of(user1, user2));

        var searchUsername = "aaa";
        var result = userRepository.findByUsernameContainingIgnoreCase(searchUsername);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getUsername()).isEqualTo(searchUsername);
    }

    @Test
    void findAllWithLimit() {
        TestDataImporter.importData(entityManager);

        var pageable = PageRequest.of(1, 5, Sort.sort(User.class).by(User::getCreatedAt));
        var result = userRepository.findAll(pageable);

        assertThat(result.getContent().size()).isEqualTo(5);
    }

    private User buildUser(String username, String firstname, String lastname) {
        return User.builder()
                .username(username)
                .firstname(firstname)
                .lastname(lastname)
                .password("qwerty")
                .status(UserStatus.ACTIVE)
                .gender(Gender.MALE)
                .role(Role.SUPER_ADMIN)
                .build();
    }
}
