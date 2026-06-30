package com.contract.repository;

import com.contract.entity.NotificationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationConfigRepository extends JpaRepository<NotificationConfig, Long> {
    List<NotificationConfig> findByStatus(String status);
    List<NotificationConfig> findByChannelTypeAndStatus(String channelType, String status);
}
