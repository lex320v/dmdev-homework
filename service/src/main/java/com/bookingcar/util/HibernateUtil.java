package com.bookingcar.util;

import com.bookingcar.entity.Car;
import com.bookingcar.entity.CarToMediaItem;
import com.bookingcar.entity.Feedback;
import com.bookingcar.entity.MediaItem;
import com.bookingcar.entity.PersonalInfo;
import com.bookingcar.entity.Request;
import com.bookingcar.entity.User;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@UtilityClass
public class HibernateUtil {

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();

        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(PersonalInfo.class);
        configuration.addAnnotatedClass(Car.class);
        configuration.addAnnotatedClass(Request.class);
        configuration.addAnnotatedClass(Feedback.class);
        configuration.addAnnotatedClass(MediaItem.class);
        configuration.addAnnotatedClass(CarToMediaItem.class);

        return configuration;
    }

}
