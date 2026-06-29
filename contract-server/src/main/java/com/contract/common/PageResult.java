package com.contract.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> records;
    private long total;
    private int page;
    private int size;

    public static <T> PageResult<T> of(Page<T> pageResult) {
        PageResult<T> pr = new PageResult<>();
        pr.setRecords(pageResult.getContent());
        pr.setTotal(pageResult.getTotalElements());
        pr.setPage(pageResult.getNumber() + 1);
        pr.setSize(pageResult.getSize());
        return pr;
    }

    public static <T> PageResult<T> of(List<T> list, long total, int page, int size) {
        PageResult<T> pr = new PageResult<>();
        pr.setRecords(list);
        pr.setTotal(total);
        pr.setPage(page);
        pr.setSize(size);
        return pr;
    }
}
