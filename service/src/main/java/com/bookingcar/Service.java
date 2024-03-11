package com.bookingcar;

import com.bookingcar.util.HibernateUtil;
import com.example.Common;
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
