package com.example.config;

import com.example.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ApplicationConfiguration.class)
@Configuration
public class ApplicationTestConfiguration {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
         return HibernateTestUtil.buildSessionFactory();
    }
}
