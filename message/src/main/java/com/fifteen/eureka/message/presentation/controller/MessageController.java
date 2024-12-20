package com.fifteen.eureka.message.presentation.controller;

import com.fifteen.eureka.common.response.ApiResponse;
import com.fifteen.eureka.common.response.ResSuccessCode;
import com.fifteen.eureka.common.role.RoleCheck;
import com.fifteen.eureka.message.application.dtos.MessageCreateRequestDto;
import com.fifteen.eureka.message.application.dtos.MessageUpdateRequestDto;
import com.fifteen.eureka.message.application.service.MessageService;
import com.fifteen.eureka.message.presentation.dtos.request.MessageCreateRequest;
import com.fifteen.eureka.message.presentation.dtos.request.MessageUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping()
    public ApiResponse<?> createMessage(
            @Valid @RequestBody MessageCreateRequest messageCreateRequest) {

        log.info("메시지 생성 URL 매핑 : OK");
        messageService.createMessage(MessageCreateRequestDto.from(messageCreateRequest));
        return ApiResponse.OK(ResSuccessCode.CREATED);
    }

    @RoleCheck({"ROLE_ADMIN_MASTER"})
    @GetMapping()
    public ApiResponse<?> getMessages(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String search) {

        log.info("메시지 조회 URL 매핑 : OK");
        return ApiResponse.OK(ResSuccessCode.SUCCESS,messageService.getMessages(pageable,search));
    }

    @RoleCheck({"ROLE_ADMIN_MASTER"})
    @PatchMapping("/{messageId}")
    public ApiResponse<?> updateMessage(
            @Valid @RequestBody MessageUpdateRequest messageUpdateRequest,
            @PathVariable UUID messageId) {

        log.info("메시지 수정 URL 매핑 : OK");
        messageService.updateMessage(MessageUpdateRequestDto.from(messageUpdateRequest),messageId);
        return ApiResponse.OK(ResSuccessCode.UPDATED);
    }

    @RoleCheck({"ROLE_ADMIN_MASTER"})
    @DeleteMapping("/{messageId}")
    public ApiResponse<?> deleteMessage(@PathVariable UUID messageId) {

        log.info("메시지 삭제 URL 매핑 : OK");
        messageService.deleteMessage(messageId);
        return ApiResponse.OK(ResSuccessCode.DELETED);
    }
}
