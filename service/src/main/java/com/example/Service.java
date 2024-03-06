package com.example;

import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
        }
    }
}
