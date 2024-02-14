package com.example;

import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FirstTest {

    @Test
    public void testDB() {

        try (SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory()) {
            User user = User.builder()
                    .username("qqqq11")
                    .password("22")
                    .status(UserStatus.ACTIVE)
                    .gender(Gender.MALE)
                    .role(Role.ADMIN)
                    .build();
            User user1 = User.builder()
                    .username("wwwww2")
                    .password("22")
                    .status(UserStatus.ACTIVE)
                    .gender(Gender.FEMALE)
                    .role(Role.ADMIN)
                    .build();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                session.persist(user1);

                var savedUser = session.get(User.class, user.getId());
                var savedUser1 = session.get(User.class, user1.getId());

                System.out.println(user);
                System.out.println(user1);

                System.out.println(savedUser);
                System.out.println(savedUser1);
            });
        }

        assertTrue(true);
    }
}
