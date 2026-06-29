package com.contract.repository;

import com.contract.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Page<Invoice> findByDirection(String direction, Pageable pageable);
    List<Invoice> findByDirection(String direction);
}
