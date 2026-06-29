package com.contract.repository;

import com.contract.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByDirection(String direction, Pageable pageable);
}
