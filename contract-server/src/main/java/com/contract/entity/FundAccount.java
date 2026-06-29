package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_fund_account")
public class FundAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String accountName;

    @Column(length = 100)
    private String accountNo;

    @Column(length = 200)
    private String bankName;

    @Column(precision = 18, scale = 2)
    private java.math.BigDecimal balance = java.math.BigDecimal.ZERO;

    @Column
    private Boolean isDefault = false;

    @Column(length = 20)
    private String type; // company / personal

    @Column(length = 20)
    private String status; // enabled / disabled

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
