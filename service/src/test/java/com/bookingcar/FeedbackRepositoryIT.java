package com.bookingcar;

import com.bookingcar.entity.Car;
import com.bookingcar.entity.Feedback;
import com.bookingcar.entity.Request;
import com.bookingcar.entity.User;
import com.bookingcar.entity.enums.CarType;
import com.bookingcar.entity.enums.Gender;
import com.bookingcar.entity.enums.RequestStatus;
import com.bookingcar.entity.enums.Role;
import com.bookingcar.entity.enums.UserStatus;
import com.bookingcar.repository.CarRepository;
import com.bookingcar.repository.FeedbackRepository;
import com.bookingcar.repository.RequestRepository;
import com.bookingcar.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class FeedbackRepositoryIT extends BaseIntegrationTest {

    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final RequestRepository requestRepository;
    private final FeedbackRepository feedbackRepository;

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
        feedbackRepository.saveAndFlush(feedback);

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
        feedbackRepository.flush();

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
