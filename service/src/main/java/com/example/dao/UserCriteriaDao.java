package com.example.dao;

import com.example.dto.UserFilterDto;
import com.example.entity.User;
import com.example.entity.User_;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCriteriaDao {

    private static final UserCriteriaDao INSTANCE = new UserCriteriaDao();

    public static UserCriteriaDao getInstance() {
        return INSTANCE;
    }

    public List<User> findUsers(Session session, UserFilterDto userFilterDto) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        if (userFilterDto.getFirstname() != null) {
            predicates.add(cb.ilike(user.get(User_.firstname), "%" + userFilterDto.getFirstname() + "%"));
        }
        if (userFilterDto.getLastname() != null) {
            predicates.add(cb.ilike(user.get(User_.lastname), "%" + userFilterDto.getLastname() + "%"));
        }
        if (userFilterDto.getUsername() != null) {
            predicates.add(cb.ilike(user.get(User_.username), "%" + userFilterDto.getUsername() + "%"));
        }

        criteria.select(user)
                .where(predicates.toArray(Predicate[]::new))
                .orderBy(cb.asc(user.get(User_.createdAt)));

        var query = session.createQuery(criteria);

        if (userFilterDto.getLimit() != null) {
            query.setMaxResults(userFilterDto.getLimit());
        }

        return query.list();
    }
}
