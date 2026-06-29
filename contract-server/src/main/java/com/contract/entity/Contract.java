package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String contractNo;

    @Column(length = 500)
    private String contractName;

    @Column(length = 100)
    private String contractType;

    @Column(length = 20)
    private String direction; // receivable / payable

    @Column(length = 200)
    private String ourCompany;

    @Column(length = 200)
    private String counterparty;

    @Column(length = 100)
    private String projectNo;

    @Column(length = 500)
    private String projectName;

    @Column(length = 500)
    private String contractAddress;

    @Column(length = 100)
    private String manager;

    @Column(length = 50)
    private String status; // unconfirmed / confirmed / archived

    private LocalDate signDate;
    private LocalDate endDate;

    @Column(length = 50)
    private String pricingMethod; // fixed / actual

    @Column(precision = 18, scale = 2)
    private BigDecimal contractAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal settlementAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal receivedAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal invoicedAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal unreceivedAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal uninvoicedAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal receivedNotInvoiced = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal invoicedNotReceived = BigDecimal.ZERO;

    // Payable side
    @Column(precision = 18, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal receivedInvoiceAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal unpaidAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal unreceivedInvoiceAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal paidNotReceivedInvoice = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal receivedInvoiceNotPaid = BigDecimal.ZERO;

    @Column(length = 50)
    private String receiptStatus; // unreceived / partial / completed

    @Column(length = 50)
    private String paymentStatus; // unpaid / partial / completed

    @Column(precision = 5, scale = 2)
    private BigDecimal receiptProgress = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal invoiceProgress = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal paymentProgress = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal receivedInvoiceProgress = BigDecimal.ZERO;

    // Profit-related (receivable side)
    @Column(precision = 18, scale = 2)
    private BigDecimal procurementContractAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal procurementSettlementAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal procurementPaidAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal procurementReceivedInvoice = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal profit = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal profitMargin = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal paidProfit = BigDecimal.ZERO;

    @Column(length = 500)
    private String attachment;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
