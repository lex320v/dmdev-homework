package com.bookingcar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @JoinColumn(name = "car_id", referencedColumnName = "id", insertable = false, updatable = false)
//    @MapsId("car_id")
    private Car car;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "media_item_id", referencedColumnName = "id", insertable = false, updatable = false)
//    @MapsId("media_item_id")
    private MediaItem mediaItem;

    @Column(nullable = false)
    private Integer position;
}
