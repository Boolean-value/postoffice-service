package com.exercise.postoffice.client;

import com.exercise.postoffice.model.dto.UserDto;
import com.exercise.postoffice.model.enums.UserType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kafka-saga-users")
public interface UserRequestFeignClient {

    @GetMapping("/internal/getRandomUser")
    UserDto getRandomUser(@RequestParam UserType userType);
}
