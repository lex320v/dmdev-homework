package com.dmdev.service;

import com.dmdev.dao.SubscriptionDao;
import com.dmdev.dto.CreateSubscriptionDto;
import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.exception.SubscriptionException;
import com.dmdev.exception.ValidationException;
import com.dmdev.mapper.CreateSubscriptionMapper;
import com.dmdev.validator.CreateSubscriptionValidator;
import com.dmdev.validator.Error;
import com.dmdev.validator.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class SubscriptionServiceTest {

    private final Instant expirationDateValid =
            ZonedDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MICROS).toInstant();
    private final Instant expirationDateInvalid =
            ZonedDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MICROS).toInstant();

    private CreateSubscriptionValidator createSubscriptionValidator;
    private SubscriptionDao subscriptionDao;
    private CreateSubscriptionMapper createSubscriptionMapper;
    private SubscriptionService subscriptionService;

    @BeforeEach
    void prepare() {
        createSubscriptionValidator = Mockito.mock(CreateSubscriptionValidator.class);
        subscriptionDao = Mockito.mock(SubscriptionDao.class);
        createSubscriptionMapper = Mockito.mock(CreateSubscriptionMapper.class);
        subscriptionService = new SubscriptionService(
                subscriptionDao,
                createSubscriptionMapper,
                createSubscriptionValidator,
                Clock.systemDefaultZone()
        );
    }

    @Test
    void upsertNewSubscription() {
        CreateSubscriptionDto subscriptionDto = getSubscriptionDto(expirationDateValid);
        Subscription subscription = getSubscription(expirationDateValid);
        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(subscriptionDto);
        doReturn(Collections.emptyList()).when(subscriptionDao).findByUserId(subscriptionDto.getUserId());
        doReturn(subscription).when(createSubscriptionMapper).map(subscriptionDto);
        doReturn(subscription).when(subscriptionDao).upsert(subscription);

        Subscription actualResult = subscriptionService.upsert(subscriptionDto);

        assertThat(actualResult).isEqualTo(subscription);
    }

    @Test
    void upsertExistingSubscription() {
        CreateSubscriptionDto subscriptionDto = getSubscriptionDto(expirationDateValid);
        Subscription subscription = getSubscription(expirationDateValid);
        doReturn(new ValidationResult()).when(createSubscriptionValidator).validate(subscriptionDto);
        doReturn(Collections.singletonList(subscription)).when(subscriptionDao).findByUserId(subscriptionDto.getUserId());
        doReturn(subscription).when(subscriptionDao).upsert(subscription);

        Subscription actualResult = subscriptionService.upsert(subscriptionDto);

        assertThat(actualResult).isEqualTo(subscription);
        verifyNoInteractions(createSubscriptionMapper);
    }

    @Test
    void upsertShouldThrowExceptionIfSubscriptionDtoInvalid() {
        CreateSubscriptionDto subscriptionDto = getSubscriptionDto(expirationDateInvalid);
        ValidationResult validationResult = new ValidationResult();
        validationResult.add(Error.of(103, "expirationDate is invalid"));
        doReturn(validationResult).when(createSubscriptionValidator).validate(subscriptionDto);

        assertThrows(ValidationException.class, () -> subscriptionService.upsert(subscriptionDto));
        verifyNoInteractions(subscriptionDao, createSubscriptionMapper);
    }

    @Test
    void cancelSuccess() {
        Subscription subscription = getSubscription(expirationDateValid);
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(anyInt());

        subscriptionService.cancel(anyInt());

        assertThat(subscription.getStatus()).isEqualTo(Status.CANCELED);
        verify(subscriptionDao).update(subscription);
    }

    @Test
    void cancelShouldThrowIllegalArgumentException() {
        Subscription subscription = getSubscription(expirationDateValid);
        doReturn(Optional.empty()).when(subscriptionDao).findById(anyInt());

        assertThrows(IllegalArgumentException.class, () -> subscriptionService.cancel(anyInt()));
        verify(subscriptionDao, never()).update(subscription);
    }

    @Test
    void cancelShouldThrowSubscriptionException() {
        Subscription subscription = Subscription.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE)
                .expirationDate(expirationDateValid)
                .status(Status.CANCELED)
                .build();
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(anyInt());

        assertThrows(SubscriptionException.class, () -> subscriptionService.cancel(anyInt()));
        verify(subscriptionDao, never()).update(subscription);
    }

    @Test
    void expireSuccess() {
        Instant currentTime = ZonedDateTime.now().toInstant();
        Subscription subscription = getSubscription(currentTime);
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(anyInt());

        subscriptionService.expire(anyInt());

        assertThat(subscription.getStatus()).isEqualTo(Status.EXPIRED);
        assertThat(subscription.getExpirationDate()).isAfter(currentTime);
        verify(subscriptionDao).update(subscription);
    }

    @Test
    void expireShouldThrowIllegalArgumentException() {
        Subscription subscription = getSubscription(expirationDateValid);
        doReturn(Optional.empty()).when(subscriptionDao).findById(anyInt());

        assertThrows(IllegalArgumentException.class, () -> subscriptionService.expire(anyInt()));
        verify(subscriptionDao, never()).update(subscription);
    }

    @Test
    void expireShouldThrowSubscriptionException() {
        Subscription subscription = Subscription.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE)
                .expirationDate(expirationDateValid)
                .status(Status.EXPIRED)
                .build();
        doReturn(Optional.of(subscription)).when(subscriptionDao).findById(anyInt());

        assertThrows(SubscriptionException.class, () -> subscriptionService.expire(anyInt()));
        verify(subscriptionDao, never()).update(subscription);
    }

    private Subscription getSubscription(Instant expirationDate) {
        return Subscription.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE)
                .expirationDate(expirationDate)
                .status(Status.ACTIVE)
                .build();
    }

    private CreateSubscriptionDto getSubscriptionDto(Instant expirationDate) {
        return CreateSubscriptionDto.builder()
                .userId(911)
                .name("Lex")
                .provider(Provider.GOOGLE.name())
                .expirationDate(expirationDate)
                .build();
    }
}