package com.example.dao;

import com.example.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    public List<User> findAll(Session session) {
        return session.createQuery("select u from users u", User.class).list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
