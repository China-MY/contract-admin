package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String contractNo;

    @Column(length = 500)
    private String contractName;

    @Column(length = 100)
    private String invoiceNo;

    @Column(length = 20)
    private String direction; // output / input

    @Column(length = 50)
    private String type; // 专用发票

    @Column(precision = 18, scale = 2)
    private BigDecimal amountWithTax = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal amountWithoutTax = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    private LocalDate issueDate;

    @Column(length = 100)
    private String voucherNo;

    @Column(length = 200)
    private String issuer;

    @Column(length = 100)
    private String issuerTaxId;

    @Column(length = 200)
    private String receiver;

    @Column(length = 100)
    private String receiverTaxId;

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
