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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class FeedbackTest {
    SessionFactory sessionFactory;
    Session openedSession;

    @BeforeEach
    void prepare() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        openedSession = sessionFactory.openSession();
    }

    @Test
    void createAndGetFeedback() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);
        var feedback = buildFeedback(request);

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            session.persist(car);
            session.persist(request);
            session.persist(feedback);
        });

        var feedbackFromDb = openedSession.get(Feedback.class, feedback.getId());

        assertThat(feedbackFromDb).isNotNull();
    }

    @Test
    void updateFeedback() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);
        var feedback = buildFeedback(request);

        var updatedString = "updated string";
        var updatedInteger = 10;

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            session.persist(car);
            session.persist(request);
            session.persist(feedback);

            feedback.setText(updatedString);
            feedback.setRating(updatedInteger);
        });

        var feedbackFromDb = openedSession.get(Feedback.class, feedback.getId());

        assertThat(feedbackFromDb.getText()).isEqualTo(updatedString);
        assertThat(feedbackFromDb.getRating()).isEqualTo(updatedInteger);
    }

    @Test
    void deleteFeedback() {
        var user = buildUser();
        var car = buildCar(user);
        var request = buildRequest(user, car);
        var feedback = buildFeedback(request);

        sessionFactory.inTransaction(session -> {
            session.persist(user);
            session.persist(car);
            session.persist(request);
            session.persist(feedback);

            session.remove(feedback);
        });

        var feedbackFromDb = openedSession.get(Feedback.class, feedback.getId());

        assertThat(feedbackFromDb).isNull();
    }

    @AfterEach
    void closeConnection() {
        openedSession.close();
        sessionFactory.close();
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
                .dateTimeFrom(LocalDateTime.now())
                .dateTimeTo(LocalDateTime.now().plusHours(8))
                .client(client)
                .car(car)
                .status(RequestStatus.OPEN)
                .build();
    }

    private Feedback buildFeedback(Request request) {
        return Feedback.builder()
                .rating(5)
                .text("text")
                .request(request)
                .build();

    }
}
