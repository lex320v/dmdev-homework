package com.bookingcar.repository;

import com.bookingcar.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, FilterUserRepository {

    @EntityGraph(attributePaths = {"avatarMediaItem"})
    @Query("""
        select u from users u
        where u.firstname ilike %:firstname% and u.lastname ilike %:lastname%
    """)
    List<User> findAllBy(String firstname, String lastname);

    List<User> findByUsernameContainingIgnoreCase(String username);
}
