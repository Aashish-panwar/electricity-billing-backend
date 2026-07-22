package com.electricity.electricity_billing_backend.service.impl;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.Cacheable;
import com.electricity.electricity_billing_backend.dto.response.*;
import com.electricity.electricity_billing_backend.entity.Consumer;
import com.electricity.electricity_billing_backend.entity.Meter;
import com.electricity.electricity_billing_backend.entity.Payment;
import com.electricity.electricity_billing_backend.enums.BillStatus;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.mapper.BillMapper;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.electricity.electricity_billing_backend.repository.ConsumerRepository;
import com.electricity.electricity_billing_backend.repository.MeterRepository;
import com.electricity.electricity_billing_backend.repository.PaymentRepository;
import com.electricity.electricity_billing_backend.service.ReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.cache.CacheManager;
import java.math.BigDecimal;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportServiceImpl implements ReportService {

    private final ConsumerRepository consumerRepository;
    private final MeterRepository meterRepository;
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final CacheManager cacheManager;

    private final BillMapper billMapper;

    @PostConstruct
    public void init() {
        System.out.println("Cache Manager = " + cacheManager.getClass().getName());
    }

    @Override
    public ConsumerReportResponse getConsumerReport(Long consumerId) {

        Consumer consumer = consumerRepository.findById(consumerId)
                .filter(Consumer::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Consumer not found."));

        List<com.electricity.electricity_billing_backend.entity.Bill> bills =
                billRepository.findDetailsByConsumerId(consumerId);

        BigDecimal totalBillAmount = bills.stream()
                .map(b -> b.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = bills.stream()
                .filter(b -> b.getStatus() == BillStatus.PAID)
                .map(b -> b.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingAmount = totalBillAmount.subtract(totalPaid);

        List<Meter> meters = meterRepository.findByConsumerId(consumerId);

        String meterNumber = meters.isEmpty()
                ? null
                : meters.get(0).getMeterNumber();

        log.info(
                "Consumer Report Generated : {}",
                consumerId
        );

        return ConsumerReportResponse.builder()
                .consumerId(consumer.getId())
                .consumerName(consumer.getFullName())
                .consumerNumber(consumer.getConsumerNumber())
                .meterNumber(meterNumber)
                .totalBills(bills.size())
                .totalBillAmount(totalBillAmount)
                .totalPaid(totalPaid)
                .pendingAmount(pendingAmount)
                .bills(
                        bills.stream()
                                .map(billMapper::toResponse)
                                .toList()
                )
                .build();
    }

    @Override
    public RevenueReportResponse getRevenueReport(Integer month, Integer year) {

        log.info(
                "Revenue Report Generated : {}/{}",
                month,
                year
        );

        return RevenueReportResponse.builder()

                .month(month)

                .year(year)

                .totalBills(
                        billRepository.countBillsByMonth(month, year)
                )

                .paidBills(
                        billRepository.countPaidBillsByMonth(month, year)
                )

                .pendingBills(
                        billRepository.countPendingBillsByMonth(month, year)
                )

                .totalRevenue(
                        billRepository.getMonthlyRevenue(month, year)
                )

                .build();
    }

    @Override
    public List<PaymentReportResponse> getPaymentReport() {

        log.info("Payment Report Generated");

        return paymentRepository.findAllWithDetails()
                .stream()
                .filter(Payment::getActive)
                .map(payment -> PaymentReportResponse.builder()
                        .transactionId(payment.getTransactionId())
                        .consumerName(payment.getBill().getConsumer().getFullName())
                        .billNumber(payment.getBill().getBillNumber())
                        .amount(payment.getAmount())
                        .paymentMethod(payment.getPaymentMethod().name())
                        .paymentDate(payment.getPaymentDate())
                        .build())
                .toList();
    }

    @Override
    @Cacheable(value = "statistics")
    public StatisticsReportResponse getStatisticsReport() {

        log.info("Loading statistics from Database...");

        return StatisticsReportResponse.builder()
                .totalConsumers(consumerRepository.countByActiveTrue())
                .totalMeters(meterRepository.countByActiveTrue())
                .totalBills(billRepository.countByActiveTrue())
                .paidBills(billRepository.countByStatusAndActiveTrue(BillStatus.PAID))
                .pendingBills(billRepository.countByStatusAndActiveTrue(BillStatus.PENDING))
                .totalRevenue(billRepository.getTotalRevenue())
                .build();
    }
}