package com.exercise.postoffice.service;

import com.exercise.postoffice.client.UserRequestFeignClient;
import com.exercise.postoffice.model.dto.SendLetter;
import com.exercise.postoffice.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendLetterListener {

    private final LetterCreateService letterCreateService;
    private final UserRequestFeignClient userRequestFeignClient;

    @KafkaListener(
            topics = "${app.kafka.topics.send-letter}",
            containerFactory = "postOfficeKafkaListenerFactory")
    public void sendLetter(SendLetter sendLetter) {
        log.debug("Received letter: {}", sendLetter.letterId());
        UserDto user = userRequestFeignClient.getRandomUser(sendLetter.senderType());
        letterCreateService.createLetter(sendLetter, user);
    }
}
