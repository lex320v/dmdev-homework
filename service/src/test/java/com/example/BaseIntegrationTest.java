package com.example;

import com.example.config.ApplicationTestConfiguration;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

abstract class BaseIntegrationTest {

    protected static AnnotationConfigApplicationContext context;
    protected static EntityManager entityManager;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(ApplicationTestConfiguration.class);
        entityManager = context.getBean(EntityManager.class);
    }

    @AfterAll
    static void closeSessionFactory() {
        context.close();
    }

    @BeforeEach
    void prepare() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeConnection() {
        entityManager.getTransaction().rollback();
    }
}
