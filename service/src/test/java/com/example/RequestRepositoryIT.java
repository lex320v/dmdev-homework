package com.example;

import com.example.config.ApplicationTestConfiguration;
import com.example.entity.Car;
import com.example.entity.Request;
import com.example.entity.User;
import com.example.entity.enums.CarType;
import com.example.entity.enums.Gender;
import com.example.entity.enums.RequestStatus;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.repository.CarRepository;
import com.example.repository.RequestRepository;
import com.example.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestRepositoryIT {

    private static AnnotationConfigApplicationContext context;
    private static EntityManager entityManager;
    private static UserRepository userRepository;
    private static CarRepository carRepository;
    private static RequestRepository requestRepository;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(ApplicationTestConfiguration.class);
        userRepository = context.getBean(UserRepository.class);
        carRepository = context.getBean(CarRepository.class);
        requestRepository = context.getBean(RequestRepository.class);
        entityManager = context.getBean(EntityManager.class);
    }

    @BeforeEach
    void prepare() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void closeConnection() {
        entityManager.getTransaction().rollback();
    }

    @AfterAll
    static void closeSessionFactory() {
        context.close();
    }

    @Test
    void createAndReadRequest() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);
        entityManager.detach(request);

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

        entityManager.detach(request);
        var requestFromDb = requestRepository.findById(request.getId());

        assertTrue(requestFromDb.isPresent());
        assertThat(requestFromDb.get().getDateTimeFrom()).isEqualTo(updatedDateTime);
        assertThat(requestFromDb.get().getDateTimeTo()).isEqualTo(updatedDateTime);
        assertThat(requestFromDb.get().getStatus()).isEqualTo(updatedStatus);
        assertThat(requestFromDb.get().getComment()).isEqualTo(updatedString);
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
