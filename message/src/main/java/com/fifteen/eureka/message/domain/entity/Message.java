package com.fifteen.eureka.message.domain.entity;

import com.fifteen.eureka.common.auditor.BaseEntity;
import com.fifteen.eureka.message.infrastructure.util.MessageUtil;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_slack_message")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private String messsageId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "message",nullable = false, columnDefinition="TEXT")
    private String message;

    @Column(name = "send_time", nullable = false)
    private LocalDateTime sendTime;

    public static Message create(Long receiverId, String message, LocalDateTime sendTime) {
        return Message.builder()
                .receiverId(receiverId)
                .message(message)
                .sendTime(sendTime)
                .build();
    }

    public void sendGeneralDirectMessage(MessageUtil messageUtil) {
        messageUtil.sendGeneralDirectMessage("syhan7516",message);
    }
}
