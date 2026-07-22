package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.electricity.electricity_billing_backend.service.PdfService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.SolidBorder;

import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;

import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;

import java.io.ByteArrayOutputStream;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PdfServiceImpl implements PdfService {

    private final BillRepository billRepository;

    @Override
    public byte[] generateBillPdf(Long billId) {

        try {

            Bill bill = billRepository.findBillWithDetails(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Paragraph title = new Paragraph("ELECTRICITY BILL")
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                    .setFontSize(20)
                    .setTextAlignment(TextAlignment.CENTER);

            document.add(title);

            document.add(new LineSeparator(new SolidLine()));

            Table consumerTable = new Table(new float[]{4, 6});
            consumerTable.useAllAvailableWidth();

            consumerTable.addCell("Consumer Name");
            consumerTable.addCell(bill.getConsumer().getFullName());

            consumerTable.addCell("Consumer Number");
            consumerTable.addCell(bill.getConsumer().getConsumerNumber());

            consumerTable.addCell("Meter Number");
            consumerTable.addCell(bill.getMeter().getMeterNumber());

            consumerTable.addCell("Bill Number");
            consumerTable.addCell(bill.getBillNumber());

            document.add(consumerTable);

            Table chargeTable = new Table(new float[]{4, 6});
            chargeTable.useAllAvailableWidth();

            chargeTable.addCell("Energy Charge");
            chargeTable.addCell("₹ " + bill.getEnergyCharge());

            chargeTable.addCell("Fixed Charge");
            chargeTable.addCell("₹ " + bill.getFixedCharge());

            chargeTable.addCell("Fuel Surcharge");
            chargeTable.addCell("₹ " + bill.getFuelSurcharge());

            chargeTable.addCell("Electricity Duty");
            chargeTable.addCell("₹ " + bill.getElectricityDuty());

            chargeTable.addCell("Late Fee");
            chargeTable.addCell("₹ " + bill.getLateFee());

            chargeTable.addCell("Total Amount");
            chargeTable.addCell("₹ " + bill.getTotalAmount());

            document.add(chargeTable);

            document.add(new Paragraph(""));

            document.add(
                    new Paragraph("Thank you for using Electricity Billing Management System.")
                            .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD))
                            .setTextAlignment(TextAlignment.CENTER)
            );

            document.add(
                    new Paragraph("Please pay your bill before due date.")
                            .setTextAlignment(TextAlignment.CENTER)
            );

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

}