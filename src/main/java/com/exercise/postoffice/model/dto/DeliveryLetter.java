package com.exercise.postoffice.model.dto;

import com.exercise.postoffice.model.enums.LetterType;
import com.exercise.postoffice.model.enums.UserType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DeliveryLetter(
        UUID letterId,
        LetterType letterType,
        UserType senderType,
        UUID getterId,
        UserType getterType
) {
}
