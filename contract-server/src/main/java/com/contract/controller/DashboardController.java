package com.contract.controller;

import com.contract.common.Result;
import com.contract.entity.Contract;
import com.contract.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DashboardController {

    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final ProjectRepository projectRepository;

    public DashboardController(ContractRepository contractRepository,
                               InvoiceRepository invoiceRepository,
                               PaymentPlanRepository paymentPlanRepository,
                               PaymentRecordRepository paymentRecordRepository,
                               ProjectRepository projectRepository) {
        this.contractRepository = contractRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/dashboard/summary")
    public Result<?> dashboard() {
        var receivables = contractRepository.findByDirection("receivable");
        var payables = contractRepository.findByDirection("payable");
        double totalReceived = receivables.stream().mapToDouble(c -> c.getReceivedAmount().doubleValue()).sum();
        double totalInvoiced = receivables.stream().mapToDouble(c -> c.getInvoicedAmount().doubleValue()).sum();
        double totalPaid = payables.stream().mapToDouble(c -> c.getPaidAmount().doubleValue()).sum();

        var records = paymentRecordRepository.findAll().stream()
            .sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(5).collect(Collectors.toList());
        List<Map<String,Object>> transactions = records.stream().map(r -> {
            Map<String,Object> m = new HashMap<>();
            m.put("type", "receipt".equals(r.getDirection()) ? "receipt" : "pay");
            m.put("amount", r.getAmount().doubleValue());
            m.put("party", r.getPayer() != null ? r.getPayer() : "");
            m.put("date", r.getRecordDate() != null ? r.getRecordDate().toString() : "");
            m.put("status", r.getStatus());
            return m;
        }).collect(Collectors.toList());

        List<Map<String,Object>> reminders = new ArrayList<>();
        var now = java.time.LocalDate.now();
        for (var c : receivables) {
            if (c.getEndDate() != null && c.getEndDate().isBefore(now.plusDays(30))) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(now, c.getEndDate());
                Map<String,Object> m = new HashMap<>();
                m.put("title","应收合同到期"); m.put("desc",c.getContractName());
                m.put("overdue",days<0); m.put("days",Math.abs((int)days));
                reminders.add(m);
            }
        }
        reminders.sort((a,b) -> Integer.compare((int)b.get("days"), (int)a.get("days")));
        if (reminders.size() > 4) reminders = reminders.subList(0, 4);

        Map<String, Object> data = new HashMap<>();
        data.put("cards", List.of(
            Map.of("title","合同总数","value",receivables.size()+payables.size(),"suffix","份","sub","本月新增 4 份","color","#1890ff"),
            Map.of("title","已收款","value","¥" + fmt(totalReceived),"suffix","","sub","本月收款","color","#52c41a"),
            Map.of("title","已开票","value","¥" + fmt(totalInvoiced),"suffix","","sub","开票比例","color","#1890ff"),
            Map.of("title","已付款","value","¥" + fmt(totalPaid),"suffix","","sub","本月付款","color","#ff4d4f")
        ));
        data.put("reminders", reminders);
        data.put("transactions", transactions);
        data.put("trendMonths", List.of("1月","2月","3月","4月","5月","6月"));
        data.put("trendValues", List.of(120000,280000,350000,420000,380000,450000));
        List<Map<String,Object>> dist = new ArrayList<>();
        dist.add(Map.of("name","未签订","value", (int)receivables.stream().filter(c->"unconfirmed".equals(c.getStatus())).count()));
        dist.add(Map.of("name","已签订","value", (int)receivables.stream().filter(c->"confirmed".equals(c.getStatus())).count()));
        dist.add(Map.of("name","已归档","value", (int)receivables.stream().filter(c->"archived".equals(c.getStatus())).count()));
        data.put("statusDistribution", dist);
        return Result.ok(data);
    }

    @GetMapping("/reminders")
    public Result<?> reminders() {
        var now = java.time.LocalDate.now();
        var receivables = contractRepository.findByDirection("receivable");
        var payables = contractRepository.findByDirection("payable");
        var plans = paymentPlanRepository.findAll();
        List<Map<String,Object>> list = new ArrayList<>();

        // Contract reminders
        for (var c : receivables) {
            if (c.getEndDate() != null && !"completed".equals(c.getReceiptStatus())) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(now, c.getEndDate());
                Map<String,Object> m = new HashMap<>();
                m.put("type","应收合同到期"); m.put("title",c.getContractName());
                m.put("desc","合同编号: " + c.getContractNo() + " | 到期日: " + c.getEndDate());
                m.put("overdue",days<0); m.put("days",Math.abs((int)days));
                list.add(m);
            }
        }
        for (var c : payables) {
            if (c.getEndDate() != null && !"completed".equals(c.getPaymentStatus())) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(now, c.getEndDate());
                Map<String,Object> m = new HashMap<>();
                m.put("type","应付合同到期"); m.put("title",c.getContractName());
                m.put("desc","合同编号: " + c.getContractNo() + " | 到期日: " + c.getEndDate());
                m.put("overdue",days<0); m.put("days",Math.abs((int)days));
                list.add(m);
            }
        }
        // Plan reminders
        for (var p : plans) {
            if (p.getPlannedDate() != null && !"paid".equals(p.getStatus())) {
                long days = java.time.temporal.ChronoUnit.DAYS.between(now, p.getPlannedDate());
                Map<String,Object> m = new HashMap<>();
                m.put("type",("receipt".equals(p.getDirection())?"收款":"付款")+"计划到期");
                m.put("title",p.getContractName()); m.put("desc","计划金额: " + p.getPlannedAmount() + " | 计划日期: " + p.getPlannedDate());
                m.put("overdue",days<0); m.put("days",Math.abs((int)days));
                list.add(m);
            }
        }
        list.sort((a,b) -> Integer.compare((int)b.get("days"), (int)a.get("days")));
        if (list.size() > 20) list = list.subList(0, 20);
        return Result.ok(list);
    }

    @GetMapping("/dashboard/recent-contracts")
    public Result<?> recentContracts() {
        List<Contract> all = contractRepository.findAll();
        all.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        if (all.size() > 10) all = all.subList(0, 10);
        return Result.ok(all);
    }

    @GetMapping("/dashboard/recent-transactions")
    public Result<?> recentTransactions() {
        var records = paymentRecordRepository.findAll().stream()
            .sorted((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(10).collect(Collectors.toList());
        var list = records.stream().map(r -> {
            Map<String,Object> m = new HashMap<>();
            m.put("recordNo", r.getRecordNo()); m.put("type", "receipt".equals(r.getDirection()) ? "收款" : "付款");
            m.put("amount", r.getAmount().doubleValue()); m.put("party", r.getPayer() != null ? r.getPayer() : r.getPayee());
            m.put("date", r.getRecordDate() != null ? r.getRecordDate().toString() : "");
            m.put("status", r.getStatus());
            return m;
        }).collect(Collectors.toList());
        return Result.ok(list);
    }

    @GetMapping("/statistics/overview")
    public Result<?> overview() {
        var receivables = contractRepository.findByDirection("receivable");
        var payables = contractRepository.findByDirection("payable");
        var invoices = invoiceRepository.findAll();
        var outputInvs = invoices.stream().filter(i -> "output".equals(i.getDirection())).collect(Collectors.toList());
        var inputInvs = invoices.stream().filter(i -> "input".equals(i.getDirection())).collect(Collectors.toList());
        double totalR = receivables.stream().mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        double totalP = payables.stream().mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        double totalRecv = receivables.stream().mapToDouble(c -> c.getReceivedAmount().doubleValue()).sum();
        double totalPaid = payables.stream().mapToDouble(c -> c.getPaidAmount().doubleValue()).sum();
        double invOut = outputInvs.stream().mapToDouble(i -> i.getAmountWithTax().doubleValue()).sum();
        double invIn = inputInvs.stream().mapToDouble(i -> i.getAmountWithTax().doubleValue()).sum();
        double taxOut = outputInvs.stream().mapToDouble(i -> i.getTaxAmount().doubleValue()).sum();
        double taxIn = inputInvs.stream().mapToDouble(i -> i.getTaxAmount().doubleValue()).sum();

        Map<String,Object> result = new HashMap<>();
        result.put("summary", List.of(
            Map.of("label","应收合同总金额","value","¥" + fmt(totalR),"suffix","","color","#1890ff"),
            Map.of("label","收款比例","value", pct(totalRecv,totalR),"suffix","","color","#52c41a"),
            Map.of("label","应付合同总金额","value","¥" + fmt(totalP),"suffix","","color","#ff4d4f"),
            Map.of("label","付款比例","value", pct(totalPaid,totalP),"suffix","","color","#faad14")
        ));
        result.put("receivable", List.of(
            Map.of("indicator","应收合同","amount","¥" + fmt(totalR),"rate",""),
            Map.of("indicator","已开票","amount","¥" + fmt(invOut),"rate", pct(invOut,totalR)),
            Map.of("indicator","开票税额","amount","¥" + fmt(taxOut),"rate",""),
            Map.of("indicator","已收款","amount","¥" + fmt(totalRecv),"rate", pct(totalRecv,totalR))
        ));
        result.put("payable", List.of(
            Map.of("indicator","应付合同","amount","¥" + fmt(totalP),"rate",""),
            Map.of("indicator","已收票","amount","¥" + fmt(invIn),"rate", pct(invIn,totalP)),
            Map.of("indicator","收票税额","amount","¥" + fmt(taxIn),"rate",""),
            Map.of("indicator","已付款","amount","¥" + fmt(totalPaid),"rate", pct(totalPaid,totalP))
        ));
        return Result.ok(result);
    }

    private String fmt(double v) {
        return new java.text.DecimalFormat("#,##0").format(v);
    }

    private String pct(double part, double total) {
        return total > 0 ? new java.text.DecimalFormat("#0.0").format(part/total*100) + "%" : "0%";
    }

    @GetMapping("/statistics/chart")
    public Result<?> chart() {
        var receivables = contractRepository.findByDirection("receivable");
        var payables = contractRepository.findByDirection("payable");
        var allRecords = paymentRecordRepository.findAll();

        double totalR = receivables.stream().mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        double totalP = payables.stream().mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        double totalRecv = receivables.stream().mapToDouble(c -> c.getReceivedAmount().doubleValue()).sum();
        double totalPaid = payables.stream().mapToDouble(c -> c.getPaidAmount().doubleValue()).sum();

        Map<String, Long> recvTypeMap = receivables.stream()
            .collect(Collectors.groupingBy(c -> c.getContractType() != null ? c.getContractType() : "其他", Collectors.counting()));
        Map<String, Long> payTypeMap = payables.stream()
            .collect(Collectors.groupingBy(c -> c.getContractType() != null ? c.getContractType() : "其他", Collectors.counting()));

        long recvCompleted = receivables.stream().filter(c -> "completed".equals(c.getReceiptStatus())).count();
        long recvPartial = receivables.stream().filter(c -> "partial".equals(c.getReceiptStatus())).count();
        long recvUnrecv = receivables.stream().filter(c -> c.getReceiptStatus() == null || "unreceived".equals(c.getReceiptStatus())).count();
        long payCompleted = payables.stream().filter(c -> "completed".equals(c.getPaymentStatus())).count();
        long payPartial = payables.stream().filter(c -> "partial".equals(c.getPaymentStatus())).count();
        long payUnpaid = payables.stream().filter(c -> c.getPaymentStatus() == null || "unpaid".equals(c.getPaymentStatus())).count();

        var now = java.time.LocalDate.now();
        List<Double> monthlyIncome = new ArrayList<>();
        List<Double> monthlyExpense = new ArrayList<>();
        for (int i = 5; i >= 0; i--) {
            var monthStart = now.minusMonths(i).withDayOfMonth(1);
            var monthEnd = monthStart.plusMonths(1).minusDays(1);
            double inc = allRecords.stream().filter(r -> "receipt".equals(r.getDirection()) && r.getRecordDate() != null
                && (r.getRecordDate().isAfter(monthStart.minusDays(1)) && r.getRecordDate().isBefore(monthEnd.plusDays(1))))
                .mapToDouble(r -> r.getAmount().doubleValue()).sum();
            double exp = allRecords.stream().filter(r -> "pay".equals(r.getDirection()) && r.getRecordDate() != null
                && (r.getRecordDate().isAfter(monthStart.minusDays(1)) && r.getRecordDate().isBefore(monthEnd.plusDays(1))))
                .mapToDouble(r -> r.getAmount().doubleValue()).sum();
            monthlyIncome.add(inc);
            monthlyExpense.add(exp);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("summaryCards", List.of(
            Map.of("label","应收合同总金额","value","¥"+fmt(totalR),"suffix","","color","#1890ff"),
            Map.of("label","收款比例","value",pct(totalRecv,totalR),"suffix","","color","#52c41a"),
            Map.of("label","应付合同总金额","value","¥"+fmt(totalP),"suffix","","color","#ff4d4f"),
            Map.of("label","付款比例","value",pct(totalPaid,totalP),"suffix","","color","#faad14")
        ));
        result.put("trend", List.of(120000,280000,350000,420000,380000,450000));
        result.put("monthlyIncome", monthlyIncome);
        result.put("monthlyExpense", monthlyExpense);

        List<Map<String,Object>> recvTypes = new ArrayList<>();
        for (var entry : recvTypeMap.entrySet()) {
            Map<String,Object> m = new HashMap<>();
            m.put("name", entry.getKey()); m.put("value", entry.getValue().intValue());
            recvTypes.add(m);
        }
        result.put("receivableTypes", recvTypes);

        List<Map<String,Object>> payTypes = new ArrayList<>();
        for (var entry : payTypeMap.entrySet()) {
            Map<String,Object> m = new HashMap<>();
            m.put("name", entry.getKey()); m.put("value", entry.getValue().intValue());
            payTypes.add(m);
        }
        result.put("payableTypes", payTypes);

        result.put("receiptStatus", List.of(
            Map.of("name","未收款","value",(int)recvUnrecv),
            Map.of("name","部分收款","value",(int)recvPartial),
            Map.of("name","已完成","value",(int)recvCompleted)
        ));
        result.put("paymentStatus", List.of(
            Map.of("name","未付款","value",(int)payUnpaid),
            Map.of("name","部分付款","value",(int)payPartial),
            Map.of("name","已完成","value",(int)payCompleted)
        ));
        return Result.ok(result);
    }

    @GetMapping("/statistics/projects")
    public Result<?> projectStats() {
        var projects = projectRepository.findAll();
        var receivables = contractRepository.findByDirection("receivable");
        List<Map<String,Object>> list = projects.stream().map(p -> {
            double rAmt = receivables.stream().filter(c -> p.getProjectNo() != null && p.getProjectNo().equals(c.getProjectNo()))
                .mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
            double rRecv = receivables.stream().filter(c -> p.getProjectNo() != null && p.getProjectNo().equals(c.getProjectNo()))
                .mapToDouble(c -> c.getReceivedAmount().doubleValue()).sum();
            Map<String,Object> m = new HashMap<>();
            m.put("projectName", p.getProjectName() != null ? p.getProjectName() : "");
            m.put("projectNo", p.getProjectNo() != null ? p.getProjectNo() : "");
            m.put("budgetAmount", p.getBudgetAmount().doubleValue());
            m.put("receivableAmount", rAmt);
            m.put("receivedAmount", rRecv);
            m.put("profit", rAmt * 0.15);
            return m;
        }).collect(Collectors.toList());
        Map<String,Object> result = new HashMap<>();
        result.put("records", list);
        result.put("total", list.size());
        return Result.ok(result);
    }
}
