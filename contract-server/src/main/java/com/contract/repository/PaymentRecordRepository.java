package com.contract.repository;

import com.contract.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Page<PaymentRecord> findByDirection(String direction, Pageable pageable);
    List<PaymentRecord> findByContractNoAndDirection(String contractNo, String direction);
}
