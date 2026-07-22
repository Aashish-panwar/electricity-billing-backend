package com.electricity.electricity_billing_backend.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import com.electricity.electricity_billing_backend.dto.response.MonthlyBillsResponse;
import com.electricity.electricity_billing_backend.dto.response.MonthlyUnitsResponse;
import com.electricity.electricity_billing_backend.dto.response.RevenueChartResponse;
import org.springframework.stereotype.Service;

import com.electricity.electricity_billing_backend.dto.response.DashboardResponse;
import com.electricity.electricity_billing_backend.enums.BillStatus;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.electricity.electricity_billing_backend.repository.ConsumerRepository;
import com.electricity.electricity_billing_backend.repository.MeterRepository;
import com.electricity.electricity_billing_backend.service.DashboardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ConsumerRepository consumerRepository;

    private final MeterRepository meterRepository;

    private final BillRepository billRepository;

    @Override
    public DashboardResponse getDashboardStatistics() {

        Long totalConsumers =
                consumerRepository.countByActiveTrue();

        Long totalMeters =
                meterRepository.countByActiveTrue();

        Long totalBills =
                billRepository.countByActiveTrue();

        BigDecimal totalRevenue =
                billRepository.getTotalRevenue();

        Long paidBills =
                billRepository.countByStatusAndActiveTrue(
                        BillStatus.PAID
                );

        Long pendingBills =
                billRepository.countByStatusAndActiveTrue(
                        BillStatus.PENDING
                );

        Long overdueBills =
                billRepository.countByStatusAndActiveTrue(
                        BillStatus.OVERDUE
                );

        return DashboardResponse.builder()

                .totalConsumers(totalConsumers)

                .totalMeters(totalMeters)

                .totalBills(totalBills)

                .totalRevenue(totalRevenue)

                .paidBills(paidBills)

                .pendingBills(pendingBills)

                .overdueBills(overdueBills)

                .activeMeters(totalMeters)

                .build();

    }

    @Override
    public List<RevenueChartResponse> getMonthlyRevenue() {

        List<Object[]> data =
                billRepository.getMonthlyRevenueChart();

        return data.stream()

                .map(row -> RevenueChartResponse.builder()

                        .month((String) row[0])

                        .revenue((BigDecimal) row[1])

                        .build())

                .toList();

    }
    @Override
    public List<MonthlyUnitsResponse> getMonthlyUnits() {

        List<MonthlyUnitsResponse> response = new ArrayList<>();

        int year = LocalDate.now().getYear();

        for (int month = 1; month <= 12; month++) {

            Double units = billRepository.getMonthlyUnits(month, year);

            response.add(

                    new MonthlyUnitsResponse(

                            Month.of(month).name(),

                            units == null ? 0.0 : units

                    )

            );

        }

        return response;

    }

    @Override
    public List<MonthlyBillsResponse> getMonthlyBills() {

        List<MonthlyBillsResponse> response = new ArrayList<>();

        int year = LocalDate.now().getYear();

        for (int month = 1; month <= 12; month++) {

            Long bills = billRepository.countBillsByMonth(month, year);

            response.add(

                    new MonthlyBillsResponse(

                            Month.of(month).name(),

                            bills

                    )

            );

        }

        return response;

    }

}