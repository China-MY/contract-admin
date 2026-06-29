package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "sys_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String realName;

    @Column(length = 10)
    private String gender;

    @Column(length = 100, unique = true)
    private String username;

    @Column(length = 200)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(length = 200)
    private String email;

    @Column(length = 500)
    private String roleNames;

    @Column(length = 500)
    private String companyIds; // 关联公司ID，逗号分隔

    @Column(length = 1000)
    private String companyNames; // 关联公司名称，逗号分隔

    @Column(length = 200)
    private String deptName;

    @Column(length = 200)
    private String postName;

    @Column(length = 20)
    private String status; // normal / disabled

    private LocalDateTime lastLoginAt;

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
