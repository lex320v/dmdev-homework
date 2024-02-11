package com.example;

import com.example.entity.Car;
import com.example.entity.Feedback;
import com.example.entity.MediaItem;
import com.example.entity.PersonalInfo;
import com.example.entity.Request;
import com.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FirstTest {

    @Test
    public void testDB() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Request.class);
        configuration.addAnnotatedClass(Feedback.class);
        configuration.addAnnotatedClass(PersonalInfo.class);
        configuration.addAnnotatedClass(MediaItem.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.get(User.class, 1);
            session.get(Car.class, 1);
            session.get(Request.class, 1);
            session.get(Feedback.class, 1);
            session.get(PersonalInfo.class, 1);
            session.get(MediaItem.class, 1);

            session.getTransaction().commit();
        }

        assertTrue(true);
    }
}
