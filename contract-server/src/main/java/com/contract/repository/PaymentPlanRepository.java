package com.contract.repository;

import com.contract.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
    Page<PaymentPlan> findByDirection(String direction, Pageable pageable);
}
