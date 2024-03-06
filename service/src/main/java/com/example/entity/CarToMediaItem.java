package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CarToMediaItem implements IdentifiableEntity<CarToMediaItemId> {

    @EmbeddedId
    private CarToMediaItemId id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("car_id")
    private Car car;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("media_item_id")
    private MediaItem mediaItem;

    @Column(nullable = false)
    private Integer position;
}
