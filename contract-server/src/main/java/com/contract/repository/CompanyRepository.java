package com.contract.repository;

import com.contract.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByIsDefaultTrue();

    @Modifying
    @Query("update Company c set c.isDefault = false where c.isDefault = true")
    void clearAllDefault();
}
