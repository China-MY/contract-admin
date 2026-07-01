package com.contract.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "project_documents")
public class ProjectDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String projectNo;

    @Column(length = 500)
    private String projectName;

    @Column(length = 500)
    private String fileName;

    @Column(length = 500)
    private String originalName;

    @Column(length = 1000)
    private String filePath;

    private Long fileSize;

    @Column(length = 100)
    private String fileType;

    @Column(updatable = false)
    private LocalDateTime uploadTime;

    @Column(length = 500)
    private String remark;

    @PrePersist
    protected void onCreate() { uploadTime = LocalDateTime.now(); }
}
