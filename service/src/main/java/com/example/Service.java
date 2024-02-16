package com.example;

import com.example.entity.User;
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
//                    .dateOfBirth(LocalDate.of(2000, 5, 5))
//                    .driverLicenseSurname("qqq")
//                    .driverLicenseName("www")
//                    .driverLicensePlaceOfBirth("qqq")
//                    .driverLicenseDateOfIssue(LocalDate.of(2000, 5, 5))
//                    .driverLicenseDateOfExpire(LocalDate.of(2000, 5, 5))
//                    .driverLicenseIssuedBy("qqqq")
//                    .driverLicenseCode("123 123")
//                    .driverLicenseResidence("eeee")
//                    .driverLicenseCategories(List.of(DriverLicenseCategories.A))
//                    .id(2L)
//                    .build();
//            User user1 = User.builder()
//                    .username("lex3")
//                    .firstname("firstname_lex")
//                    .lastname("lastname_lex")
//                    .password("qwerty")
//                    .status(UserStatus.ACTIVE)
//                    .gender(Gender.MALE)
//                    .role(Role.SUPER_ADMIN)
//                    .build();
//            MediaItem mediaItem = MediaItem.builder()
//                    .type(MediaItemType.AVATAR)
//                    .link("111")
//                    .previewLink("222")
//                    .mimeType("3333")
//                    .uploader(User.builder().id(2L).build())
//                    .build();
//            Car car = Car.builder()
//                    .model("111")
//                    .price(13.3)
//                    .manufacturer("bbbb")
//                    .year(2020)
//                    .type(CarType.SEDAN)
//                    .horsepower(123)
//                    .active(true)
//                    .owner(User.builder().id(2L).build())
//                    .build();

//            session.persist(car);

//            var t = session.get(User.class, 1);
            var t = session.get(User.class, 1);
            System.out.println();
            session.getTransaction().commit();

        }
    }
}
