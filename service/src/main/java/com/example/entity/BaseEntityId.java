package com.example.entity;

import java.io.Serializable;

public interface BaseEntityId<T extends Serializable> {

    void setId(T id);

    T getId();
}
