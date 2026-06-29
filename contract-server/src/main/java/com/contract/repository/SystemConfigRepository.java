package com.contract.repository;

import com.contract.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
    SystemConfig findByConfigKey(String configKey);
}
