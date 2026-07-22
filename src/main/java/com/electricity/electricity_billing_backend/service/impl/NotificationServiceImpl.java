package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.dto.request.NotificationRequest;
import com.electricity.electricity_billing_backend.dto.response.NotificationResponse;
import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.entity.Consumer;
import com.electricity.electricity_billing_backend.entity.Notification;
import com.electricity.electricity_billing_backend.mapper.NotificationMapper;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.electricity.electricity_billing_backend.repository.ConsumerRepository;
import com.electricity.electricity_billing_backend.repository.NotificationRepository;
import com.electricity.electricity_billing_backend.service.EmailService;
import com.electricity.electricity_billing_backend.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ConsumerRepository consumerRepository;
    private final BillRepository billRepository;
    private final EmailService emailService;

    @Override
    public List<NotificationResponse> getConsumerNotifications(Long consumerId) {

        return notificationRepository.findByConsumerId(consumerId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Override
    public Long getUnreadCount(Long consumerId) {

        return notificationRepository
                .countByConsumerIdAndIsReadFalse(consumerId);
    }

    @Override
    public void markAsRead(Long notificationId) {

        Notification notification = notificationRepository
                .findById(notificationId)
                .filter(Notification::getActive)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found."));

        notification.setIsRead(true);

        notificationRepository.save(notification);

        log.info(
                "Notification Read : {}",
                notification.getId()
        );

        notificationRepository.save(notification);


    }
    @Override
    public NotificationResponse createNotification(NotificationRequest request) {

        Consumer consumer = consumerRepository.findById(request.getConsumerId())
                .filter(Consumer::getActive)
                .orElseThrow(() ->
                        new RuntimeException("Consumer not found."));

        Notification notification = Notification.builder()

                .consumer(consumer)

                .type(request.getType())

                .title(request.getTitle())

                .message(request.getMessage())

                .build();

        Notification savedNotification = notificationRepository.save(notification);

        log.info(
                "Notification Created : {}",
                savedNotification.getTitle()
        );
        emailService.sendEmail(

                consumer.getEmail(),

                notification.getTitle(),

                notification.getMessage()

        );

        return notificationMapper.toResponse(notification);

    }

    @Scheduled(cron = "0 0 9 * * ?")
    @Transactional
    public void sendDueReminder() {

        List<Bill> bills =
                billRepository.findBillsDueTomorrow(
                        LocalDate.now().plusDays(1)
                );

        for (Bill bill : bills) {

            Notification notification = Notification.builder()
                    .consumer(bill.getConsumer())
                    .title("Bill Due Reminder")
                    .message("Your electricity bill " + bill.getBillNumber()
                            + " is due on " + bill.getDueDate())
                    .isRead(false)
                    .build();

            Notification savedNotification = notificationRepository.save(notification);

            log.info(
                    "Due Reminder Notification Sent : {}",
                    bill.getBillNumber()
            );

            emailService.sendEmail(

                    bill.getConsumer().getEmail(),

                    "Electricity Bill Due Reminder",

                    """
                    Dear %s,
    
                    Your electricity bill is due on %s.
    
                    Amount : ₹%s
    
                    Please pay before due date.
    
                    Thank you.
                    """
                            .formatted(

                                    bill.getConsumer().getFullName(),

                                    bill.getDueDate(),

                                    bill.getTotalAmount()
                            )
            );
            log.info(
                    "Reminder Email Sent To {}",
                    bill.getConsumer().getEmail()
            );
        }

    }

    @Override
    public void deleteNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId)
                .filter(Notification::getActive)
                .orElseThrow(() ->
                        new RuntimeException("Notification not found."));

        notification.setActive(false);

        notificationRepository.save(notification);

        log.warn(
                "Notification Deleted : {}",
                notification.getId()
        );
    }

}