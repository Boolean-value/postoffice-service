package com.exercise.postoffice.model.entities;

import com.exercise.postoffice.model.enums.OutboxAttachmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_outbox")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "topic_name", nullable = false)
    private String topic;

    @Column(name = "letter_id", nullable = false)
    private UUID letterId;

    @Column(name = "created_datetime", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OutboxAttachmentStatus status;
}
