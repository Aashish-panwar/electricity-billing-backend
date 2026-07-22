package com.electricity.electricity_billing_backend.mapper;


import com.electricity.electricity_billing_backend.dto.response.PaymentResponse;
import com.electricity.electricity_billing_backend.entity.Payment;

import org.springframework.stereotype.Component;



@Component
public class PaymentMapper {


    public PaymentResponse toResponse(Payment payment){


        return PaymentResponse.builder()

                .id(payment.getId())


                .transactionId(
                        payment.getTransactionId()
                )


                .billId(
                        payment.getBill().getId()
                )


                .billNumber(
                        payment.getBill()
                                .getBillNumber()
                )


                .consumerName(
                        payment.getBill()
                                .getConsumer()
                                .getFullName()
                )

                .meterNumber(
                        payment.getBill()
                                .getMeter()
                                .getMeterNumber()
                )

                .billStatus(
                        payment.getBill()
                                .getStatus()
                )


                .amount(
                        payment.getAmount()
                )


                .paymentMethod(
                        payment.getPaymentMethod()
                )


                .status(
                        payment.getStatus()
                )


                .paymentDate(
                        payment.getPaymentDate()
                )


                .remarks(
                        payment.getRemarks()
                )


                .build();

    }

}