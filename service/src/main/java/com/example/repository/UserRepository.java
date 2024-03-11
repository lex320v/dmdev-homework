package com.example.repository;

import com.example.dto.UserFilterDto;
import com.example.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.entity.QUser.user;

@Repository
public class UserRepository extends BaseRepository<Long, User> {

    private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<User> findAll(UserFilterDto userFilterDto) {
        var predicate = QPredicate.builder()
                .addLikeIgnoreCase(userFilterDto.getFirstname(), user.firstname::likeIgnoreCase)
                .addLikeIgnoreCase(userFilterDto.getLastname(), user.lastname::likeIgnoreCase)
                .addLikeIgnoreCase(userFilterDto.getUsername(), user.username::likeIgnoreCase)
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
