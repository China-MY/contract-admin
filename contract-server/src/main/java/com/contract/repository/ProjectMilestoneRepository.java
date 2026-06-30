package com.contract.repository;

import com.contract.entity.ProjectMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Long> {
    List<ProjectMilestone> findByProjectNoOrderByStageOrder(String projectNo);
    List<ProjectMilestone> findByStatusNot(String status);
    List<ProjectMilestone> findByStatus(String status);
}
