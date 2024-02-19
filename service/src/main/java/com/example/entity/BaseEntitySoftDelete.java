package com.example.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;

@ToString
@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntitySoftDelete<T extends Serializable> extends BaseEntity<T> implements BaseEntityId<T>  {

    private Instant deletedAt;
}
