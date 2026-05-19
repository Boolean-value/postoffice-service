package com.exercise.postoffice.service;

import com.exercise.postoffice.model.dto.SendLetter;
import com.exercise.postoffice.model.dto.UserDto;
import com.exercise.postoffice.model.entities.AcceptedLetter;
import com.exercise.postoffice.model.entities.OutboxEvent;
import com.exercise.postoffice.model.enums.OutboxAttachmentStatus;
import com.exercise.postoffice.repository.AcceptedLetterRepository;
import com.exercise.postoffice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterCreateService {

    @Value("${app.kafka.topics.delivery-letter}")
    private String deliveryLetterTopic;

    private final AcceptedLetterRepository letterRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void createLetter(SendLetter sendLetter, UserDto user) {
        AcceptedLetter letter = AcceptedLetter.builder()
                .letterId(sendLetter.letterId())
                .letterType(sendLetter.letterType())
                .senderType(sendLetter.senderType())
                .getterId(user.userId())
                .getterType(user.userType())
                .build();
        letterRepository.save(letter);

        OutboxEvent outboxEvent = OutboxEvent.builder()
                .topic(deliveryLetterTopic)
                .letterId(sendLetter.letterId())
                .createdAt(LocalDateTime.now())
                .status(OutboxAttachmentStatus.PENDING)
                .build();
        outboxEventRepository.save(outboxEvent);
    }
}
