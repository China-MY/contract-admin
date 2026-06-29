package com.contract.repository;

import com.contract.entity.FundAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundAccountRepository extends JpaRepository<FundAccount, Long> {}
