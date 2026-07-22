package com.electricity.electricity_billing_backend.mapper;

import com.electricity.electricity_billing_backend.dto.response.NotificationResponse;
import com.electricity.electricity_billing_backend.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification){

        return NotificationResponse.builder()

                .id(notification.getId())

                .consumerId(
                        notification.getConsumer().getId()
                )

                .consumerName(
                        notification.getConsumer().getFullName()
                )

                .type(
                        notification.getType()
                )

                .title(
                        notification.getTitle()
                )

                .message(
                        notification.getMessage()
                )

                .isRead(
                        notification.getIsRead()
                )

                .createdAt(
                        notification.getCreatedAt()
                )

                .build();

    }

}