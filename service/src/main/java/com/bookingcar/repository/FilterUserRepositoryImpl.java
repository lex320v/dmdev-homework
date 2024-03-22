package com.bookingcar.repository;

import com.bookingcar.dto.UserFilterDto;
import com.bookingcar.entity.User;
import com.bookingcar.entity.User_;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
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

        List<Predicate> predicates = new ArrayList<>();
        if (userFilterDto.getFirstname() != null) {
            predicates.add(cb.like(user.get(User_.firstname), "%" + userFilterDto.getFirstname() + "%"));
        }
        if (userFilterDto.getLastname() != null) {
            predicates.add(cb.like(user.get(User_.lastname), "%" + userFilterDto.getLastname() + "%"));
        }
        if (userFilterDto.getUsername() != null) {
            predicates.add(cb.like(user.get(User_.username), "%" + userFilterDto.getUsername() + "%"));
        }

        criteria.select(user)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(cb.asc(user.get(User_.createdAt)));

        var query = entityManager.createQuery(criteria);

        if (userFilterDto.getLimit() != null) {
            query.setMaxResults(userFilterDto.getLimit());
        }

        return query.getResultList();
    }

    @Override
    public List<User> findAllByFilterQueryDsl(UserFilterDto userFilterDto) {
        var predicate = QPredicate.builder()
                .add(userFilterDto.getFirstname(), user.firstname::containsIgnoreCase)
                .add(userFilterDto.getLastname(), user.lastname::containsIgnoreCase)
                .add(userFilterDto.getUsername(), user.username::containsIgnoreCase)
                .buildAnd();

        var query = new JPAQuery<User>(entityManager)
                .select(user)
                .from(user)
                .where(predicate)
                .orderBy(user.createdAt.asc());

        if (userFilterDto.getLimit() != null) {
            query.limit(userFilterDto.getLimit());
        }

        return query.fetch();
    }
}
