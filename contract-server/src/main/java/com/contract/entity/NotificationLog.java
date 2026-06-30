package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification_log")
public class NotificationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long milestoneId;

    @Column(length = 50)
    private String channelType; // email / feishu / dingtalk

    @Column(length = 50)
    private String remindType; // before_start / before_end / on_end / overdue

    @Column(length = 20)
    private String status; // success / fail

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime sentAt;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
