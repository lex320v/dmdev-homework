package com.example;

import com.example.dao.UserDao;
import com.example.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

@Slf4j
public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()
        ) {
            var t = UserDao.getInstance().findAll(session);

            System.out.println(t);
        }
    }
}
