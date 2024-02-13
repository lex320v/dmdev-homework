package com.example.entity;

import com.example.entity.enums.MediaItemType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MediaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private MediaItemType type;
    @Column(nullable = false)
    private String mimeType;
    @Column(nullable = false)
    private String previewLink;
    @Column(nullable = false)
    private String link;
    @Column(nullable = false)
    private Long uploaderId;
    @Column(nullable = false)
    private Instant createdAt;
}
