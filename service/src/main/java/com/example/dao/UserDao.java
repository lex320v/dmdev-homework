package com.example.dao;

import com.example.dto.CarDto;
import com.example.entity.Car;
import com.example.entity.Car_;
import com.example.entity.Feedback;
import com.example.entity.Feedback_;
import com.example.entity.PersonalInfo_;
import com.example.entity.Request_;
import com.example.entity.User;
import com.example.entity.User_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public List<User> findAll(Session session) {
        HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
        JpaCriteriaQuery<User> criteria = cb.createQuery(User.class);

        var user = criteria.from(User.class);

        criteria.select(user);

        var result = session.createQuery(criteria).list();

        return result;
    }

    public List<User> findAllByFirstName(Session session, String firstname) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);

        criteria.select(user).where(
                cb.ilike(user.get(User_.firstname), "%" + firstname + "%")
        );

        var result = session.createQuery(criteria).list();

        return result;
    }

    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(User.class);
        var user = criteria.from(User.class);
        var personalInfo = user.join(User_.personalInfo);

        criteria.select(user).orderBy(cb.asc(personalInfo.get(PersonalInfo_.dateOfBirth)));

        var result = session.createQuery(criteria)
                .setMaxResults(limit)
                .list();

        return result;
    }

    public List<Car> findCarsByOwner(Session session, String search) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var cars = criteria.from(Car.class);
        var owner = cars.join(Car_.owner);

        criteria.select(cars).where(
                        cb.or(
                                cb.ilike(owner.get(User_.FIRSTNAME), "%" + search + "%"),
                                cb.ilike(owner.get(User_.LASTNAME), "%" + search + "%")
                        )
                )
                .orderBy(cb.asc(owner.get(User_.createdAt)));

        var result = session.createQuery(criteria).list();

        return result;
    }

    public List<CarDto[]> findCarsByAverageRate(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CarDto[].class);
        var car = criteria.from(Car.class);
        var request = car.join(Car_.requests);
        var feedback = request.join(Request_.feedbacks);

        var subquery = criteria.subquery(Integer.class);
        var feedbackSubquery = subquery.from(Feedback.class);
        var requestSubquery = feedbackSubquery.join(Feedback_.request);

        var sumRateSubquery = subquery
                .select(cb.sum(feedbackSubquery.get(Feedback_.rating)))
                .where(cb.equal(requestSubquery.get(Request_.car).get(Car_.id), car.get(Car_.id)));

        criteria.multiselect(
                        cb.construct(CarDto.class,
                                car.get(Car_.id),
                                car.get(Car_.manufacturer),
                                car.get(Car_.model),
                                // todo: how to set custom field
                                cb.avg(feedback.get(Feedback_.rating)),
                                sumRateSubquery
                        )
                )
                .groupBy(car.get(Car_.id))
                // todo: how to order by custom fields
                .orderBy(cb.desc(cb.avg(feedback.get(Feedback_.rating))));

        var result = session.createQuery(criteria).list();

        System.out.println(result);

        return result;
    }
}
