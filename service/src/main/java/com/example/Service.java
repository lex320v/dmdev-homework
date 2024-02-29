package com.example;

import com.example.dao.CarRepository;
import com.example.entity.Car;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.Proxy;
import java.util.Optional;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            var session = (Session) Proxy.newProxyInstance(
                    SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1)
            );

            session.beginTransaction();

            CarRepository carRepository = new CarRepository(session);

            Optional<Car> car = carRepository.findById(49L);

            System.out.println(car.get());
            System.out.println(car.get().getOwner());

            session.getTransaction().commit();
        }
    }
}
