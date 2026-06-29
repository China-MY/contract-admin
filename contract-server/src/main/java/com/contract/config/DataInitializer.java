package com.contract.config;

import com.contract.entity.*;
import com.contract.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final DictItemRepository dictItemRepository;
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;
    private final PostRepository postRepository;
    private final LoginLogRepository loginLogRepository;
    private final OperationLogRepository operationLogRepository;
    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CompanyRepository companyRepository,
                           DictItemRepository dictItemRepository,
                           RoleRepository roleRepository, DeptRepository deptRepository, PostRepository postRepository,
                           LoginLogRepository loginLogRepository, OperationLogRepository operationLogRepository,
                           ContractRepository contractRepository, InvoiceRepository invoiceRepository,
                           PaymentPlanRepository paymentPlanRepository, PaymentRecordRepository paymentRecordRepository,
                           ProjectRepository projectRepository, CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.dictItemRepository = dictItemRepository;
        this.roleRepository = roleRepository;
        this.deptRepository = deptRepository;
        this.postRepository = postRepository;
        this.loginLogRepository = loginLogRepository;
        this.operationLogRepository = operationLogRepository;
        this.contractRepository = contractRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.projectRepository = projectRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void seedDict(String dictType, String[][] items, boolean hasValue, boolean hasDefaults) {
        if (!dictItemRepository.findByDictType(dictType).isEmpty()) return;
        for (String[] row : items) {
            DictItem d = new DictItem(); d.setDictType(dictType); d.setLabel(row[0]);
            if (hasValue && row.length > 1) d.setValue(row[1]);
            int idx = hasValue ? 2 : 1;
            if (row.length > idx) d.setSortOrder(Integer.parseInt(row[idx]));
            else d.setSortOrder(1);
            d.setStatus("enabled");
            if (hasDefaults && row.length > idx + 1) d.setIsDefault("true".equals(row[idx + 1]));
            if (hasDefaults && row.length > idx + 2) d.setReceivableDefault("true".equals(row[idx + 2]));
            if (hasDefaults && row.length > idx + 3) d.setPayableDefault("true".equals(row[idx + 3]));
            dictItemRepository.save(d);
        }
    }

    @Override
    public void run(String... args) {
        // 创建默认用户
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setRealName("管理员");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRoleNames("超级管理员");
            admin.setStatus("normal");
            admin.setCompanyIds("1");
            admin.setCompanyNames("成者为译科技有限公司");
            userRepository.save(admin);

            Company c1 = new Company(); c1.setCompanyName("成者为译科技有限公司"); c1.setTaxId("91350100MA12345");
            c1.setPhone("0591-1234567"); c1.setAddress("福建省福州市"); c1.setIsDefault(true);
            companyRepository.save(c1);
        }

        // 字典数据（每种类型只初始化一次）
        seedDict("contract_type", new String[][]{
            {"销售合同","1","true","false"},{"采购合同","2","false","true"},{"服务合同","3","false","false"}
        }, false, true);

        seedDict("tax_rate", new String[][]{
            {"13%","0.13","1"},{"9%","0.09","2"},{"6%","0.06","3"},{"3%","0.03","4"},{"1%","0.01","5","true"},{"0%","0","6"}
        }, true, true);

        seedDict("project_type", new String[][]{
            {"研发项目","1"},{"实施项目","2"},{"维护项目","3"},{"咨询项目","4"},{"其他","5"}
        }, false, false);

        seedDict("project_source", new String[][]{
            {"客户介绍","1"},{"公开招标","2"},{"邀请招标","3"},{"自研项目","4"}
        }, false, false);

        seedDict("region", new String[][]{
            {"华东","1"},{"华南","2"},{"华北","3"},{"华中","4"},{"西南","5"},{"西北","6"},{"东北","7"}
        }, false, false);

        seedDict("payment_method", new String[][]{
            {"银行转账","1","true"},{"现金","2"},{"微信支付","3"},{"支付宝","4"},{"支票","5"},{"汇票","6"}
        }, false, true);

        seedDict("expense_type", new String[][]{
            {"材料费","1"},{"人工费","2"},{"设备费","3"},{"运输费","4"},{"管理费","5"},{"税费","6"},{"租赁费","7"},{"电费","8"},{"其他","9"}
        }, false, false);

        seedDict("bank", new String[][]{
            {"中国银行","1"},{"工商银行","2"},{"农业银行","3","true"},{"建设银行","4"},{"交通银行","5"},{"招商银行","6"},{"浦发银行","7"}
        }, false, true);

        seedDict("reminder_settings", new String[][]{
            {"合同到期前7天提醒","7"},{"合同到期前30天提醒","30"},{"收款到期前3天提醒","3"},{"收款到期前7天提醒","7"}
        }, false, false);

        // 角色/部门/岗位
        if (roleRepository.count() == 0) {
            for (String[] r : new String[][]{{"超级管理员","SUPER_ADMIN","0"},{"普通用户","USER","4"}}) {
                Role role = new Role(); role.setRoleName(r[0]); role.setRoleCode(r[1]);
                role.setSortOrder(Integer.parseInt(r[2])); role.setStatus("enabled");
                role.setRemark(r[0] + "角色"); roleRepository.save(role);
            }
        }
        if (deptRepository.count() == 0) {
            for (String[] d : new String[][]{{"总经办","-","0"},{"财务部","FINANCE","1"},{"销售部","SALES","2"},{"技术部","TECH","3"}}) {
                Dept dept = new Dept(); dept.setDeptName(d[0]); dept.setDeptCode(d[1]);
                dept.setSortOrder(Integer.parseInt(d[2])); dept.setStatus("enabled"); deptRepository.save(dept);
            }
        }
        if (postRepository.count() == 0) {
            for (String[] p : new String[][]{{"总经理","CEO","0"},{"部门经理","MGR","1"},{"普通员工","EMP","2"}}) {
                Post post = new Post(); post.setPostName(p[0]); post.setPostCode(p[1]);
                post.setSortOrder(Integer.parseInt(p[2])); post.setStatus("enabled"); postRepository.save(post);
            }
        }

        // 示例日志
        if (loginLogRepository.count() == 0) {
            String now = "2026-06-29T10:00:00";
            String[] times = {"2026-06-29T09:30:00","2026-06-29T08:15:00","2026-06-28T18:00:00","2026-06-28T09:00:00","2026-06-27T15:30:00"};
            for (int i = 0; i < times.length; i++) {
                com.contract.entity.LoginLog log = new com.contract.entity.LoginLog();
                log.setUsername(i == 0 ? "admin" : (i == 1 ? "zhangsan" : "admin"));
                log.setIp("192.168.1." + (i + 1));
                log.setStatus(i == 2 ? "fail" : "success");
                log.setMessage(i == 2 ? "密码错误" : "登录成功");
                log.setLoginTime(java.time.LocalDateTime.parse(times[i]));
                loginLogRepository.save(log);
            }
        }
        if (operationLogRepository.count() == 0) {
            String[][] ops = {{"admin","合同管理","新建","HT20260501","新建应收合同","192.168.1.1","2026-06-29T09:00:00"},
                             {"admin","发票管理","编辑","INV2026001","修改销项发票金额","192.168.1.1","2026-06-29T08:30:00"},
                             {"zhangsan","系统设置","修改","公司信息","更新公司地址","192.168.1.2","2026-06-28T16:00:00"}};
            for (String[] o : ops) {
                com.contract.entity.OperationLog log = new com.contract.entity.OperationLog();
                log.setUsername(o[0]); log.setModule(o[1]); log.setAction(o[2]); log.setTarget(o[3]);
                log.setDetail(o[4]); log.setIp(o[5]);
                log.setOperationTime(java.time.LocalDateTime.parse(o[6]));
                operationLogRepository.save(log);
            }
        }

        // 示例业务数据
        java.time.LocalDate now = java.time.LocalDate.now();
        if (contractRepository.count() == 0) {
            // 创建客户
            String[][] custs = {{"南京科技公司","customer"},{"中铁建二十局","customer"},{"福州赢雅科技","customer"},{"联想集团","supplier"},{"联通福建分公司","supplier"}};
            for (String[] c : custs) {
                com.contract.entity.Customer cu = new com.contract.entity.Customer();
                cu.setName(c[0]); cu.setType(c[1]); cu.setPhone("1380000" + (int)(Math.random()*9000+1000));
                customerRepository.save(cu);
            }
            // 创建项目
            String[][] projs = {{"NM260101","苏州爱华MES项目","实施项目","in_progress","2026","张总","1000000","苏州爱华"},
                               {"LY-BTH-01","白炭黑绿化工程","东大洋-1","in_progress","2026","李经理","1000000","中铁建二十局"},
                               {"202603001","智慧校园项目","集成项目","in_progress","2026","王工","5000000",""}};
            for (String[] p : projs) {
                com.contract.entity.Project project = new com.contract.entity.Project();
                project.setProjectNo(p[0]); project.setProjectName(p[1]); project.setProjectType(p[2]);
                project.setStatus(p[3]); project.setYear(p[4]); project.setManager(p[5]);
                project.setBudgetAmount(new java.math.BigDecimal(p[6])); project.setCustomerName(p[7]);
                project.setStartDate(now.minusMonths(3)); project.setEndDate(now.plusMonths(6));
                projectRepository.save(project);
            }
        }
        if (contractRepository.count() == 0) {
            // 应收合同
            Object[][] recContracts = {
                {"HT20260501","智慧校园平台开发合同","销售合同","receivable","筱凡网络科技","南京科技公司","NM260101","苏州爱华MES项目","已签订",now.minusMonths(2),now.plusMonths(10),"fixed",580000d,580000d,120000d,200000d,460000d,380000d,0d,80000d,"partial",20.68,34.48},
                {"HT20260502","白炭黑生产系统","销售合同","receivable","成者为译","中铁建二十局","LY-BTH-01","白炭黑绿化工程","已签订",now.minusMonths(1),now.plusMonths(8),"fixed",1200000d,1150000d,300000d,500000d,850000d,650000d,0d,200000d,"partial",26.09,43.48},
                {"HT20260503","MES系统运维服务","服务合同","receivable","筱凡网络科技","福州赢雅科技","NM260101","苏州爱华MES项目","已签订",now.minusMonths(1),now.plusMonths(12),"fixed",96000d,96000d,32000d,32000d,64000d,64000d,0d,0d,"partial",33.33,33.33},
                {"HT20260504","校园网络改造","销售合同","receivable","成者为译","南京科技公司","","","未签订",null,null,"fixed",45000d,45000d,0d,0d,45000d,45000d,0d,0d,"未收款",0d,0d},
                {"HT20260505","设备维护合同","服务合同","receivable","筱凡网络科技","南京科技公司","","","已归档",now.minusMonths(6),now.minusMonths(1),"fixed",24000d,24000d,24000d,24000d,0d,0d,0d,0d,"已完成",100d,100d},
            };
            for (Object[] c : recContracts) {
                com.contract.entity.Contract ct = new com.contract.entity.Contract();
                ct.setContractNo((String)c[0]); ct.setContractName((String)c[1]); ct.setContractType((String)c[2]);
                ct.setDirection((String)c[3]); ct.setOurCompany((String)c[4]); ct.setCounterparty((String)c[5]);
                ct.setProjectNo((String)c[6]); ct.setProjectName((String)c[7]); ct.setStatus((String)c[8]);
                ct.setSignDate((java.time.LocalDate)c[9]); ct.setEndDate((java.time.LocalDate)c[10]);
                ct.setPricingMethod((String)c[11]);
                ct.setContractAmount(java.math.BigDecimal.valueOf((Double)c[12]));
                ct.setSettlementAmount(java.math.BigDecimal.valueOf((Double)c[13]));
                ct.setReceivedAmount(java.math.BigDecimal.valueOf((Double)c[14]));
                ct.setInvoicedAmount(java.math.BigDecimal.valueOf((Double)c[15]));
                ct.setUnreceivedAmount(java.math.BigDecimal.valueOf((Double)c[16]));
                ct.setUninvoicedAmount(java.math.BigDecimal.valueOf((Double)c[17]));
                ct.setReceivedNotInvoiced(java.math.BigDecimal.valueOf((Double)c[18]));
                ct.setInvoicedNotReceived(java.math.BigDecimal.valueOf((Double)c[19]));
                ct.setReceiptStatus((String)c[20]);
                ct.setReceiptProgress(java.math.BigDecimal.valueOf((Double)c[21]));
                ct.setInvoiceProgress(java.math.BigDecimal.valueOf((Double)c[22]));
                ct.setProfit(java.math.BigDecimal.valueOf((Double)c[12] * 0.15));
                contractRepository.save(ct);
            }
            // 应付合同
            Object[][] payContracts = {
                {"CG20260501","服务器采购","采购合同","payable","筱凡网络科技","联想集团","NM260101","苏州爱华MES项目","已签订",now.minusMonths(1),now.plusMonths(6),"fixed",180000d,180000d,90000d,90000d,90000d,90000d,0d,0d,"partial",50d,50d},
                {"CG20260502","网络设备采购","采购合同","payable","成者为译","联通福建分公司","LY-BTH-01","白炭黑绿化工程","已签订",now.minusMonths(2),now.plusMonths(4),"fixed",350000d,340000d,100000d,0d,250000d,340000d,100000d,0d,"partial",29.41,0d},
                {"CG20260503","软件授权费","采购合同","payable","筱凡网络科技","南京科技公司","","","已签订",now,now.plusMonths(12),"fixed",50000d,50000d,0d,0d,50000d,50000d,0d,0d,"未付款",0d,0d},
            };
            for (Object[] c : payContracts) {
                com.contract.entity.Contract ct = new com.contract.entity.Contract();
                ct.setContractNo((String)c[0]); ct.setContractName((String)c[1]); ct.setContractType((String)c[2]);
                ct.setDirection((String)c[3]); ct.setOurCompany((String)c[4]); ct.setCounterparty((String)c[5]);
                ct.setProjectNo((String)c[6]); ct.setProjectName((String)c[7]); ct.setStatus((String)c[8]);
                ct.setSignDate((java.time.LocalDate)c[9]); ct.setEndDate((java.time.LocalDate)c[10]);
                ct.setPricingMethod((String)c[11]);
                ct.setContractAmount(java.math.BigDecimal.valueOf((Double)c[12]));
                ct.setSettlementAmount(java.math.BigDecimal.valueOf((Double)c[13]));
                ct.setPaidAmount(java.math.BigDecimal.valueOf((Double)c[14]));
                ct.setReceivedInvoiceAmount(java.math.BigDecimal.valueOf((Double)c[15]));
                ct.setUnpaidAmount(java.math.BigDecimal.valueOf((Double)c[16]));
                ct.setUnreceivedInvoiceAmount(java.math.BigDecimal.valueOf((Double)c[17]));
                ct.setPaidNotReceivedInvoice(java.math.BigDecimal.valueOf((Double)c[18]));
                ct.setReceivedInvoiceNotPaid(java.math.BigDecimal.valueOf((Double)c[19]));
                ct.setPaymentStatus((String)c[20]);
                ct.setPaymentProgress(java.math.BigDecimal.valueOf((Double)c[21]));
                ct.setReceivedInvoiceProgress(java.math.BigDecimal.valueOf((Double)c[22]));
                contractRepository.save(ct);
            }
            // 发票
            Object[][] invs = {
                {"HT20260501","智慧校园平台开发合同","INV2026001","output","专用发票",212000d,187610.62d,0.13d,24389.38d,now.minusDays(30),"-","筱凡网络科技","","南京科技公司",""},
                {"HT20260502","白炭黑生产系统","INV2026002","output","专用发票",500000d,442477.88d,0.13d,57522.12d,now.minusDays(15),"-","成者为译","","中铁建二十局",""},
                {"HT20260503","MES系统运维服务","INV2026003","output","专用发票",32000d,31683.17d,0.01d,316.83d,now.minusDays(5),"-","筱凡网络科技","","福州赢雅科技",""},
                {"CG20260501","服务器采购","INV2026004","input","专用发票",90000d,79646.02d,0.13d,10353.98d,now.minusDays(20),"-","联想集团","","筱凡网络科技",""},
            };
            for (Object[] i : invs) {
                com.contract.entity.Invoice inv = new com.contract.entity.Invoice();
                inv.setContractNo((String)i[0]); inv.setContractName((String)i[1]); inv.setInvoiceNo((String)i[2]);
                inv.setDirection((String)i[3]); inv.setType((String)i[4]);
                inv.setAmountWithTax(java.math.BigDecimal.valueOf((Double)i[5]));
                inv.setAmountWithoutTax(java.math.BigDecimal.valueOf((Double)i[6]));
                inv.setTaxRate(java.math.BigDecimal.valueOf((Double)i[7]));
                inv.setTaxAmount(java.math.BigDecimal.valueOf((Double)i[8]));
                inv.setIssueDate((java.time.LocalDate)i[9]); inv.setVoucherNo((String)i[10]);
                inv.setIssuer((String)i[11]); inv.setReceiver((String)i[13]);
                invoiceRepository.save(inv);
            }
            // 收款计划
            Object[][] plans = {
                {"HT20260501","智慧校园平台开发合同","PS-202605-001","receipt",120000d,120000d,0d,now.plusDays(30),"paid",100d,"南京科技公司","筱凡网络科技"},
                {"HT20260501","智慧校园平台开发合同","PS-202606-002","receipt",460000d,0d,460000d,now.plusDays(90),"unpaid",0d,"南京科技公司","筱凡网络科技"},
                {"HT20260502","白炭黑生产系统","PS-202605-003","receipt",300000d,300000d,0d,now.minusDays(10),"paid",100d,"中铁建二十局","成者为译"},
            };
            for (Object[] p : plans) {
                com.contract.entity.PaymentPlan plan = new com.contract.entity.PaymentPlan();
                plan.setContractNo((String)p[0]); plan.setContractName((String)p[1]); plan.setPlanCode((String)p[2]);
                plan.setDirection((String)p[3]);
                plan.setPlannedAmount(java.math.BigDecimal.valueOf((Double)p[4]));
                plan.setPaidAmount(java.math.BigDecimal.valueOf((Double)p[5]));
                plan.setUnpaidAmount(java.math.BigDecimal.valueOf((Double)p[6]));
                plan.setPlannedDate((java.time.LocalDate)p[7]); plan.setStatus((String)p[8]);
                plan.setProgress(java.math.BigDecimal.valueOf((Double)p[9]));
                plan.setPayer((String)p[10]); plan.setPayee((String)p[11]);
                paymentPlanRepository.save(plan);
            }
            // 收款记录
            Object[][] records = {
                {"HT20260501","智慧校园平台开发合同","RC-202605-001","receipt",now.minusDays(25),120000d,"confirmed","银行转账","南京科技公司","筱凡网络科技"},
                {"HT20260502","白炭黑生产系统","RC-202605-002","receipt",now.minusDays(10),300000d,"confirmed","银行转账","中铁建二十局","成者为译"},
                {"HT20260501","智慧校园平台开发合同","RC-202605-003","receipt",now.minusDays(5),10000d,"pending","银行转账","南京科技公司","筱凡网络科技"},
            };
            for (Object[] r : records) {
                com.contract.entity.PaymentRecord rec = new com.contract.entity.PaymentRecord();
                rec.setContractNo((String)r[0]); rec.setContractName((String)r[1]); rec.setRecordNo((String)r[2]);
                rec.setDirection((String)r[3]); rec.setRecordDate((java.time.LocalDate)r[4]);
                rec.setAmount(java.math.BigDecimal.valueOf((Double)r[5])); rec.setStatus((String)r[6]);
                rec.setMethod((String)r[7]); rec.setPayer((String)r[8]); rec.setPayee((String)r[9]);
                paymentRecordRepository.save(rec);
            }
        }

        seedDict("payment_settings", new String[][]{
            {"启用收付款计划","1"},{"启用收付款确认","2"},{"启用项目功能","3"},{"启用合同地址","4"},{"启用利润统计","5"}
        }, false, false);
    }
}
