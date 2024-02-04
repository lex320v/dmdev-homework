package com.dmdev.mapper;

import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class CreateSubscriptionMapperTest {

    private final CreateSubscriptionMapper mapper = CreateSubscriptionMapper.getInstance();

    @Test
    void map() {
        Instant expirationDate = ZonedDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MICROS).toInstant();

        CreateSubscriptionDto dto = CreateSubscriptionDto.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE.name())
                .expirationDate(expirationDate)
                .build();

        Subscription actualResult = mapper.map(dto);

        Subscription expectedResult = Subscription.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE)
                .expirationDate(expirationDate)
                .status(Status.ACTIVE)
                .build();
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}