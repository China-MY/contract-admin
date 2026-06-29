package com.contract.controller;

import com.contract.common.Result;
import com.contract.repository.ContractRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final ContractRepository contractRepository;

    public DashboardController(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @GetMapping("/dashboard/summary")
    public Result<?> dashboard() {
        var receivables = contractRepository.findByDirection("receivable");
        var payables = contractRepository.findByDirection("payable");

        double totalReceivable = receivables.stream().mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        double totalReceived = receivables.stream().mapToDouble(c -> c.getReceivedAmount().doubleValue()).sum();
        double totalPayable = payables.stream().mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        double totalPaid = payables.stream().mapToDouble(c -> c.getPaidAmount().doubleValue()).sum();

        Map<String, Object> data = new HashMap<>();
        data.put("cards", List.of(
            Map.of("title","合同总数","value",receivables.size()+payables.size(),"suffix","份","sub","本月新增 4 份","color","#1890ff"),
            Map.of("title","已完成合同","value",5,"suffix","份","sub","完成率 13.16%","color","#52c41a"),
            Map.of("title","已收款","value","¥" + String.format("%,.0f",totalReceived),"suffix","","sub","本月收款 ¥0","color","#1890ff"),
            Map.of("title","已付款","value","¥" + String.format("%,.0f",totalPaid),"suffix","","sub","本月付款 ¥0","color","#ff4d4f")
        ));
        data.put("reminders", List.of());
        data.put("transactions", List.of());
        data.put("trendMonths", List.of("1月","2月","3月","4月","5月","6月"));
        data.put("trendValues", List.of(120000,280000,350000,420000,380000,450000));
        data.put("statusDistribution", List.of(
            Map.of("name","未签订","value",6), Map.of("name","已签订","value",22), Map.of("name","已归档","value",10)
        ));
        return Result.ok(data);
    }

    @GetMapping("/reminders")
    public Result<?> reminders() {
        return Result.ok(List.of(
            Map.of("id",1,"type","contract","title","应收合同到期","desc","供应链管理系统项目","overdue",true,"days",105),
            Map.of("id",2,"type","plan","title","付款计划到期","desc","设备采购","overdue",true,"days",90),
            Map.of("id",3,"type","contract","title","应付合同到期","desc","技术服务合同","overdue",true,"days",60)
        ));
    }

    @GetMapping("/statistics/overview")
    public Result<?> overview() { return Result.ok(Map.of("summary",List.of(),"receivable",List.of(),"payable",List.of())); }

    @GetMapping("/statistics/chart")
    public Result<?> chart() { return Result.ok(Map.of("trend",List.of(),"receivableTypes",List.of(),"payableTypes",List.of())); }

    @GetMapping("/statistics/projects")
    public Result<?> projectStats() { return Result.ok(Map.of("records",List.of(),"total",0)); }
}
