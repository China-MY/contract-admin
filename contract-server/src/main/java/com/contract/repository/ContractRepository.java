package com.contract.repository;

import com.contract.entity.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    Page<Contract> findByDirection(String direction, Pageable pageable);

    @Query("SELECT c FROM Contract c WHERE c.direction = :direction AND " +
           "(:keyword IS NULL OR c.contractNo LIKE %:keyword% OR c.contractName LIKE %:keyword%) " +
           "AND (:project IS NULL OR c.projectName LIKE %:project%) " +
           "AND (:counterparty IS NULL OR c.counterparty LIKE %:counterparty%) " +
           "AND (:status IS NULL OR c.receiptStatus = :status OR c.paymentStatus = :status)")
    Page<Contract> search(@Param("direction") String direction,
                          @Param("keyword") String keyword,
                          @Param("project") String project,
                          @Param("counterparty") String counterparty,
                          @Param("status") String status,
                          Pageable pageable);

    List<Contract> findByDirection(String direction);
    List<Contract> findByContractNo(String contractNo);
}
