package com.bookingcar.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaPredicate {

    private final List<Predicate> predicates = new ArrayList<>();

    public static CriteriaPredicate builder() {
        return new CriteriaPredicate();
    }

    public CriteriaPredicate add(String search, Expression<String> field, CriteriaPredicateFunction function) {
        if (search != null) {
            predicates.add(function.apply(field, search));
        }

        return this;
    }

    public CriteriaPredicate addILike(
            String search,
            Expression<String> field,
            CriteriaBuilder cb
    ) {
        if (search != null) {
            var predicate = cb.like(cb.upper(field), "%" + search.toUpperCase() + "%" );
            predicates.add(predicate);
        }

        return this;
    }

    public Predicate[] build() {
        return predicates.toArray(Predicate[]::new);
    }
}
