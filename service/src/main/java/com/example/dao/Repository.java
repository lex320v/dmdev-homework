package com.example.dao;

import com.example.entity.IdentifiableEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Repository<K extends Serializable, E extends IdentifiableEntity<K>>{

    E save(E entity);

    void delete(K id);

    void update(E entity);

    Optional<E> findById(K id);

    List<E> findAll();
}
