package com.exercise.postoffice.model.dto;

import com.exercise.postoffice.model.enums.UserType;

import java.util.UUID;

public record UserDto(
        UUID userId,
        UserType userType
) {
}
