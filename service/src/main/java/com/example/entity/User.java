package com.example.entity;

import com.example.entity.enums.Gender;
import com.example.entity.enums.Role;
import com.example.entity.enums.UserStatus;
import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"personalInfo", "avatarMediaItem", "cars", "mediaItems"})
@EqualsAndHashCode(exclude = {"personalInfo", "avatarMediaItem", "cars", "mediaItems"})
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    private Instant deletedAt;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PersonalInfo personalInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "avatar_media_item_id")
    private MediaItem avatarMediaItem;

    public void setAvatar(MediaItem mediaItem) {
        if (mediaItem != null) {
            mediaItem.setUserAvatar(this);
            this.avatarMediaItem = mediaItem;
        } else {
            this.avatarMediaItem = null;
        }
    }

    @Builder.Default
    @OneToMany(mappedBy = "uploader", cascade = CascadeType.ALL)
    private List<MediaItem> mediaItems = new ArrayList<>();

    public void addMediaItem(MediaItem mediaItem) {
        mediaItems.add(mediaItem);
        mediaItem.setUploader(this);
    }

    @Builder.Default
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Car> cars = new ArrayList<>();

    public void addCar(Car car) {
        cars.add(car);
        car.setOwner(this);
    }

    @OneToMany(mappedBy = "client")
    private List<Request> requests;
}
