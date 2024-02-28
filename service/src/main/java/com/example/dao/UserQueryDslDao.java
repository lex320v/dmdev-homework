package com.example.dao;

import com.example.dto.UserFilterDto;
import com.example.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

import static com.example.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQueryDslDao {

    private static final UserQueryDslDao INSTANCE = new UserQueryDslDao();

    public static UserQueryDslDao getInstance() {
        return INSTANCE;
    }

    public List<User> findUsers(Session session, UserFilterDto userFilterDto) {
        var predicate = QPredicate.builder()
                .addLikeIgnoreCase(userFilterDto.getFirstname(), user.firstname::likeIgnoreCase)
                .addLikeIgnoreCase(userFilterDto.getLastname(), user.lastname::likeIgnoreCase)
                .addLikeIgnoreCase(userFilterDto.getUsername(), user.username::likeIgnoreCase)
                .buildAnd();

        var query = new JPAQuery<User>(session)
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
