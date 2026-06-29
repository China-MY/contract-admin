package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String projectNo;

    @Column(length = 500)
    private String projectName;

    @Column(length = 100)
    private String projectType;

    @Column(length = 50)
    private String status; // init / in_progress / completed

    @Column(length = 10)
    private String year;

    @Column(length = 100)
    private String source;

    @Column(length = 100)
    private String manager;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(precision = 18, scale = 2)
    private BigDecimal budgetAmount = BigDecimal.ZERO;

    @Column(length = 200)
    private String customerName;

    @Column(length = 500)
    private String attachment;

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
