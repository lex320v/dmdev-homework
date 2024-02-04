package com.dmdev.dao;

import com.dmdev.entity.Provider;
import com.dmdev.entity.Status;
import com.dmdev.entity.Subscription;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubscriptionDaoIT extends IntegrationTestBase {

    private final SubscriptionDao subscriptionDao = SubscriptionDao.getInstance();

    @Test
    void findAll() {
        Subscription subscription1 = subscriptionDao.insert(getSubscription(1, "name1"));
        Subscription subscription2 = subscriptionDao.insert(getSubscription(2, "name2"));
        Subscription subscription3 = subscriptionDao.insert(getSubscription(3, "name3"));

        List<Subscription> actualResult = subscriptionDao.findAll();

        assertThat(actualResult).hasSize(3);
        List<Integer> subscriptionIds = actualResult.stream()
                .map(Subscription::getId)
                .toList();
        assertThat(subscriptionIds).contains(subscription1.getId(), subscription2.getId(), subscription3.getId());
    }

    @Test
    void findById() {
        Subscription subscription = subscriptionDao.insert(getSubscription(1, "name1"));

        Optional<Subscription> actualResult = subscriptionDao.findById(subscription.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(subscription);
    }

    @Test
    void shouldNotFindIfIdDoesNotExist() {
        subscriptionDao.insert(getSubscription(1, "name1"));

        Optional<Subscription> actualResult = subscriptionDao.findById(911);

        assertFalse(actualResult.isPresent());
    }

    @Test
    void findByUserId() {
        Subscription subscription1 = subscriptionDao.insert(getSubscription(1, "name1"));
        Subscription subscription2 = subscriptionDao.insert(getSubscription(1, "name2"));

        List<Subscription> actualResult = subscriptionDao.findByUserId(1);

        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0)).isEqualTo(subscription1);
        assertThat(actualResult.get(1)).isEqualTo(subscription2);
    }

    @Test
    void shouldNotFindIfUserIdDoesNotExist() {
        subscriptionDao.insert(getSubscription(1, "name1"));

        List<Subscription> actualResult = subscriptionDao.findByUserId(2);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void insert() {
        Subscription subscription = getSubscription(1, "name1");

        Subscription actualResult = subscriptionDao.insert(subscription);

        assertNotNull(actualResult.getId());
    }

    @Test
    void deleteExistingSubscription() {
        Subscription subscription = subscriptionDao.insert(getSubscription(1, "name1"));

        boolean actualResult = subscriptionDao.delete(subscription.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteNotExistingSubscription() {
        subscriptionDao.insert(getSubscription(1, "name1"));

        boolean actualResult = subscriptionDao.delete(911);

        assertFalse(actualResult);
    }

    @Test
    void update() {
        Subscription subscription = getSubscription(1, "name1");
        subscriptionDao.insert(subscription);
        subscription.setName("updated-name");
        subscription.setProvider(Provider.APPLE);

        subscriptionDao.update(subscription);

        Optional<Subscription> updatedSubscription = subscriptionDao.findById(subscription.getId());
        assertTrue(updatedSubscription.isPresent());
        assertThat(updatedSubscription.get()).isEqualTo(subscription);
    }

    private static Subscription getSubscription(Integer userId, String name) {
        return Subscription.builder()
                .userId(userId)
                .name(name)
                .provider(Provider.GOOGLE)
                .expirationDate(ZonedDateTime.now().plusDays(7).truncatedTo(ChronoUnit.MICROS).toInstant())
                .status(Status.ACTIVE)
                .build();
    }
}