package com.electricity.electricity_billing_backend.service.impl;
import org.springframework.cache.annotation.CacheEvict;
import com.electricity.electricity_billing_backend.service.PdfService;
import com.electricity.electricity_billing_backend.dto.request.BillRequest;
import com.electricity.electricity_billing_backend.dto.response.BillResponse;
import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.entity.Meter;
import com.electricity.electricity_billing_backend.entity.MeterReading;
import com.electricity.electricity_billing_backend.entity.Tariff;
import com.electricity.electricity_billing_backend.enums.BillStatus;
import com.electricity.electricity_billing_backend.exception.BadRequestException;
import com.electricity.electricity_billing_backend.exception.DuplicateResourceException;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.mapper.BillMapper;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.electricity.electricity_billing_backend.repository.MeterReadingRepository;
import com.electricity.electricity_billing_backend.repository.MeterRepository;
import com.electricity.electricity_billing_backend.service.BillService;
import com.electricity.electricity_billing_backend.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.electricity.electricity_billing_backend.dto.request.NotificationRequest;
import com.electricity.electricity_billing_backend.enums.NotificationType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import com.electricity.electricity_billing_backend.service.EmailService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final BillMapper billMapper;
    private final NotificationService notificationService;
    private final MeterRepository meterRepository;
    private final EmailService emailService;
    private final PdfService pdfService;


    @Override
    @CacheEvict(value = "statistics", allEntries = true)
    public BillResponse generateBill(BillRequest request) {

        log.info("Generating bill for Meter Reading ID: {}", request.getMeterReadingId());

        MeterReading reading = meterReadingRepository.findById(request.getMeterReadingId())
                .orElseThrow(() -> new ResourceNotFoundException("Meter reading not found."));

        if (billRepository.existsByMeterReadingId(reading.getId())) {
            throw new DuplicateResourceException("Bill already generated.");
        }

        Meter meter = reading.getMeter();

        Tariff tariff = meter.getTariff();

        if (tariff == null) {
            throw new BadRequestException("No tariff assigned to this meter.");
        }

        BigDecimal energyRate = tariff.getRatePerUnit();
        BigDecimal fixedCharge = tariff.getFixedCharge();
        BigDecimal fuelRate = tariff.getFuelSurcharge();
        BigDecimal dutyRate = tariff.getElectricityDuty();

        BigDecimal units = BigDecimal.valueOf(reading.getUnitsConsumed());

        BigDecimal energyCharge = units.multiply(energyRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal fuelSurcharge = units.multiply(fuelRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal electricityDuty = energyCharge
                .multiply(dutyRate)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal lateFee = BigDecimal.ZERO.setScale(2);

        BigDecimal totalAmount = energyCharge
                .add(fixedCharge)
                .add(fuelSurcharge)
                .add(electricityDuty)
                .add(lateFee)
                .setScale(2, RoundingMode.HALF_UP);

        Bill bill = Bill.builder()
                .billNumber(generateBillNumber())
                .consumer(meter.getConsumer())
                .meter(meter)
                .meterReading(reading)
                .energyCharge(energyCharge)
                .fixedCharge(fixedCharge)
                .fuelSurcharge(fuelSurcharge)
                .electricityDuty(electricityDuty)
                .lateFee(lateFee)
                .totalAmount(totalAmount)
                .billDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(15))
                .status(BillStatus.GENERATED)
                .build();

        Bill savedBill = billRepository.save(bill);
        log.info(
                "Bill Generated Successfully. Bill Number: {}, Amount: {}",
                savedBill.getBillNumber(),
                savedBill.getTotalAmount()
        );

        byte[] pdf = pdfService.generateBillPdf(savedBill.getId());

        emailService.sendEmail(

                savedBill.getConsumer().getEmail(),

                "Electricity Bill Generated",

                """
                Dear %s,
        
                Your electricity bill has been generated.
        
                Bill Number : %s
        
                Amount : ₹%s
        
                Due Date : %s
        
                Thank you.
                """
                        .formatted(
                                savedBill.getConsumer().getFullName(),
                                savedBill.getBillNumber(),
                                savedBill.getTotalAmount(),
                                savedBill.getDueDate()
                        )
        );

        log.info(
                "Bill Email Sent To {}",
                savedBill.getConsumer().getEmail()
        );

        notificationService.createNotification(

                NotificationRequest.builder()

                        .consumerId(
                                savedBill.getConsumer().getId()
                        )

                        .type(
                                NotificationType.BILL_GENERATED
                        )

                        .title(
                                "Electricity Bill Generated"
                        )

                        .message(
                                "Your bill "
                                        + savedBill.getBillNumber()
                                        + " has been generated. Amount: ₹"
                                        + savedBill.getTotalAmount()
                        )

                        .build()

        );

        log.info(
                "Notification Created For Consumer {}",
                savedBill.getConsumer().getConsumerNumber()
        );





        return billMapper.toResponse(savedBill);
    }

    @Override
    public BillResponse getBillById(Long id) {

        Bill bill = billRepository.findById(id)
                .filter(Bill::getActive)
                .orElseThrow(() -> new RuntimeException("Bill not found."));

        return billMapper.toResponse(bill);
    }

    @Override
    public BillResponse getBillByNumber(String billNumber) {

        Bill bill = billRepository.findByBillNumber(billNumber)
                .filter(Bill::getActive)
                .orElseThrow(() -> new RuntimeException("Bill not found."));

        return billMapper.toResponse(bill);
    }

    @Override
    public List<BillResponse> getAllBills() {

        return billRepository.findByActiveTrue()
                .stream()
                .map(billMapper::toResponse)
                .toList();
    }

    @Override
    public List<BillResponse> getBillsByConsumer(Long consumerId) {

        return billRepository.findByConsumerId(consumerId)
                .stream()
                .filter(Bill::getActive)
                .map(billMapper::toResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = "statistics", allEntries = true)
    public void deleteBill(Long id) {

        Bill bill = billRepository.findById(id)
                .filter(Bill::getActive)
                .orElseThrow(() -> new RuntimeException("Bill not found."));

        bill.setActive(false);

        billRepository.save(bill);
    }

    private String generateBillNumber() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        String random = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return "BILL-" + date + "-" + random;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateOverdueBills() {

        List<Bill> bills = billRepository.findOverdueBills();

        for (Bill bill : bills) {

            bill.setLateFee(
                    bill.getLateFee().add(new BigDecimal("100"))
            );

            bill.setTotalAmount(

                    bill.getEnergyCharge()
                            .add(bill.getFixedCharge())
                            .add(bill.getFuelSurcharge())
                            .add(bill.getElectricityDuty())
                            .add(bill.getLateFee())
            );

            billRepository.save(bill);
            emailService.sendEmail(

                    bill.getConsumer().getEmail(),

                    "Late Fee Applied",

                    """
                    Dear %s,
            
                    Your bill is overdue.
            
                    Late fee has been applied.
            
                    New Amount : ₹%s
            
                    Please pay immediately.
            
                    Thank you. 
                    """
                            .formatted(

                                    bill.getConsumer().getFullName(),

                                    bill.getTotalAmount()
                            )
            );

        }
    }
    @Scheduled(cron = "0 0 0 1 * ?")
    @Transactional
    public void generateMonthlyBills() {

        // Generate bills for all active meters
    }


}