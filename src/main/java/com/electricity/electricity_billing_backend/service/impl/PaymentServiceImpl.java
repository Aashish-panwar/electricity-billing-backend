package com.electricity.electricity_billing_backend.service.impl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import com.electricity.electricity_billing_backend.dto.request.NotificationRequest;
import com.electricity.electricity_billing_backend.dto.request.PaymentRequest;
import com.electricity.electricity_billing_backend.dto.response.PaymentResponse;
import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.entity.Payment;
import com.electricity.electricity_billing_backend.enums.BillStatus;
import com.electricity.electricity_billing_backend.enums.NotificationType;
import com.electricity.electricity_billing_backend.enums.PaymentStatus;
import com.electricity.electricity_billing_backend.exception.BadRequestException;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.mapper.PaymentMapper;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.electricity.electricity_billing_backend.repository.PaymentRepository;
import com.electricity.electricity_billing_backend.service.NotificationService;
import com.electricity.electricity_billing_backend.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import com.electricity.electricity_billing_backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BillRepository billRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationService notificationService;
    private final EmailService emailService;

    @Override
    @Caching(evict = {

            @CacheEvict(value = "statistics", allEntries = true),

            @CacheEvict(value = "reports", allEntries = true)

    })
    public PaymentResponse makePayment(PaymentRequest request) {

        Bill bill = billRepository.findById(request.getBillId())
                .filter(Bill::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found."));

        log.info(
                "Processing Payment For Bill {}",
                bill.getBillNumber()
        );



        if (bill.getStatus() == BillStatus.PAID) {
            throw new BadRequestException("Bill is already paid.");
        }

        if (paymentRepository.existsByBillId(bill.getId())) {
            throw new RuntimeException("Payment already exists for this bill.");
        }

        if (request.getAmount().compareTo(bill.getTotalAmount()) != 0) {
            throw new RuntimeException("Payment amount must be equal to bill amount.");
        }

        Payment payment = Payment.builder()
                .transactionId(generateTransactionId())
                .bill(bill)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.SUCCESS)
                .paymentDate(LocalDateTime.now())
                .remarks(request.getRemarks())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        log.info(
                "Payment Successful. Transaction ID: {}",
                savedPayment.getTransactionId()
        );

        emailService.sendEmail(

                bill.getConsumer().getEmail(),

                "Payment Successful",

                """
                Dear %s,
        
                Your payment has been received successfully.
        
                Transaction ID : %s
        
                Amount : ₹%s
        
                Payment Date : %s
        
                Thank you.
                """
                        .formatted(

                                bill.getConsumer().getFullName(),

                                savedPayment.getTransactionId(),

                                savedPayment.getAmount(),

                                savedPayment.getPaymentDate()
                        )
        );

        log.info(
                "Payment Receipt Email Sent To {}",
                bill.getConsumer().getEmail()
        );

        bill.setStatus(BillStatus.PAID);

        billRepository.save(bill);

        log.info(
                "Bill {} marked as PAID",
                bill.getBillNumber()
        );
        notificationService.createNotification(

                NotificationRequest.builder()

                        .consumerId(
                                bill.getConsumer().getId()
                        )

                        .type(
                                NotificationType.PAYMENT_SUCCESS
                        )

                        .title(
                                "Payment Successful"
                        )

                        .message(
                                "Payment received for Bill " +
                                        bill.getBillNumber()
                        )

                        .build()

        );


        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findDetailsById(id)
                .filter(Payment::getActive)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAllWithDetails()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByBill(Long billId) {

        return paymentRepository.findDetailsByBillId(billId)
                .stream()
                .filter(Payment::getActive)
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    public List<PaymentResponse> getPaymentsByConsumer(Long consumerId) {

        return paymentRepository.findDetailsByConsumerId(consumerId)
                .stream()
                .filter(Payment::getActive)
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    public PaymentResponse getPaymentByTransactionId(String transactionId) {

        Payment payment = paymentRepository.findDetailsByTransactionId(transactionId)
                .filter(Payment::getActive)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        return paymentMapper.toResponse(payment);
    }

    @Override
    public void deletePayment(Long id) {

        Payment payment = paymentRepository.findDetailsById(id)
                .filter(Payment::getActive)
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        payment.setActive(false);

        paymentRepository.save(payment);
    }

    private String generateTransactionId() {

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String random = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return "TXN-" + date + "-" + random;
    }
}