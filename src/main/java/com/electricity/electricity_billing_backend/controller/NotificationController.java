package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.response.NotificationResponse;
import com.electricity.electricity_billing_backend.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get Consumer Notifications")
    @GetMapping("/consumer/{consumerId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<List<NotificationResponse>>
    getNotifications(@PathVariable Long consumerId){

        return ResponseEntity.ok(
                notificationService.getConsumerNotifications(consumerId)
        );
    }

    @Operation(summary = "Get Unread Notification Count")
    @GetMapping("/consumer/{consumerId}/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<Long> getUnreadCount(
            @PathVariable Long consumerId){

        return ResponseEntity.ok(
                notificationService.getUnreadCount(consumerId)
        );
    }

    @Operation(summary = "Mark Notification As Read")
    @PutMapping("/{notificationId}/read")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long notificationId){

        notificationService.markAsRead(notificationId);

        return ResponseEntity.ok("Notification marked as read.");
    }

    @Operation(summary = "Get Unread Notification")
    @GetMapping("/consumer/{consumerId}/unread")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CONSUMER')")
    public ResponseEntity<List<NotificationResponse>>
    getUnreadNotifications(
            @PathVariable Long consumerId){

        return ResponseEntity.ok(

                notificationService
                        .getConsumerNotifications(consumerId)
                        .stream()
                        .filter(notification -> !notification.getIsRead())
                        .toList()

        );
    }

    @Operation(summary = "Delete Notification")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long id
    ) {
        notificationService.deleteNotification(id);

        return ResponseEntity.ok("Notification deleted successfully.");
    }



}