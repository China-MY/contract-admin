package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notification_config")
public class NotificationConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String configName;

    @Column(length = 50)
    private String channelType; // email / feishu / dingtalk

    @Column(length = 500)
    private String webhookUrl;

    @Column(length = 500)
    private String secret;

    @Column(length = 200)
    private String email;

    @Column(length = 20)
    private String status = "enabled"; // enabled / disabled

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
