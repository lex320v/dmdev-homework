package com.example.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class HibernateTestUtil {

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.2");

    static {
        postgres.start();
    }

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = HibernateUtil.buildConfiguration();
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.configure();

        System.out.println("===============");
        System.out.println("DB_URL: " + configuration.getProperty("hibernate.connection.url"));
        System.out.println("DB_USERNAME: " + configuration.getProperty("hibernate.connection.username"));
        System.out.println("DB_PASSWORD: " + configuration.getProperty("hibernate.connection.password"));
        System.out.println("===============");

        return configuration.buildSessionFactory();
    }
}
