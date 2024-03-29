package com.bookingcar.repository;

import com.bookingcar.entity.IdentifiableEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public interface Repository<K extends Serializable, E extends IdentifiableEntity<K>>{

    E save(E entity);

    void delete(E entity);

    void update(E entity);

    default Optional<E> findById(K id) {
        return findById(id, emptyMap());
    };

    Optional<E> findById(K id, Map<String, Object> properties);

    List<E> findAll();
}
