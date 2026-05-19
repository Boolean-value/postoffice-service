package com.exercise.postoffice.service;

import com.exercise.postoffice.model.dto.DeliveryLetter;
import com.exercise.postoffice.model.entities.AcceptedLetter;
import com.exercise.postoffice.model.entities.OutboxEvent;
import com.exercise.postoffice.model.enums.OutboxAttachmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.RetriableException;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxScheduler {

    private final LetterEventService letterEventService;
    private final KafkaTemplate<String, DeliveryLetter> deliveryLetterKafkaTemplate;

    @Scheduled(fixedDelayString = "${app.outbox.scheduler.delay}")
    public void processOutbox() {
        Pageable pageable = Pageable.ofSize(200);
        List<OutboxEvent> events = letterEventService
                .getEvents(OutboxAttachmentStatus.PENDING, pageable);
        if (events.isEmpty()) {
            return;
        }

        for (OutboxEvent event : events) {
            try {
                processSingleEvent(event);
            } catch (InterruptedException e) {
                log.error("[processOutbox] abort cycle, process is shutdown...");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    @Scheduled(fixedDelayString = "${app.outbox.scheduler.expired}")
    public void deleteOldEventsOutbox() {
        log.debug("[deleteOldEventsOutbox] cleanup started");

        int deleted = letterEventService.deleteEvents(
                OutboxAttachmentStatus.SENT,
                LocalDateTime.now().minusHours(24));

        log.info("[deleteOldEventsOutbox] deleted event records: {}", deleted);
    }

    private void processSingleEvent(OutboxEvent event) throws InterruptedException {
        try {
            AcceptedLetter letter = letterEventService.getLetter(event.getLetterId());
            if (letter == null) {
                log.warn("[processSingleEvent] letter not found for event {}", event);
                letterEventService.updateStatus(
                        event.getEventId(),
                        OutboxAttachmentStatus.FAILED);
                return;
            }

            DeliveryLetter payload = DeliveryLetter.builder()
                    .letterId(letter.getLetterId())
                    .letterType(letter.getLetterType())
                    .senderType(letter.getSenderType())
                    .getterId(letter.getGetterId())
                    .getterType(letter.getGetterType())
                    .build();

            ProducerRecord<String, DeliveryLetter> record = new ProducerRecord<>(event.getTopic(), payload);
            record.headers().add(new RecordHeader("TYPE", "CREATE".getBytes()));

            deliveryLetterKafkaTemplate.send(record).get();

            letterEventService.updateStatus(event.getEventId(), OutboxAttachmentStatus.SENT);

        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RetriableException) {
                log.warn("[processOutbox] temporary Kafka error for event {}: {}",
                        event.getEventId(), cause.getMessage());
            } else {
                log.error("[processOutbox] fatal error sending event {}. Marking as FAILED",
                        event.getEventId(), cause);
                letterEventService.updateStatus(
                        event.getEventId(),
                        OutboxAttachmentStatus.FAILED);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        } catch (Exception e) {
            log.error("[processOutbox] unknown error", e);
        }
    }
}
