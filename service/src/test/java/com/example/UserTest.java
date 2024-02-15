package com.example;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTest {

    SessionFactory sessionFactory;
    Session openedSession;

    @BeforeEach
    public void prepare() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        openedSession = sessionFactory.openSession();
    }

    @Nested
    class userCRUD {
        @Test
        void createUser() {
            var user = buildUser();

            openedSession.beginTransaction();
            openedSession.persist(user);
            openedSession.getTransaction().commit();

            assertNotNull(user.getId());
        }

        @Test
        void readUser() {
            var user = buildUser();

            openedSession.beginTransaction();
            openedSession.persist(user);
            openedSession.getTransaction().commit();
            User userFromDb = openedSession.get(User.class, user.getId());

            assertThat(userFromDb).isEqualTo(user);
        }

        @Test
        void updateUser() {
            var user = buildUser();

            var updatedString = "updated_value";
            var updatedGender = Gender.FEMALE;
            var updatedRole = Role.CLIENT;

            sessionFactory.inTransaction(session -> {
                session.persist(user);

                user.setUsername(updatedString);
                user.setFirstname(updatedString);
                user.setLastname(updatedString);
                user.setGender(updatedGender);
                user.setRole(updatedRole);
                user.setPassword(updatedString);
            });

            assertThat(user.getUsername()).isEqualTo(updatedString);
            assertThat(user.getFirstname()).isEqualTo(updatedString);
            assertThat(user.getLastname()).isEqualTo(updatedString);
            assertThat(user.getGender()).isEqualTo(updatedGender);
            assertThat(user.getRole()).isEqualTo(updatedRole);
            assertThat(user.getPassword()).isEqualTo(updatedString);
        }

        @Test
        void deleteUser() {
            User user = buildUser();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                session.remove(user);
            });

            User userFromDb = openedSession.get(User.class, user.getId());

            assertThat(userFromDb).isNull();
        }
    }

    @Nested
    class personalInfoCRUD {
        @Test
        void createPersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                personalInfo.setUser(user);
            });

            assertThat(personalInfo.getId()).isNotNull();
        }

        @Test
        void readPersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                personalInfo.setUser(user);
            });

            openedSession.get(PersonalInfo.class, personalInfo.getId());

            System.out.println(personalInfo);
            assertThat(personalInfo).isNotNull();
        }

        @Test
        void updatePersonalInfo() {
            PersonalInfo personalInfo = buildPersonalInfo();
            User user = buildUser();

            var updatedString = "updated_value";
            var updatedDate = LocalDate.of(2012, 12, 12);
            var updatedCategories = List.of(DriverLicenseCategories.M, DriverLicenseCategories.B);

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                personalInfo.setUser(user);

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
            });

            assertThat(personalInfo.getDriverLicenseName()).isEqualTo(updatedString);
            assertThat(personalInfo.getDriverLicenseSurname()).isEqualTo(updatedString);
            assertThat(personalInfo.getDriverLicensePlaceOfBirth()).isEqualTo(updatedString);
            assertThat(personalInfo.getDriverLicenseIssuedBy()).isEqualTo(updatedString);
            assertThat(personalInfo.getDriverLicenseCode()).isEqualTo(updatedString);
            assertThat(personalInfo.getDriverLicenseResidence()).isEqualTo(updatedString);
            assertThat(personalInfo.getDateOfBirth()).isEqualTo(updatedDate);
            assertThat(personalInfo.getDriverLicenseDateOfIssue()).isEqualTo(updatedDate);
            assertThat(personalInfo.getDriverLicenseDateOfExpire()).isEqualTo(updatedDate);
            assertThat(personalInfo.getDriverLicenseCategories()).isEqualTo(updatedCategories);
        }

        @Test
        void deletePersonalInfo() {
            User user = buildUser();
            PersonalInfo personalInfo = buildPersonalInfo();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                personalInfo.setId(user.getId());
                session.persist(personalInfo);
                session.remove(personalInfo);
            });

            PersonalInfo personalInfoFromDb = openedSession.get(PersonalInfo.class, personalInfo.getId());

            assertThat(personalInfoFromDb).isNull();
        }
    }

    @Nested
    class avatarCRUD {

        @Test
        void createAndGetAvatar() {
            var user = buildUser();
            var avatar = buildMediaItem();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                avatar.setUploader(user);
                user.setAvatar(avatar);
            });

            var userWithAvatar = openedSession.get(User.class, user.getId());

            assertThat(userWithAvatar.getAvatarMediaItem()).isNotNull();
        }

        @Test
        void updateAvatar() {
            var user = buildUser();
            var avatar = buildMediaItem();
            var updated_avatar = buildMediaItem();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                avatar.setUploader(user);
                user.setAvatar(avatar);
                session.flush();

                updated_avatar.setUploader(user);
                user.setAvatar(updated_avatar);
                session.remove(avatar);
            });

            var userWithAvatar = openedSession.get(User.class, user.getId());

            assertThat(userWithAvatar.getAvatarMediaItem().getId()).isNotEqualTo(avatar.getId());
        }

        @Test
        void deleteAvatar() {
            var user = buildUser();
            var avatar = buildMediaItem();

            sessionFactory.inTransaction(session -> {
                session.persist(user);
                avatar.setUploader(user);
                user.setAvatar(avatar);
                session.flush();

                user.setAvatar(null);
                session.remove(avatar);
            });

            var userWithAvatar = openedSession.get(User.class, user.getId());

            assertThat(userWithAvatar.getAvatarMediaItem()).isNull();
        }
    }

    @AfterEach
    void closeConnection() {
        openedSession.close();
        sessionFactory.close();
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
