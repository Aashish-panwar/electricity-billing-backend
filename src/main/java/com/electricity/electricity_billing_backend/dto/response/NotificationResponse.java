package com.electricity.electricity_billing_backend.dto.response;

import com.electricity.electricity_billing_backend.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private Long consumerId;

    private String consumerName;

    private NotificationType type;

    private String title;

    private String message;

    private Boolean isRead;

    private LocalDateTime createdAt;

}