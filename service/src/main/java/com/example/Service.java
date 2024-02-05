package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            System.out.println("##");
        }
    }

    public static void serviceMethod() {
        String commonValue = common.commonMethod();

        System.out.println("log from service");
        System.out.println("value from common: " + commonValue);
    }

}
