package com.example;

import com.example.entity.Car;
import com.example.entity.Feedback;
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

import static org.assertj.core.api.Assertions.assertThat;

class FeedbackIT {
    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void prepare() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void closeConnection() {
        session.getTransaction().rollback();
    }

    @AfterAll
    static void closeSessionFactory() {
        session.close();
        sessionFactory.close();
    }

    @Test
    void createAndGetFeedback() {
        var user = buildUser();
        var car = buildCar();
        user.addCar(car);

        var request = buildRequest();
        car.addRequest(request, user);

        var feedback = buildFeedback();
        request.addFeedback(feedback);

        session.persist(user);

        var feedbackFromDb = session.get(Feedback.class, feedback.getId());

        assertThat(feedbackFromDb).isNotNull();
    }

    @Test
    void updateFeedback() {
        var user = buildUser();
        var car = buildCar();
        user.addCar(car);

        var request = buildRequest();
        car.addRequest(request, user);

        var feedback = buildFeedback();
        request.addFeedback(feedback);

        var updatedString = "updated string";
        var updatedInteger = 10;

        session.persist(user);

        feedback.setText(updatedString);
        feedback.setRating(updatedInteger);
        session.flush();

        var feedbackFromDb = session.get(Feedback.class, feedback.getId());

        assertThat(feedbackFromDb.getText()).isEqualTo(updatedString);
        assertThat(feedbackFromDb.getRating()).isEqualTo(updatedInteger);
    }

    @Test
    void deleteFeedback() {
        var user = buildUser();
        var car = buildCar();
        user.addCar(car);

        var request = buildRequest();
        car.addRequest(request, user);

        var feedback = buildFeedback();
        request.addFeedback(feedback);

        session.persist(user);
        request.getFeedbacks().remove(feedback);

        session.remove(feedback);
        session.flush();

        var feedbackFromDb = session.get(Feedback.class, feedback.getId());

        assertThat(feedbackFromDb).isNull();
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

    private Car buildCar() {
        return Car.builder()
                .manufacturer("manufacturer")
                .model("model")
                .year(2015)
                .horsepower(115)
                .price(20.4)
                .active(true)
                .type(CarType.SEDAN)
                .build();
    }

    private Request buildRequest() {
        return Request.builder()
                .dateTimeFrom(LocalDateTime.now())
                .dateTimeTo(LocalDateTime.now().plusHours(8))
                .status(RequestStatus.OPEN)
                .build();
    }

    private Feedback buildFeedback() {
        return Feedback.builder()
                .rating(5)
                .text("text")
                .build();

    }
}
