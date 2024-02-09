package com.example;

import com.example.entity.Car;
import com.example.entity.Comment;
import com.example.entity.MediaItem;
import com.example.entity.Payment;
import com.example.entity.Request;
import com.example.entity.Role;
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
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Request.class);
        configuration.addAnnotatedClass(Comment.class);
        configuration.addAnnotatedClass(Payment.class);
        configuration.addAnnotatedClass(MediaItem.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = User.builder()
                    .username("11")
                    .firstname("222")
                    .lastname("333")
                    .birthDate(LocalDate.of(2000, 1, 15))
                    .role(Role.ADMIN)
                    .build();

            session.persist(user);

            session.getTransaction().commit();

            System.out.println("##");
        }
    }
}
