package com.contract.repository;

import com.contract.entity.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DictItemRepository extends JpaRepository<DictItem, Long> {
    List<DictItem> findByDictType(String dictType);
}
