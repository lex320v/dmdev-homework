package com.example.entity;

import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "cars")
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstname;

    private String lastname;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    private Instant deletedAt;

    @Column(nullable = false)
    private Instant createdAt;

    @PrePersist
    private void init() {
        createdAt = Instant.now();
    }

    @OneToOne(mappedBy = "user")
    private PersonalInfo personalInfo;

    @OneToOne
    @JoinColumn(name = "avatar_media_item_id")
    private MediaItem avatarMediaItem;

    public void setAvatar(MediaItem mediaItem) {
        mediaItem.setUser(this);
        this.avatarMediaItem = mediaItem;
    }

    @OneToMany(mappedBy = "uploader")
    private List<MediaItem> mediaItems;

    @OneToMany(mappedBy = "owner")
    private List<Car> cars;

    @OneToMany(mappedBy = "client")
    private List<Request> requests;
}
