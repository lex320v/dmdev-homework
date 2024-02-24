package com.example.dao;

import com.example.dto.UserFilterDto;
import com.example.entity.Car;
import com.example.entity.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

import static com.example.entity.QCar.car;
import static com.example.entity.QFeedback.feedback;
import static com.example.entity.QRequest.request;
import static com.example.entity.QUser.user;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserQueryDslDao {
    private static final UserQueryDslDao INSTANCE = new UserQueryDslDao();

    public static UserQueryDslDao getInstance() {
        return INSTANCE;
    }


    public List<User> findUsers(Session session, UserFilterDto userFilter, int limit) {

        var predicate = QPredicate.builder()
                .add(userFilter.getFirstname(), user.firstname::eq)
                .add(userFilter.getLastname(), user.lastname::eq)
                .buildAnd();

        var result = new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .join(user.personalInfo)
                .where(predicate)
                .orderBy(user.personalInfo.dateOfBirth.desc())
                .limit(limit)
                .fetch();

        System.out.println(result);

        return result;
    }

    public List<Tuple> findCars(Session session) {

        var result = new JPAQuery<Car>(session)
                .select(
                        car,
                        feedback.rating.avg().as("ttt"),
                        feedback.rating.sum()
//                        new JPAQuery<Integer>(session)
//                                .select(feedback.rating.sum())
//                                .from(feedback)
//                                .join(feedback.request, request)
//                                .where(request.car.id.eq(car.id))
                )
                .from(car)
                .join(car.requests, request)
                .join(request.feedbacks, feedback)
                .groupBy(car.id)
                .orderBy()
//                .orderBy(car.createdAt.asc())
                .fetch();

        System.out.println(result);

        return result;
    }
}
