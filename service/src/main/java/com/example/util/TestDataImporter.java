package com.example.util;

import com.example.entity.Car;
import com.example.entity.Feedback;
import com.example.entity.PersonalInfo;
import com.example.entity.Request;
import com.example.entity.User;
import com.example.entity.enums.CarType;
import com.example.entity.enums.Gender;
import com.example.entity.enums.RequestStatus;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

@UtilityClass
public class TestDataImporter {

    public void importData(SessionFactory sessionFactory) {

        sessionFactory.inTransaction(session -> {
            saveUser(session, "miness", "Александр", "Кузнецов", Gender.MALE, Role.SUPER_ADMIN);
            saveUser(session, "victorin", "Артём", "Михайлов", Gender.MALE, Role.ADMIN);
            saveUser(session, "xuan", "Михаил", "Черкасов", Gender.MALE, Role.ADMIN);

            var owner1 = saveUser(session, "hagopati", "Константин", "Ларин", Gender.MALE, Role.OWNER);
            var owner2 = saveUser(session, "ilardin", "Анастасия", "Орлова", Gender.FEMALE, Role.OWNER);
            saveUser(session, "yatikai", "Анна", "Кочергина", Gender.FEMALE, Role.OWNER);

            var client1 = saveUser(session, "atelgina", "Арина", "Новикова", Gender.FEMALE, Role.CLIENT);
            var client2 = saveUser(session, "michels", "Мирослава", "Суслова", Gender.FEMALE, Role.CLIENT);
            saveUser(session, "rtonyoka", "Александр", "Петров", Gender.MALE, Role.CLIENT);
            saveUser(session, "uniyand", "Василиса", "Сидорова", Gender.FEMALE, Role.CLIENT);

            var car1 = saveCar(session, owner1, "Toyota", "Camry");
            var car2 = saveCar(session, owner2, "Ford", "Focus");
            saveCar(session, owner1, "Audi", "TT");
            saveCar(session, owner1, "Nissan", "X-Trail");
            saveCar(session, owner1, "Suzuki", "Swift");
            saveCar(session, owner1, "Citroen", "C4");
            saveCar(session, owner1, "Renault", "Zoe");
            saveCar(session, owner1, "Jeep", "Wrangler");
            saveCar(session, owner1, "Ford", "Explorer");
            saveCar(session, owner1, "Lexus", "ES");
            saveCar(session, owner1, "Volkswagen", "Jetta");
            saveCar(session, owner1, "GMC", "Terrain");
            saveCar(session, owner1, "Volkswagen", "Golf");
            saveCar(session, owner2, "Land Rover", "Range Rover");
            saveCar(session, owner2, "Toyota", "Tacoma");
            saveCar(session, owner2, "Lincoln", "MKZ");

            saveRequest(session, client1, car1, RequestStatus.OPEN);
            saveRequest(session, client1, car2, RequestStatus.OPEN);
            saveRequest(session, client2, car1, RequestStatus.OPEN);

            var request1 = saveRequest(session, client1, car1, RequestStatus.CLOSED);
            var request2 = saveRequest(session, client1, car1, RequestStatus.CLOSED);
            var request3 = saveRequest(session, client1, car1, RequestStatus.CLOSED);

            var request4 = saveRequest(session, client1, car2, RequestStatus.CLOSED);
            var request5 = saveRequest(session, client1, car2, RequestStatus.CLOSED);
            var request6 = saveRequest(session, client1, car2, RequestStatus.CLOSED);

            saveFeedback(session, request1, 3);
            saveFeedback(session, request2, 4);
            saveFeedback(session, request3, 5);

            saveFeedback(session, request4, 2);
            saveFeedback(session, request5, 2);
            saveFeedback(session, request6, 3);
        });
    }

    private User saveUser(Session session, String username, String firstname, String lastname,
                          Gender gender, Role role) {
        var personalInfo = PersonalInfo.builder()
                .dateOfBirth(LocalDate.of(randomInt(1990, 2000), randomInt(1, 12), randomInt(1, 29)))
                .build();
        var user = User.builder()
                .username(username)
                .firstname(firstname)
                .lastname(lastname)
                .password("qwerty")
                .status(UserStatus.ACTIVE)
                .gender(gender)
                .role(role)
                .build();

        session.persist(user);
        personalInfo.setUser(user);
        session.flush();

        return user;
    }

    private Car saveCar(Session session, User owner, String manufacturer, String model) {
        CarType[] values = CarType.values();

        Car car = Car.builder()
                .manufacturer(manufacturer)
                .model(model)
                .year(randomInt(2005, 2022))
                .horsepower(randomInt(60, 600))
                .price((double) randomInt(100, 9000))
                .active(true)
                .type(values[randomInt(0, values.length - 1)])
                .owner(owner)
                .build();

        session.persist(car);

        return car;
    }

    private Request saveRequest(Session session, User client, Car car, RequestStatus status) {
        var request =  Request.builder()
                .dateTimeFrom(LocalDateTime.now())
                .dateTimeTo(LocalDateTime.now().plusHours(8))
                .client(client)
                .car(car)
                .status(status)
                .build();

        session.persist(request);

        return request;
    }

    private Feedback saveFeedback(Session session, Request request, int rating) {
        Feedback feedback = Feedback.builder()
                .rating(rating)
                .text("text")
                .request(request)
                .build();

        session.persist(feedback);

        return feedback;
    }

    private int randomInt(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
