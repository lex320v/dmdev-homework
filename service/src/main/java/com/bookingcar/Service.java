package com.bookingcar;

import com.example.Common;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Service {

    private final static Common common = new Common();

    public static void main(String[] args) {

        var context = SpringApplication.run(Service.class, args);

    }
}
