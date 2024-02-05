package com.example;

import com.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

import java.time.LocalDate;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .id(1L)
                    .username("11")
                    .firstname("222")
                    .lastname("333")
                    .birthDate(LocalDate.of(2000, 1, 15))
                    .build();

            session.persist(user);

            session.getTransaction().commit();

            System.out.println("##");
        }
    }

    public static void serviceMethod() {
        String commonValue = common.commonMethod();

        System.out.println("log from service");
        System.out.println("value from common: " + commonValue);
    }

}
