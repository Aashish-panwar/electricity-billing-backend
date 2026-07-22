package com.electricity.electricity_billing_backend.dto.request;

import com.electricity.electricity_billing_backend.enums.NotificationType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    private Long consumerId;

    private NotificationType type;

    private String title;

    private String message;

}