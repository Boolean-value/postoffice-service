package com.exercise.postoffice.repository;

import com.exercise.postoffice.model.entities.AcceptedLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AcceptedLetterRepository extends JpaRepository<AcceptedLetter, Long> {
    Optional<AcceptedLetter> findByLetterId(UUID letterId);
}
