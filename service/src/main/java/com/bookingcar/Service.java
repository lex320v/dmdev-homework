package com.bookingcar;

import com.example.Common;
import com.bookingcar.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {

        var context = SpringApplication.run(Service.class, args);

        var entityManager = context.getBean(EntityManager.class);
        var userRepository = context.getBean(UserRepository.class);
        entityManager.getTransaction().begin();
        var t = userRepository.findAll();

        System.out.println(t);

        entityManager.getTransaction().commit();
    }
}
