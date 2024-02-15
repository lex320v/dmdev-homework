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
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"carToMediaItems"})
@EqualsAndHashCode(exclude = {"carToMediaItems"})
@Entity
public class MediaItem {

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

    @CreationTimestamp
    private Instant createdAt;

    @OneToOne(mappedBy = "avatarMediaItem")
    private User userAvatar;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private User uploader;

    @OneToMany(mappedBy = "mediaItem")
    private List<CarToMediaItem> carToMediaItems;
}
