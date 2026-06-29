package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment_records")
public class PaymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String contractNo;

    @Column(length = 500)
    private String contractName;

    @Column(length = 100)
    private String invoiceNo;

    @Column(length = 100)
    private String recordNo;

    private LocalDate recordDate;

    @Column(precision = 18, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(length = 50)
    private String status; // pending / confirmed

    @Column(length = 20)
    private String direction; // receipt / pay

    @Column(length = 100)
    private String method;

    @Column(length = 200)
    private String account;

    @Column(length = 100)
    private String expenseType;

    @Column(length = 200)
    private String payer;

    @Column(length = 200)
    private String payerBank;

    @Column(length = 200)
    private String payee;

    @Column(length = 200)
    private String payeeBank;

    @Column(length = 100)
    private String voucherNo;

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
