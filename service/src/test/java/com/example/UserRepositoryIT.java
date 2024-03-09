package com.example;

import com.example.config.ApplicationTestConfiguration;
import com.example.dto.UserFilterDto;
import com.example.entity.MediaItem;
import com.example.entity.PersonalInfo;
import com.example.entity.User;
import com.example.entity.enums.DriverLicenseCategories;
import com.example.entity.enums.Gender;
import com.example.entity.enums.MediaItemType;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import com.example.repository.MediaItemRepository;
import com.example.repository.PersonalInfoRepository;
import com.example.repository.UserRepository;
import com.example.util.TestDataImporter;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryIT {

    private static AnnotationConfigApplicationContext context;
    private static EntityManager entityManager;
    private static UserRepository userRepository;
    private static PersonalInfoRepository personalInfoRepository;
    private static MediaItemRepository mediaItemRepository;

    @BeforeAll
    static void init() {
        context = new AnnotationConfigApplicationContext(ApplicationTestConfiguration.class);
        entityManager = context.getBean(EntityManager.class);
        userRepository = context.getBean(UserRepository.class);
        personalInfoRepository = context.getBean(PersonalInfoRepository.class);
        mediaItemRepository = context.getBean(MediaItemRepository.class);
    }

    @BeforeEach
    public void prepare() {
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

    @Nested
    class UserCRUD {
        @Test
        void createAndReadUser() {
            var user = buildUser("lex1", "firstname_lex", "lastname_lex");
            userRepository.save(user);

            entityManager.detach(user);
            var userFromDb = userRepository.findById(user.getId());

            assertTrue(userFromDb.isPresent());
            assertThat(userFromDb.get()).isEqualTo(user);
        }

        @Test
        void updateUser() {
            var user = buildUser("lex1", "firstname_lex", "lastname_lex");

            var updatedString = "updated_value";
            var updatedGender = Gender.FEMALE;
            var updatedRole = Role.CLIENT;

            userRepository.save(user);

            user.setUsername(updatedString);
            user.setFirstname(updatedString);
            user.setLastname(updatedString);
            user.setGender(updatedGender);
            user.setRole(updatedRole);
            user.setPassword(updatedString);
            userRepository.update(user);

            entityManager.detach(user);
            var userFromDb = userRepository.findById(user.getId());

            assertTrue(userFromDb.isPresent());
            assertThat(userFromDb.get().getUsername()).isEqualTo(updatedString);
            assertThat(userFromDb.get().getFirstname()).isEqualTo(updatedString);
            assertThat(userFromDb.get().getLastname()).isEqualTo(updatedString);
            assertThat(userFromDb.get().getGender()).isEqualTo(updatedGender);
            assertThat(userFromDb.get().getRole()).isEqualTo(updatedRole);
            assertThat(userFromDb.get().getPassword()).isEqualTo(updatedString);
        }

        @Test
        void deleteUser() {
            User user = buildUser("lex1", "firstname_lex", "lastname_lex");

            userRepository.save(user);
            userRepository.delete(user);

            Optional<User> userFromDb = userRepository.findById(user.getId());

            assertTrue(userFromDb.isEmpty());
        }

        @Test
        void findAllByFirstnameAndLastname() {
            var user1 = buildUser("aaa", "Александр", "Кузьмин");
            var user2 = buildUser("bbb", "Алексей", "Кузьмиченко");
            var user3 = buildUser("ссс", "Андрей", "Кузьмин");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            var searchFirstname = "алекс";
            var searchLastname = "куз";
            UserFilterDto userFilterDto = UserFilterDto.builder()
                    .firstname(searchFirstname)
                    .lastname(searchLastname)
                    .build();

            var result = userRepository.findAll(userFilterDto);

            assertThat(result.size()).isEqualTo(2);

            var result1 = result.get(0);
            var result2 = result.get(1);

            assertTrue(result1.getFirstname().toLowerCase().contains(searchFirstname));
            assertTrue(result1.getLastname().toLowerCase().contains(searchLastname));

            assertTrue(result2.getFirstname().toLowerCase().contains(searchFirstname));
            assertTrue(result2.getLastname().toLowerCase().contains(searchLastname));
        }

        @Test
        void findAllByUsername() {
            var user1 = buildUser("aaa", "Александр", "Кузьмин");
            var user2 = buildUser("bbb", "Алексей", "Кузьмиченко");

            userRepository.save(user1);
            userRepository.save(user2);

            var searchUsername = "aaa";
            UserFilterDto userFilterDto = UserFilterDto.builder()
                    .username(searchUsername)
                    .build();

            var result = userRepository.findAll(userFilterDto);

            assertThat(result.size()).isEqualTo(1);
            assertThat(result.get(0).getUsername()).isEqualTo(searchUsername);
        }

        @Test
        void findAllWithLimit() {
            TestDataImporter.importData(entityManager);

            UserFilterDto userFilterDto = UserFilterDto.builder().limit(5).build();

            var result = userRepository.findAll(userFilterDto);

            assertThat(result.size()).isEqualTo(5);
        }
    }

    @Nested
    class PersonalInfoCRUD {
        @Test
        void createAndReadPersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser("lex1", "firstname_lex", "lastname_lex");

            userRepository.save(user);
            personalInfo.setUser(user);
            personalInfoRepository.save(personalInfo);
            entityManager.flush();
            entityManager.detach(personalInfo);

            Optional<PersonalInfo> personalInfoFromDb = personalInfoRepository.findById(personalInfo.getId());

            assertTrue(personalInfoFromDb.isPresent());
            assertThat(personalInfoFromDb.get()).isEqualTo(personalInfo);
        }

        @Test
        void updatePersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser("lex1", "firstname_lex", "lastname_lex");

            var updatedString = "updated_value";
            var updatedDate = LocalDate.of(2012, 12, 12);
            var updatedCategories = List.of(DriverLicenseCategories.M, DriverLicenseCategories.B);

            userRepository.save(user);
            personalInfo.setUser(user);
            personalInfoRepository.save(personalInfo);
            entityManager.flush();

            personalInfo.setDriverLicenseName(updatedString);
            personalInfo.setDriverLicenseSurname(updatedString);
            personalInfo.setDriverLicensePlaceOfBirth(updatedString);
            personalInfo.setDriverLicenseIssuedBy(updatedString);
            personalInfo.setDriverLicenseCode(updatedString);
            personalInfo.setDriverLicenseResidence(updatedString);
            personalInfo.setDateOfBirth(updatedDate);
            personalInfo.setDriverLicenseDateOfIssue(updatedDate);
            personalInfo.setDriverLicenseDateOfExpire(updatedDate);
            personalInfo.setDriverLicenseCategories(updatedCategories);
            personalInfoRepository.update(personalInfo);

            entityManager.detach(personalInfo);
            var personalInfoFromDb = personalInfoRepository.findById(personalInfo.getId());

            assertTrue(personalInfoFromDb.isPresent());
            assertThat(personalInfoFromDb.get().getDriverLicenseName()).isEqualTo(updatedString);
            assertThat(personalInfoFromDb.get().getDriverLicenseSurname()).isEqualTo(updatedString);
            assertThat(personalInfoFromDb.get().getDriverLicensePlaceOfBirth()).isEqualTo(updatedString);
            assertThat(personalInfoFromDb.get().getDriverLicenseIssuedBy()).isEqualTo(updatedString);
            assertThat(personalInfoFromDb.get().getDriverLicenseCode()).isEqualTo(updatedString);
            assertThat(personalInfoFromDb.get().getDriverLicenseResidence()).isEqualTo(updatedString);
            assertThat(personalInfoFromDb.get().getDateOfBirth()).isEqualTo(updatedDate);
            assertThat(personalInfoFromDb.get().getDriverLicenseDateOfIssue()).isEqualTo(updatedDate);
            assertThat(personalInfoFromDb.get().getDriverLicenseDateOfExpire()).isEqualTo(updatedDate);
            assertThat(personalInfoFromDb.get().getDriverLicenseCategories()).isEqualTo(updatedCategories);
        }

        @Test
        void deletePersonalInfo() {
            User user = buildUser("lex1", "firstname_lex", "lastname_lex");
            PersonalInfo personalInfo = buildPersonalInfo();

            userRepository.save(user);
            personalInfo.setUser(user);
            personalInfoRepository.save(personalInfo);
            user.setPersonalInfo(null);
            personalInfoRepository.delete(personalInfo);

            Optional<PersonalInfo> personalInfoFromDb = personalInfoRepository.findById(personalInfo.getId());

            assertTrue(personalInfoFromDb.isEmpty());
        }
    }

    @Nested
    class AvatarCRUD {

        @Test
        void createAndReadAvatar() {
            var user = buildUser("lex1", "firstname_lex", "lastname_lex");
            var avatar = buildMediaItem();

            userRepository.save(user);
            avatar.setUploader(user);
            mediaItemRepository.save(avatar);
            user.setAvatar(avatar);
            userRepository.update(user);
            entityManager.detach(user);

            var userWithAvatar = userRepository.findById(user.getId());

            assertTrue(userWithAvatar.isPresent());
            assertThat(userWithAvatar.get().getAvatarMediaItem()).isNotNull();
        }

        @Test
        void updateAvatar() {
            var user = buildUser("lex1", "firstname_lex", "lastname_lex");
            var avatar = buildMediaItem();
            var updated_avatar = buildMediaItem();

            userRepository.save(user);
            avatar.setUploader(user);
            mediaItemRepository.save(avatar);
            user.setAvatar(avatar);
            userRepository.update(user);

            updated_avatar.setUploader(user);
            mediaItemRepository.save(updated_avatar);
            user.setAvatar(updated_avatar);
            userRepository.update(user);
            entityManager.detach(user);

            mediaItemRepository.delete(avatar);

            var userWithAvatar = userRepository.findById(user.getId());

            assertTrue(userWithAvatar.isPresent());
            assertThat(userWithAvatar.get().getAvatarMediaItem().getId()).isNotEqualTo(avatar.getId());
        }

        @Test
        void deleteAvatar() {
            var user = buildUser("lex1", "firstname_lex", "lastname_lex");
            var avatar = buildMediaItem();

            userRepository.save(user);
            avatar.setUploader(user);
            mediaItemRepository.save(avatar);
            user.setAvatar(avatar);
            userRepository.update(user);

            user.setAvatar(null);
            mediaItemRepository.delete(avatar);

            var deletedAvatar = mediaItemRepository.findById(avatar.getId());

            assertTrue(deletedAvatar.isEmpty());
        }
    }

    private User buildUser(String username, String firstname, String lastname) {
        return User.builder()
                .username(username)
                .firstname(firstname)
                .lastname(lastname)
                .password("qwerty")
                .status(UserStatus.ACTIVE)
                .gender(Gender.MALE)
                .role(Role.SUPER_ADMIN)
                .build();
    }

    private PersonalInfo buildPersonalInfo() {
        return PersonalInfo.builder()
                .driverLicenseName("dl_name")
                .driverLicenseSurname("dl_surname")
                .dateOfBirth(LocalDate.of(2000, 12, 10))
                .driverLicensePlaceOfBirth("dl_place_of_birth")
                .driverLicenseDateOfIssue(LocalDate.of(2020, 5, 5))
                .driverLicenseDateOfExpire(LocalDate.of(2027, 5, 5))
                .driverLicenseIssuedBy("dl_issued_by")
                .driverLicenseCode("0000 000000")
                .driverLicenseResidence("dl_residence")
                .driverLicenseCategories(List.of(DriverLicenseCategories.A, DriverLicenseCategories.B, DriverLicenseCategories.B1))
                .build();
    }

    private MediaItem buildMediaItem() {
        return MediaItem.builder()
                .type(MediaItemType.AVATAR)
                .mimeType("mime_type")
                .link("link")
                .previewLink("preview_link")
                .build();
    }
}
