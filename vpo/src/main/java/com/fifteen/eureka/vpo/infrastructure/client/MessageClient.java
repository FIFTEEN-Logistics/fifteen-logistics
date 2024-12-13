package com.fifteen.eureka.vpo.infrastructure.client;

import com.fifteen.eureka.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "message-service")
public interface MessageClient {

    @PostMapping()
    public ApiResponse<?> createMessage(
            @Valid @RequestBody MessageCreateRequest messageCreateRequest);
}
