package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_milestones")
public class ProjectMilestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String projectNo;

    @Column(length = 500)
    private String projectName;

    @Column(length = 200)
    private String stageName;

    private Integer stageOrder;

    @Column(columnDefinition = "TEXT")
    private String deliverableContent;

    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;

    @Column(length = 20)
    private String status; // pending / in_progress / completed / delayed

    @Column(precision = 5, scale = 2)
    private BigDecimal progress = BigDecimal.ZERO;

    @Column(length = 100)
    private String assignee;

    @Column(length = 500)
    private String remark;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
