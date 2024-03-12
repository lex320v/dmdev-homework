package com.bookingcar;

import com.bookingcar.config.ApplicationConfiguration;
import com.bookingcar.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ApplicationConfiguration.class)
@TestConfiguration
public class TestApplicationRunner {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}