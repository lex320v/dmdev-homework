package com.bookingcar.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import(ApplicationConfiguration.class)
@TestConfiguration
public class ApplicationTestConfiguration {

//    @Bean(destroyMethod = "close")
//    public SessionFactory sessionFactory() {
//        return HibernateTestUtil.buildSessionFactory();
//    }
}