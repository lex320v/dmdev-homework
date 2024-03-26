package com.bookingcar.repository;

import com.bookingcar.dto.UserFilterDto;
import com.bookingcar.entity.User;
import com.bookingcar.entity.User_;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.bookingcar.entity.QUser.user;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilterCriteria(UserFilterDto userFilterDto) {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        var predicates = CriteriaPredicate.builder()
                .add(userFilterDto.getFirstname(), user.get(User_.firstname), (a, b) -> cb.like(cb.upper(a), "%" + b.toUpperCase() + "%" ))
                .add(userFilterDto.getLastname(), user.get(User_.lastname), (a, b) -> cb.like(cb.upper(a), "%" + b.toUpperCase() + "%" ))
                .add(userFilterDto.getUsername(), user.get(User_.username), (a, b) -> cb.like(cb.upper(a), "%" + b.toUpperCase() + "%" ))
                .build();

        criteria.select(user)
                .where(predicates)
                .orderBy(cb.asc(user.get(User_.createdAt)));

        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public List<User> findAllByFilterQueryDsl(UserFilterDto userFilterDto) {
        var predicate = QPredicate.builder()
                .add(userFilterDto.getFirstname(), user.firstname::containsIgnoreCase)
                .add(userFilterDto.getLastname(), user.lastname::containsIgnoreCase)
                .add(userFilterDto.getUsername(), user.username::containsIgnoreCase)
                .buildAnd();

        return new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.createdAt.asc())
                .fetch();
    }
}
