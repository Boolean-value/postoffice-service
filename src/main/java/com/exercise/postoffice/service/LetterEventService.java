package com.exercise.postoffice.service;

import com.exercise.postoffice.model.entities.AcceptedLetter;
import com.exercise.postoffice.model.entities.OutboxEvent;
import com.exercise.postoffice.model.enums.OutboxAttachmentStatus;
import com.exercise.postoffice.repository.AcceptedLetterRepository;
import com.exercise.postoffice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterEventService {

    private final AcceptedLetterRepository letterRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Transactional(readOnly = true)
    public List<OutboxEvent> getEvents (OutboxAttachmentStatus status, Pageable pageable) {
        return outboxEventRepository.findByStatusOrderByEventIdAsc(status, pageable);
    }

    @Transactional(readOnly = true)
    public AcceptedLetter getLetter (UUID letterId) {
        return letterRepository.findByLetterId(letterId).orElse(null);
    }

    @Transactional
    public void updateStatus(Long eventId, OutboxAttachmentStatus status) {
        outboxEventRepository.updateStatusByEventId(eventId, status);
    }

    @Transactional
    public int deleteEvents(OutboxAttachmentStatus status, LocalDateTime earlierThan) {
        return outboxEventRepository.deleteOldSent(status, earlierThan);
    }
}
