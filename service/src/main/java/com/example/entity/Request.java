package com.example.entity;

import com.example.entity.enums.RequestStatus;
import jakarta.persistence.CascadeType;
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
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"car", "client", "feedbacks"}, callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Request extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTimeFrom;

    @Column(nullable = false)
    private LocalDateTime dateTimeTo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private String comment;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @Builder.Default
    @OneToMany(mappedBy = "request")
    private List<Feedback> feedbacks = new ArrayList<>();

    public void addFeedback(Feedback feedback) {
        feedbacks.add(feedback);
        feedback.setRequest(this);
    }
}
