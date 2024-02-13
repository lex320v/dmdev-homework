package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Entity(name = "car_to_media_item")
public class CarToMediaItem {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "car_id")
    private Car car;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "media_item_id")
    private MediaItem mediaItem;
}
