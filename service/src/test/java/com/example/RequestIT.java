package com.example;

import com.example.dao.CarRepository;
import com.example.dao.RequestRepository;
import com.example.dao.UserRepository;
import com.example.entity.Car;
import com.example.entity.Request;
import com.example.entity.User;
import com.example.entity.enums.CarType;
import com.example.entity.enums.Gender;
import com.example.entity.enums.RequestStatus;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private RequestRepository requestRepository;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void prepare() {
        session = sessionFactory.getCurrentSession();
        userRepository = new UserRepository(session);
        carRepository = new CarRepository(session);
        requestRepository = new RequestRepository(session);
        session.beginTransaction();
    }

    @AfterEach
    void closeConnection() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void createAndReadRequest() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);
        session.evict(request);

        var requestFromDb = requestRepository.findById(request.getId());

        assertTrue(requestFromDb.isPresent());
        assertThat(requestFromDb.get()).isEqualTo(request);
    }

    @Test
    void updateRequest() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);

        var updatedDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        var updatedStatus = RequestStatus.REJECTED;
        var updatedString = "updated value";

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);

        request.setDateTimeFrom(updatedDateTime);
        request.setDateTimeTo(updatedDateTime);
        request.setStatus(updatedStatus);
        request.setComment(updatedString);

        requestRepository.update(request);

        assertThat(request.getDateTimeFrom()).isEqualTo(updatedDateTime);
        assertThat(request.getDateTimeTo()).isEqualTo(updatedDateTime);
        assertThat(request.getStatus()).isEqualTo(updatedStatus);
        assertThat(request.getComment()).isEqualTo(updatedString);
    }

    @Test
    void deleteRequest() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);
        requestRepository.delete(request);

        var requestFromDb = requestRepository.findById(request.getId());

        assertTrue(requestFromDb.isEmpty());
    }

    private User buildUser() {
        return User.builder()
                .username("username")
                .firstname("firstname")
                .lastname("lastname")
                .password("password")
                .status(UserStatus.ACTIVE)
                .gender(Gender.MALE)
                .role(Role.SUPER_ADMIN)
                .build();
    }

    private Car buildCar(User user) {
        return Car.builder()
                .manufacturer("manufacturer")
                .model("model")
                .year(2015)
                .horsepower(115)
                .price(20.4)
                .active(true)
                .type(CarType.SEDAN)
                .owner(user)
                .build();
    }

    private Request buildRequest(User client, Car car) {
        return Request.builder()
                .dateTimeFrom(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS))
                .dateTimeTo(LocalDateTime.now().plusHours(8).truncatedTo(ChronoUnit.MICROS))
                .client(client)
                .car(car)
                .status(RequestStatus.OPEN)
                .build();
    }
}
