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
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, CompanyRepository companyRepository,
                           DictItemRepository dictItemRepository,
                           RoleRepository roleRepository, DeptRepository deptRepository, PostRepository postRepository,
                           LoginLogRepository loginLogRepository, OperationLogRepository operationLogRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.dictItemRepository = dictItemRepository;
        this.roleRepository = roleRepository;
        this.deptRepository = deptRepository;
        this.postRepository = postRepository;
        this.loginLogRepository = loginLogRepository;
        this.operationLogRepository = operationLogRepository;
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

        seedDict("payment_settings", new String[][]{
            {"启用收付款计划","1"},{"启用收付款确认","2"},{"启用项目功能","3"},{"启用合同地址","4"},{"启用利润统计","5"}
        }, false, false);
    }
}
