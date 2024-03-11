package com.bookingcar.repository;

import com.bookingcar.entity.PersonalInfo;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PersonalInfoRepository extends BaseRepository<Long, PersonalInfo> {

    public PersonalInfoRepository(EntityManager entityManager) {
        super(PersonalInfo.class, entityManager);
    }
}
