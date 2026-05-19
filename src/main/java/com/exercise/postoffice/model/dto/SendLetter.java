package com.exercise.postoffice.model.dto;

import com.exercise.postoffice.model.enums.LetterType;
import com.exercise.postoffice.model.enums.UserType;

import java.util.UUID;

public record SendLetter(
        UUID letterId,
        LetterType letterType,
        UserType senderType
) {
}
