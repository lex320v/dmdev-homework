package com.example.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2");

    static {
//        postgres.start();
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = HibernateUtil.buildConfiguration();
//        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
//        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
//        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/booking_car_test");
        configuration.setProperty("hibernate.connection.username", "postgres");
        configuration.setProperty("hibernate.connection.password", "0000");
        configuration.configure();

        return configuration.buildSessionFactory();
    }
}
