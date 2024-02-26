package com.example;

import com.example.dao.UserCriteriaDao;
import com.example.dao.UserQueryDslDao;
import com.example.dto.UserFilterDto;
import com.example.entity.User;
import com.example.util.HibernateTestUtil;
import com.example.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class UserDaoIT {

    private static final SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
    private final UserQueryDslDao userQueryDslDao = UserQueryDslDao.getInstance();
    private final UserCriteriaDao userCriteriaDao = UserCriteriaDao.getInstance();
    private Session session;


    @BeforeAll
    static void initDb() {
        TestDataImporter.importData(sessionFactory);
    }

    @AfterAll
    static void finish() {
        sessionFactory.close();
    }

    @BeforeEach
    void prepare() {
        session = sessionFactory.openSession();
    }

    @AfterEach
    void closeConnection() {
        session.close();
    }

    @Nested
    class CriteriaApi {
        @Test
        void findAllByFirstnameAndLastname() {
            var searchFirstname = "алекс";
            var searchLastname = "куз";
            UserFilterDto userFilterDto = UserFilterDto.builder()
                    .firstname(searchFirstname)
                    .lastname(searchLastname)
                    .build();

            var result = userCriteriaDao.findUsers(session, userFilterDto);

            var checkedResult = result.stream().allMatch(user ->
                    user.getFirstname().toLowerCase().contains(searchFirstname) &&
                            user.getLastname().toLowerCase().contains(searchLastname)
            );


            assertTrue(checkedResult);
        }

        @Test
        void findAllByUsername() {
            var searchUsername = "miness";
            UserFilterDto userFilterDto = UserFilterDto.builder()
                    .username(searchUsername)
                    .build();

            var result = userCriteriaDao.findUsers(session, userFilterDto);

            Optional<User> user = result.stream().findFirst();

            if (user.isPresent()) {
                assertEquals(searchUsername, user.get().getUsername());
            } else {
                fail();
            }
        }

        @Test
        void findAllWithLimit() {
            UserFilterDto userFilterDto = UserFilterDto.builder().limit(5).build();

            var result = userCriteriaDao.findUsers(session, userFilterDto);

            assertEquals(5, result.size());
        }
    }

    @Nested
    class QueryDsl {
        @Test
        void findAllByFirstnameAndLastname() {
            var searchFirstname = "алекс";
            var searchLastname = "куз";
            UserFilterDto userFilterDto = UserFilterDto.builder()
                    .firstname(searchFirstname)
                    .lastname(searchLastname)
                    .build();

            var result = userQueryDslDao.findUsers(session, userFilterDto);

            var checkedResult = result.stream().allMatch(user ->
                    user.getFirstname().toLowerCase().contains(searchFirstname) &&
                            user.getLastname().toLowerCase().contains(searchLastname)
            );


            assertTrue(checkedResult);
        }

        @Test
        void findAllByUsername() {
            var searchUsername = "miness";
            UserFilterDto userFilterDto = UserFilterDto.builder()
                    .username(searchUsername)
                    .build();

            var result = userQueryDslDao.findUsers(session, userFilterDto);

            Optional<User> user = result.stream().findFirst();

            if (user.isPresent()) {
                assertEquals(searchUsername, user.get().getUsername());
            } else {
                fail();
            }
        }

        @Test
        void findAllWithLimit() {
            UserFilterDto userFilterDto = UserFilterDto.builder().limit(5).build();

            var result = userQueryDslDao.findUsers(session, userFilterDto);

            assertEquals(5, result.size());
        }
    }
}
