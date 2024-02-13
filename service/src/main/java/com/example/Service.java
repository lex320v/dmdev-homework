package com.example;

import com.example.entity.Car;
import com.example.entity.CarToMediaItem;
import com.example.entity.Feedback;
import com.example.entity.MediaItem;
import com.example.entity.PersonalInfo;
import com.example.entity.Request;
import com.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(PersonalInfo.class);
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Request.class);
        configuration.addAnnotatedClass(Feedback.class);
        configuration.addAnnotatedClass(MediaItem.class);
        configuration.addAnnotatedClass(CarToMediaItem.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            User user = User.builder()
//                    .username("777")
//                    .password("22")
//                    .status(UserStatus.ACTIVE)
//                    .role(Role.ADMIN)
//                    .build();
//
//            session.persist(user);
//            User user = session.get(User.class, 1);
//            User user = User.builder()
//                    .id(1L)
//                    .build();
//            Car car = Car.builder()
//                    .manufacturer("11331")
//                    .model("222")
//                    .year(123)
//                    .horsepower(123)
//                    .price(444.12)
//                    .isActive(true)
//                    .type(CarType.SEDAN)
//                    .owner(user)
//                    .build();
//            session.persist(car);

            Car car = session.get(Car.class, 3);
            System.out.println("## " + car);

            session.getTransaction().commit();

        }
    }
}
