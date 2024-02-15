package com.example;

import com.example.entity.Car;
import com.example.entity.CarToMediaItem;
import com.example.entity.MediaItem;
import com.example.entity.User;
import com.example.entity.enums.CarType;
import com.example.entity.enums.Gender;
import com.example.entity.enums.MediaItemType;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CarTest {

    SessionFactory sessionFactory;
    Session openedSession;

    @BeforeEach
    public void prepare() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        openedSession = sessionFactory.openSession();
    }

    @Nested
    class carCRUD {

        @Test
        void createAndGetCar() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            sessionFactory.inTransaction(session -> session.persist(user));

            var carFromDb = openedSession.get(Car.class, car.getId());

            assertThat(carFromDb).isNotNull();
        }

        @Test
        void updateCar() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var updatedString = "updated_value";
            var updatedInteger = 1000;
            var updatedDouble = 10.2;
            var updatedBoolean = false;
            var updatedType = CarType.CROSSOVER;

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                car.setManufacturer(updatedString);
                car.setModel(updatedString);
                car.setYear(updatedInteger);
                car.setHorsepower(updatedInteger);
                car.setPrice(updatedDouble);
                car.setActive(updatedBoolean);
                car.setType(updatedType);
            });

            var carFromDb = openedSession.get(Car.class, car.getId());

            assertThat(carFromDb.getManufacturer()).isEqualTo(updatedString);
            assertThat(carFromDb.getModel()).isEqualTo(updatedString);
            assertThat(carFromDb.getYear()).isEqualTo(updatedInteger);
            assertThat(carFromDb.getHorsepower()).isEqualTo(updatedInteger);
            assertThat(carFromDb.getPrice()).isEqualTo(updatedDouble);
            assertThat(carFromDb.getActive()).isEqualTo(updatedBoolean);
            assertThat(carFromDb.getType()).isEqualTo(updatedType);
        }

        @Test
        void deleteCar() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                user.getCars().remove(car);
                session.remove(car);
            });

            var carFromDb = openedSession.get(Car.class, car.getId());

            assertThat(carFromDb).isNull();
        }
    }

    @Nested
    class carMediaItems {
        @Test
        void createAndGetMediaItems() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var image1 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var image2 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var video = buildMediaItem(MediaItemType.CAR_VIDEO, user);

            var carToMediaItem1 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(image1)
                    .position(1)
                    .build();
            var carToMediaItem2 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(image2)
                    .position(2)
                    .build();
            var carToMediaItem3 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(video)
                    .position(1)
                    .build();

            sessionFactory.inTransaction(session -> {
                session.persist(user);

                session.persist(image1);
                session.persist(image2);
                session.persist(video);

                session.persist(carToMediaItem1);
                session.persist(carToMediaItem2);
                session.persist(carToMediaItem3);
            });

            Car carWithMediaItems = openedSession.get(Car.class, car.getId());
            List<CarToMediaItem> carMediaItems = carWithMediaItems.getCarToMediaItems();

            Optional<CarToMediaItem> containImage1 = carMediaItems.stream()
                    .filter(carToMediaItem -> carToMediaItem.getMediaItem().getId() == image1.getId()).findFirst();
            Optional<CarToMediaItem> containImage2 = carMediaItems.stream()
                    .filter(carToMediaItem -> carToMediaItem.getMediaItem().getId() == image2.getId()).findFirst();
            Optional<CarToMediaItem> containVideo = carMediaItems.stream()
                    .filter(carToMediaItem -> carToMediaItem.getMediaItem().getId() == video.getId()).findFirst();

            Assertions.assertTrue(containImage1.isPresent());
            Assertions.assertTrue(containImage2.isPresent());
            Assertions.assertTrue(containVideo.isPresent());
        }

        @Test
        void changeMediaItemsPosition() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var image1 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var image2 = buildMediaItem(MediaItemType.CAR_IMAGE, user);

            var carToMediaItem1 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(image1)
                    .position(1)
                    .build();
            var carToMediaItem2 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(image2)
                    .position(2)
                    .build();

            sessionFactory.inTransaction(session -> {
                session.persist(user);

                session.persist(image1);
                session.persist(image2);

                session.persist(carToMediaItem1);
                session.persist(carToMediaItem2);

                carToMediaItem1.setPosition(2);
                carToMediaItem2.setPosition(1);
            });

            assertThat(carToMediaItem1.getPosition()).isEqualTo(2);
            assertThat(carToMediaItem2.getPosition()).isEqualTo(1);
        }

        @Test
        void deleteMediaItems() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var image1 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var image2 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var video = buildMediaItem(MediaItemType.CAR_VIDEO, user);

            var carToMediaItem1 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(image1)
                    .position(1)
                    .build();
            var carToMediaItem2 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(image2)
                    .position(2)
                    .build();
            var carToMediaItem3 = CarToMediaItem.builder()
                    .car(car)
                    .mediaItem(video)
                    .position(1)
                    .build();

            sessionFactory.inTransaction(session -> {
                session.persist(user);

                session.persist(image1);
                session.persist(image2);
                session.persist(video);

                session.persist(carToMediaItem1);
                session.persist(carToMediaItem2);
                session.persist(carToMediaItem3);

                session.remove(image1);
                session.remove(image2);
                session.remove(video);
            });

            Car carWithoutMediaItems = openedSession.get(Car.class, car.getId());

            assertThat(carWithoutMediaItems.getCarToMediaItems()).isEmpty();
        }
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

    private MediaItem buildMediaItem(MediaItemType type, User uploader) {
        return MediaItem.builder()
                .type(type)
                .mimeType("mime_type")
                .link("link")
                .previewLink("preview_link")
                .uploader(uploader)
                .build();
    }
}
