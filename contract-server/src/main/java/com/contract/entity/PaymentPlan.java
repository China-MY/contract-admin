package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment_plans")
public class PaymentPlan {
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
    private String planCode;

    @Column(length = 20)
    private String direction; // receipt / pay

    @Column(precision = 18, scale = 2)
    private BigDecimal plannedAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(precision = 18, scale = 2)
    private BigDecimal unpaidAmount = BigDecimal.ZERO;

    private LocalDate plannedDate;

    @Column(length = 50)
    private String status; // unpaid / partial / paid

    @Column(precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;

    @Column(length = 200)
    private String payer;

    @Column(length = 200)
    private String payee;

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
