package com.example.entity;

import com.example.entity.enums.MediaItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"carToMediaItems"}, callSuper = true)
@EqualsAndHashCode(exclude = {"carToMediaItems"}, callSuper = true)
@Entity
public class MediaItem extends BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private MediaItemType type;

    @Column(nullable = false, updatable = false)
    private String mimeType;

    @Column(nullable = false, updatable = false)
    private String previewLink;

    @Column(nullable = false, updatable = false)
    private String link;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @OneToMany(mappedBy = "mediaItem")
    private List<CarToMediaItem> carToMediaItems;
}
