package com.bookingcar.integration.http.controller;

import com.bookingcar.BaseIntegrationTest;
import com.bookingcar.util.TestDataImporter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.bookingcar.dto.UserCreateEditDto.Fields.birthDate;
import static com.bookingcar.dto.UserCreateEditDto.Fields.firstname;
import static com.bookingcar.dto.UserCreateEditDto.Fields.gender;
import static com.bookingcar.dto.UserCreateEditDto.Fields.lastname;
import static com.bookingcar.dto.UserCreateEditDto.Fields.password;
import static com.bookingcar.dto.UserCreateEditDto.Fields.role;
import static com.bookingcar.dto.UserCreateEditDto.Fields.username;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final EntityManager entityManager;

    @Test
    void findAll() throws Exception {
        TestDataImporter.importData(entityManager);

        mockMvc.perform(get("/users"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(view().name("user/users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attribute("users", hasSize(10)));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(
                post("/users")
                        .param(username, "username_test")
                        .param(password, "test")
                        .param(firstname, "test")
                        .param(lastname, "test_test")
                        .param(role, "ADMIN")
                        .param(gender, "MALE")
                        .param(birthDate, "2000-01-01")
        )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/users/*")
                );
    }
}
