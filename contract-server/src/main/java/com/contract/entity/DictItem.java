package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sys_dict_item")
public class DictItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String dictType; // contract_type / project_type / project_source / region / payment_method / expense_type / bank / tax_rate

    @Column(length = 200)
    private String label;

    @Column(length = 100)
    private String value;

    private Integer sortOrder;

    @Column(length = 20)
    private String status; // enabled / disabled

    private Boolean isDefault;

    private Boolean receivableDefault;

    private Boolean payableDefault;

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
