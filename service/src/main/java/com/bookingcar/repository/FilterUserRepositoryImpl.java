package com.bookingcar.repository;

import com.bookingcar.dto.UserFilterDto;
import com.bookingcar.entity.User;
import com.bookingcar.entity.User_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {

    private final EntityManager entityManager;

    @Override
    public List<User> findAllByFilter(UserFilterDto userFilterDto) {
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
}
