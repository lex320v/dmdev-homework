package com.example.entity;

import java.io.Serializable;

public interface BaseEntityWithId<T extends Serializable> {

    void setId(T id);

    T getId();
}
