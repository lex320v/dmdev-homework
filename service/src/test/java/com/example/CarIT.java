package com.example;

import com.example.dao.CarRepository;
import com.example.dao.CarToMediaItemRepository;
import com.example.dao.MediaItemRepository;
import com.example.dao.UserRepository;
import com.example.entity.Car;
import com.example.entity.CarToMediaItem;
import com.example.entity.CarToMediaItemId;
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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CarIT {

    private static SessionFactory sessionFactory;
    private static Session session;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private MediaItemRepository mediaItemRepository;
    private CarToMediaItemRepository carToMediaItemRepository;


    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    void prepare() {
        session = sessionFactory.getCurrentSession();
        userRepository = new UserRepository(session);
        carRepository = new CarRepository(session);
        mediaItemRepository = new MediaItemRepository(session);
        carToMediaItemRepository = new CarToMediaItemRepository(session);
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

    @Nested
    class CarCRUD {

        @Test
        void createAndReadCar() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            userRepository.save(user);
            carRepository.save(car);
            session.evict(car);

            var carFromDb = carRepository.findById(car.getId());

            assertTrue(carFromDb.isPresent());
            assertThat(carFromDb.get()).isEqualTo(car);
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

            userRepository.save(user);
            carRepository.save(car);

            car.setManufacturer(updatedString);
            car.setModel(updatedString);
            car.setYear(updatedInteger);
            car.setHorsepower(updatedInteger);
            car.setPrice(updatedDouble);
            car.setActive(updatedBoolean);
            car.setType(updatedType);

            carRepository.update(car);

            session.evict(car);
            var carFromDb = carRepository.findById(car.getId());

            assertTrue(carFromDb.isPresent());
            assertThat(carFromDb.get().getManufacturer()).isEqualTo(updatedString);
            assertThat(carFromDb.get().getModel()).isEqualTo(updatedString);
            assertThat(carFromDb.get().getYear()).isEqualTo(updatedInteger);
            assertThat(carFromDb.get().getHorsepower()).isEqualTo(updatedInteger);
            assertThat(carFromDb.get().getPrice()).isEqualTo(updatedDouble);
            assertThat(carFromDb.get().getActive()).isEqualTo(updatedBoolean);
            assertThat(carFromDb.get().getType()).isEqualTo(updatedType);
        }

        @Test
        void deleteCar() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            userRepository.save(user);
            carRepository.save(car);

            user.getCars().remove(car);
            carRepository.delete(car);

            var carFromDb = carRepository.findById(car.getId());

            assertTrue(carFromDb.isEmpty());
        }
    }

    @Nested
    class CarMediaItemsCRUD {
        @Test
        void createAndReadMediaItems() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var image1 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var image2 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var video = buildMediaItem(MediaItemType.CAR_VIDEO, user);

            userRepository.save(user);
            carRepository.save(car);

            mediaItemRepository.save(image1);
            mediaItemRepository.save(image2);
            mediaItemRepository.save(video);

            var carToMediaItem1 = buildCarToMediaItem(car, image1, 1);
            var carToMediaItem2 = buildCarToMediaItem(car, image2, 2);
            var carToMediaItem3 = buildCarToMediaItem(car, video, 1);

            carToMediaItemRepository.save(carToMediaItem1);
            carToMediaItemRepository.save(carToMediaItem2);
            carToMediaItemRepository.save(carToMediaItem3);
            session.flush();
            session.evict(carToMediaItem1);
            session.evict(carToMediaItem2);
            session.evict(carToMediaItem3);

            var key1 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(image1.getId())
                    .build();
            var key2 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(image2.getId())
                    .build();
            var key3 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(video.getId())
                    .build();

            Optional<CarToMediaItem> carMedia1 =
                    carToMediaItemRepository.findById(key1);
            Optional<CarToMediaItem> carMedia2 =
                    carToMediaItemRepository.findById(key2);
            Optional<CarToMediaItem> carMedia3 =
                    carToMediaItemRepository.findById(key3);

            assertTrue(carMedia1.isPresent());
            assertTrue(carMedia2.isPresent());
            assertTrue(carMedia3.isPresent());
        }

        @Test
        void changeMediaItemsPosition() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var image1 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var image2 = buildMediaItem(MediaItemType.CAR_IMAGE, user);

            userRepository.save(user);
            carRepository.save(car);

            mediaItemRepository.save(image1);
            mediaItemRepository.save(image2);

            var carToMediaItem1 = buildCarToMediaItem(car, image1, 1);
            var carToMediaItem2 = buildCarToMediaItem(car, image2, 2);

            carToMediaItemRepository.save(carToMediaItem1);
            carToMediaItemRepository.save(carToMediaItem2);

            carToMediaItem1.setPosition(2);
            carToMediaItemRepository.update(carToMediaItem1);

            carToMediaItem2.setPosition(1);
            carToMediaItemRepository.update(carToMediaItem2);

            session.evict(carToMediaItem1);
            session.evict(carToMediaItem2);
            var key1 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(image1.getId())
                    .build();
            var key2 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(image2.getId())
                    .build();
            var ctmiFromDb1 = carToMediaItemRepository.findById(key1);
            var ctmiFromDb2 = carToMediaItemRepository.findById(key2);

            assertTrue(ctmiFromDb1.isPresent());
            assertTrue(ctmiFromDb2.isPresent());
            assertThat(ctmiFromDb1.get().getPosition()).isEqualTo(2);
            assertThat(ctmiFromDb2.get().getPosition()).isEqualTo(1);
        }

        @Test
        void deleteMediaItems() {
            var user = buildUser();
            var car = buildCar();
            user.addCar(car);

            var image1 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var image2 = buildMediaItem(MediaItemType.CAR_IMAGE, user);
            var video = buildMediaItem(MediaItemType.CAR_VIDEO, user);

            userRepository.save(user);
            carRepository.save(car);

            mediaItemRepository.save(image1);
            mediaItemRepository.save(image2);
            mediaItemRepository.save(video);

            var carToMediaItem1 = buildCarToMediaItem(car, image1, 1);
            var carToMediaItem2 = buildCarToMediaItem(car, image2, 2);
            var carToMediaItem3 = buildCarToMediaItem(car, video, 1);

            carToMediaItemRepository.save(carToMediaItem1);
            carToMediaItemRepository.save(carToMediaItem2);
            carToMediaItemRepository.save(carToMediaItem3);
            session.flush();

            carToMediaItemRepository.delete(carToMediaItem1);
            carToMediaItemRepository.delete(carToMediaItem2);
            carToMediaItemRepository.delete(carToMediaItem3);

            var key1 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(image1.getId())
                    .build();
            var key2 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(image2.getId())
                    .build();
            var key3 = CarToMediaItemId.builder()
                    .carId(car.getId())
                    .mediaItemId(video.getId())
                    .build();
            Optional<CarToMediaItem> carMedia1 =
                    carToMediaItemRepository.findById(key1);
            Optional<CarToMediaItem> carMedia2 =
                    carToMediaItemRepository.findById(key2);
            Optional<CarToMediaItem> carMedia3 =
                    carToMediaItemRepository.findById(key3);

            assertTrue(carMedia1.isEmpty());
            assertTrue(carMedia2.isEmpty());
            assertTrue(carMedia3.isEmpty());
        }
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

    private CarToMediaItem buildCarToMediaItem(Car car, MediaItem mediaItem, int position) {
        var carToMediaItemId = CarToMediaItemId.builder()
                .carId(car.getId())
                .mediaItemId(mediaItem.getId())
                .build();
        return CarToMediaItem.builder()
                .id(carToMediaItemId)
                .position(position)
                .build();
    }
}
