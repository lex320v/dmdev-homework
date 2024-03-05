package com.example.dao;

import com.example.entity.PersonalInfo;
import jakarta.persistence.EntityManager;

public class PersonalInfoRepository extends BaseRepository<Long, PersonalInfo> {

    public PersonalInfoRepository(EntityManager entityManager) {
        super(PersonalInfo.class, entityManager);
    }
}
