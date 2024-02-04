package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionServiceIT extends IntegrationTestBase {

    private final Instant expirationDate =
            ZonedDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MICROS).toInstant();
    private SubscriptionDao subscriptionDao;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void prepare() {
        subscriptionDao = SubscriptionDao.getInstance();

        subscriptionService = new SubscriptionService(
                subscriptionDao,
                CreateSubscriptionMapper.getInstance(),
                CreateSubscriptionValidator.getInstance(),
                Clock.systemDefaultZone()
        );
    }

    @Test
    void upsertCheckInsert() {
        Subscription actualResult = subscriptionService.upsert(getSubscriptionDto());

        assertNotNull(actualResult.getId());
    }

    @Test
    void upsertCheckUpdate() {
        Subscription subscription = subscriptionService.upsert(getSubscriptionDto());
        CreateSubscriptionDto subscriptionDto = CreateSubscriptionDto.builder()
                .userId(subscription.getUserId())
                .name(subscription.getName())
                .provider(subscription.getProvider().name())
                .expirationDate(ZonedDateTime.now().plusDays(30).truncatedTo(ChronoUnit.MICROS).toInstant())
                .build();

        Subscription updatedSubscription = subscriptionService.upsert(subscriptionDto);

        assertThat(subscription.getId()).isEqualTo(updatedSubscription.getId());
        assertThat(updatedSubscription.getExpirationDate()).isEqualTo(subscriptionDto.getExpirationDate());
    }

    @Test
    void cancel() {
        Subscription subscription = subscriptionService.upsert(getSubscriptionDto());

        subscriptionService.cancel(subscription.getId());

        Optional<Subscription> updatedSubscription = subscriptionDao.findById(subscription.getId());
        assertTrue(updatedSubscription.isPresent());
        assertThat(updatedSubscription.get().getStatus()).isEqualTo(Status.CANCELED);
    }

    @Test
    void expire() {
        Subscription subscription = subscriptionService.upsert(getSubscriptionDto());

        subscriptionService.expire(subscription.getId());

        Optional<Subscription> updatedSubscription = subscriptionDao.findById(subscription.getId());
        assertTrue(updatedSubscription.isPresent());
        assertThat(updatedSubscription.get().getStatus()).isEqualTo(Status.EXPIRED);
    }

    private CreateSubscriptionDto getSubscriptionDto() {
        return CreateSubscriptionDto.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE.name())
                .expirationDate(expirationDate)
                .build();
    }
}
