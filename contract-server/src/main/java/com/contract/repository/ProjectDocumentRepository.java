package com.contract.repository;

import com.contract.entity.ProjectDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectDocumentRepository extends JpaRepository<ProjectDocument, Long> {
    List<ProjectDocument> findByProjectNoOrderByUploadTimeDesc(String projectNo);
    void deleteByProjectNo(String projectNo);
}
