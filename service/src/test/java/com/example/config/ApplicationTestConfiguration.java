package com.example.config;

import com.example.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Import(ApplicationConfiguration.class)
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = "com.example")
public class ApplicationTestConfiguration {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
         return HibernateTestUtil.buildSessionFactory();
    }
}
