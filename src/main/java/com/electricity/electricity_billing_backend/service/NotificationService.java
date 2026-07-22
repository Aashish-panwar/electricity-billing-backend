package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.NotificationRequest;
import com.electricity.electricity_billing_backend.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {

    NotificationResponse createNotification(NotificationRequest request);

    List<NotificationResponse> getConsumerNotifications(Long consumerId);

    Long getUnreadCount(Long consumerId);

    void markAsRead(Long notificationId);
    void deleteNotification(Long notificationId);


}