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
import com.example.repository.CarRepository;
import com.example.repository.FeedbackRepository;
import com.example.repository.RequestRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FeedbackRepositoryIT extends BaseIntegrationTest {

    private static UserRepository userRepository;
    private static CarRepository carRepository;
    private static RequestRepository requestRepository;
    private static FeedbackRepository feedbackRepository;

    @BeforeAll
    static void getRepositories() {
        userRepository = context.getBean(UserRepository.class);
        carRepository = context.getBean(CarRepository.class);
        requestRepository = context.getBean(RequestRepository.class);
        feedbackRepository = context.getBean(FeedbackRepository.class);
    }

    @Test
    void createAndReadFeedback() {
        var user = buildUser();
        var car = buildCar();
        user.addCar(car);

        var request = buildRequest();
        car.addRequest(request, user);

        var feedback = buildFeedback();
        request.addFeedback(feedback);

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);
        feedbackRepository.save(feedback);
        entityManager.detach(feedback);

        var feedbackFromDb = feedbackRepository.findById(feedback.getId());

        assertTrue(feedbackFromDb.isPresent());
        assertThat(feedbackFromDb.get()).isEqualTo(feedback);
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

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);
        feedbackRepository.save(feedback);

        feedback.setText(updatedString);
        feedback.setRating(updatedInteger);
        feedbackRepository.update(feedback);

        entityManager.detach(feedback);
        var feedbackFromDb = feedbackRepository.findById(feedback.getId());

        assertTrue(feedbackFromDb.isPresent());
        assertThat(feedbackFromDb.get().getText()).isEqualTo(updatedString);
        assertThat(feedbackFromDb.get().getRating()).isEqualTo(updatedInteger);
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

        userRepository.save(user);
        carRepository.save(car);
        requestRepository.save(request);
        feedbackRepository.save(feedback);

        feedbackRepository.delete(feedback);

        var feedbackFromDb = feedbackRepository.findById(feedback.getId());

        assertTrue(feedbackFromDb.isEmpty());
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
