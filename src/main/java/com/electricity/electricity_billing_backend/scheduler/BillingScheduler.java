package com.electricity.electricity_billing_backend.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BillingScheduler {

    @Scheduled(cron = "0 0 1 * * ?")
    public void addLateFee() {

        System.out.println("Running Late Fee Scheduler...");

    }

}