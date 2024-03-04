package com.example;

import com.example.dao.MediaItemRepository;
import com.example.dao.PersonalInfoRepository;
import com.example.dao.UserRepository;
import com.example.entity.MediaItem;
import com.example.entity.PersonalInfo;
import com.example.entity.User;
import com.example.entity.enums.DriverLicenseCategories;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserIT {

    private static SessionFactory sessionFactory;
    private static Session session;

    private UserRepository userRepository;
    private PersonalInfoRepository personalInfoRepository;
    private MediaItemRepository mediaItemRepository;

    @BeforeAll
    static void init() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @BeforeEach
    public void prepare() {
        session = sessionFactory.getCurrentSession();
        userRepository = new UserRepository(session);
        personalInfoRepository = new PersonalInfoRepository(session);
        mediaItemRepository = new MediaItemRepository(session);
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
    class UserCRUD {
        @Test
        void createAndReadUser() {
            var user = buildUser();

            var savedUser = userRepository.save(user);
            session.evict(user);
            var userFromDb = userRepository.findById(user.getId());

            assertNotNull(savedUser.getId());
            assertTrue(userFromDb.isPresent());
            assertThat(userFromDb.get()).isEqualTo(user);
        }

        @Test
        void updateUser() {
            var user = buildUser();

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

            session.evict(user);
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
            User user = buildUser();

            userRepository.save(user);
            userRepository.delete(user);

            Optional<User> userFromDb = userRepository.findById(user.getId());

            assertTrue(userFromDb.isEmpty());
        }
    }

    @Nested
    class PersonalInfoCRUD {
        @Test
        void createAndReadPersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser();

            userRepository.save(user);
            personalInfo.setUser(user);
            personalInfoRepository.save(personalInfo);
            session.flush();
            session.evict(personalInfo);

            Optional<PersonalInfo> personalInfoFromDb = personalInfoRepository.findById(personalInfo.getId());

            assertTrue(personalInfoFromDb.isPresent());
            assertThat(personalInfoFromDb.get()).isEqualTo(personalInfo);
        }

        @Test
        void updatePersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser();

            var updatedString = "updated_value";
            var updatedDate = LocalDate.of(2012, 12, 12);
            var updatedCategories = List.of(DriverLicenseCategories.M, DriverLicenseCategories.B);

            userRepository.save(user);
            personalInfo.setUser(user);
            personalInfoRepository.save(personalInfo);
            session.flush();

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

            session.evict(personalInfo);
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
            User user = buildUser();
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
            var user = buildUser();
            var avatar = buildMediaItem();

            userRepository.save(user);
            avatar.setUploader(user);
            mediaItemRepository.save(avatar);
            user.setAvatar(avatar);
            userRepository.update(user);
            session.evict(user);

            var userWithAvatar = userRepository.findById(user.getId());

            assertTrue(userWithAvatar.isPresent());
            assertThat(userWithAvatar.get().getAvatarMediaItem()).isNotNull();
        }

        @Test
        void updateAvatar() {
            var user = buildUser();
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
            session.evict(user);

            mediaItemRepository.delete(avatar);

            var userWithAvatar = userRepository.findById(user.getId());

            assertTrue(userWithAvatar.isPresent());
            assertThat(userWithAvatar.get().getAvatarMediaItem().getId()).isNotEqualTo(avatar.getId());
        }

        @Test
        void deleteAvatar() {
            var user = buildUser();
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

    private User buildUser() {
        return User.builder()
                .username("lex1")
                .firstname("firstname_lex")
                .lastname("lastname_lex")
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
