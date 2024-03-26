package com.bookingcar.repository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

@FunctionalInterface
public interface CriteriaPredicateFunction {
  Predicate apply(Expression<String> x, String string);
}
