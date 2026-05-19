package com.exercise.postoffice.model.entities;

import com.exercise.postoffice.model.enums.LetterType;
import com.exercise.postoffice.model.enums.UserType;
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

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accepted_letter")
public class AcceptedLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "letter_id")
    private UUID letterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "letter_type")
    private LetterType letterType;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type")
    private UserType senderType;

    @Column(name = "getter_id")
    private UUID getterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "getter_type")
    private UserType getterType;
}
