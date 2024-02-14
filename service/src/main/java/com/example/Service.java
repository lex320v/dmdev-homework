package com.example;

import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {
            Transaction transaction = session.beginTransaction();

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

            try {
                session.persist(user);
                if (true) throw new RuntimeException();
                session.persist(user1);

                transaction.commit();
            } catch (RuntimeException exception) {
                transaction.rollback();
            }
        }
    }
}
