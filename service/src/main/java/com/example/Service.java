package com.example;

import com.example.entity.User;
import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
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
            session.beginTransaction();

//            User user = session.get(User.class, 30);
//            PersonalInfo personalInfo = PersonalInfo.builder()
//                    .driverLicenseSurname("qqq")
//                    .driverLicenseName("www")
//                    .driverLicenseDateOfBirth(LocalDate.of(2000, 5, 5))
//                    .driverLicensePlaceOfBirth("qqq")
//                    .driverLicenseDateOfIssue(LocalDate.of(2000, 5, 5))
//                    .driverLicenseDateOfExpire(LocalDate.of(2000, 5, 5))
//                    .driverLicenseIssuedBy("qqqq")
//                    .driverLicenseCode("123 123")
//                    .driverLicenseResidence("eeee")
//                    .driverLicenseCategories(List.of(DriverLicenseCategories.A, DriverLicenseCategories.B, DriverLicenseCategories.B1))
//                    .build();
            User user1 = User.builder()
                    .username("lex1")
                    .firstname("firstname_lex")
                    .lastname("lastname_lex")
                    .password("qwerty")
                    .status(UserStatus.ACTIVE)
                    .gender(Gender.MALE)
                    .role(Role.SUPER_ADMIN)
                    .build();

            session.persist(user1);
//            personalInfo.setUser(user1);

            session.getTransaction().commit();

        }
    }
}
