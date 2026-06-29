package com.contract.controller;

import com.contract.common.Result;
import com.contract.entity.*;
import com.contract.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ContractController {

    private final ContractRepository contractRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentPlanRepository paymentPlanRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final DictItemRepository dictItemRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DeptRepository deptRepository;
    private final PostRepository postRepository;
    private final FundAccountRepository fundAccountRepository;
    private final LoginLogRepository loginLogRepository;
    private final OperationLogRepository operationLogRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public ContractController(ContractRepository contractRepository,
            InvoiceRepository invoiceRepository,
            PaymentPlanRepository paymentPlanRepository,
            PaymentRecordRepository paymentRecordRepository,
            ProjectRepository projectRepository,
            CustomerRepository customerRepository,
            CompanyRepository companyRepository,
            DictItemRepository dictItemRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            DeptRepository deptRepository,
            PostRepository postRepository,
            FundAccountRepository fundAccountRepository,
            LoginLogRepository loginLogRepository,
            OperationLogRepository operationLogRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.contractRepository = contractRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentPlanRepository = paymentPlanRepository;
        this.paymentRecordRepository = paymentRecordRepository;
        this.projectRepository = projectRepository;
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.dictItemRepository = dictItemRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.deptRepository = deptRepository;
        this.postRepository = postRepository;
        this.fundAccountRepository = fundAccountRepository;
        this.loginLogRepository = loginLogRepository;
        this.operationLogRepository = operationLogRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // === 合同 ===
    @GetMapping("/contracts")
    public Result<?> getContracts(
            @RequestParam String direction,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String project,
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) String receiptStatus,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) String paymentStatus) {
        Page<Contract> result = contractRepository.findByDirection(direction, PageRequest.of(page - 1, size));
        double totalAmount = contractRepository.findByDirection(direction).stream()
                .mapToDouble(c -> c.getContractAmount().doubleValue()).sum();
        Map<String, Object> data = new HashMap<>();
        data.put("records", result.getContent());
        data.put("total", result.getTotalElements());
        data.put("page", page);
        data.put("size", size);
        data.put("totalAmount", totalAmount);
        return Result.ok(data);
    }

    // === 发票 ===
    @GetMapping("/invoices")
    public Result<?> getInvoices(
            @RequestParam String direction,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Invoice> result = invoiceRepository.findByDirection(direction, PageRequest.of(page - 1, size));
        return Result.ok(Map.of("records", result.getContent(), "total", result.getTotalElements()));
    }

    // === 付款计划 ===
    @GetMapping("/payment-plans")
    public Result<?> getPaymentPlans(
            @RequestParam String direction,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PaymentPlan> result = paymentPlanRepository.findByDirection(direction, PageRequest.of(page - 1, size));
        return Result.ok(Map.of("records", result.getContent(), "total", result.getTotalElements()));
    }

    // === 付款记录 ===
    @GetMapping("/payment-records")
    public Result<?> getPaymentRecords(
            @RequestParam String direction,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<PaymentRecord> result = paymentRecordRepository.findByDirection(direction, PageRequest.of(page - 1, size));
        return Result.ok(Map.of("records", result.getContent(), "total", result.getTotalElements()));
    }

    // === 项目 ===
    @GetMapping("/projects")
    public Result<?> getProjects(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Project> result = projectRepository.findAll(PageRequest.of(page - 1, size));
        return Result.ok(Map.of("records", result.getContent(), "total", result.getTotalElements()));
    }

    // === 客户/供应商 ===
    @GetMapping("/customers")
    public Result<?> getCustomers(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return Result.ok(Map.of("records", customerRepository.findByType("customer", PageRequest.of(page - 1, size)).getContent()));
    }

    @GetMapping("/suppliers")
    public Result<?> getSuppliers(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return Result.ok(Map.of("records", customerRepository.findByType("supplier", PageRequest.of(page - 1, size)).getContent()));
    }

    // === 公司 ===
    @GetMapping("/settings/companies")
    public Result<?> getCompanies() { return Result.ok(companyRepository.findAll()); }

    @PostMapping("/settings/companies")
    public Result<?> createCompany(@RequestBody Company company) {
        company.setId(null);
        companyRepository.save(company);
        return Result.ok("保存成功");
    }

    @PutMapping("/settings/companies/{id}")
    public Result<?> updateCompany(@PathVariable Long id, @RequestBody Company body) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) return Result.error("公司不存在");
        if (body.getCompanyName() != null) company.setCompanyName(body.getCompanyName());
        if (body.getTaxId() != null) company.setTaxId(body.getTaxId());
        if (body.getPhone() != null) company.setPhone(body.getPhone());
        if (body.getAddress() != null) company.setAddress(body.getAddress());
        if (body.getInvoiceTitle() != null) company.setInvoiceTitle(body.getInvoiceTitle());
        if (body.getBankName() != null) company.setBankName(body.getBankName());
        if (body.getBankAccount() != null) company.setBankAccount(body.getBankAccount());
        companyRepository.save(company);
        return Result.ok("保存成功");
    }

    @PutMapping("/settings/companies/{id}/default")
    public Result<?> setDefaultCompany(@PathVariable Long id) {
        companyRepository.findByIsDefaultTrue().forEach(c -> { c.setIsDefault(false); companyRepository.save(c); });
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) { company.setIsDefault(true); companyRepository.save(company); }
        return Result.ok("设置成功");
    }

    @DeleteMapping("/settings/companies/{id}")
    public Result<?> deleteCompany(@PathVariable Long id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) return Result.error("公司不存在");
        if (Boolean.TRUE.equals(company.getIsDefault())) {
            return Result.error(400, "默认公司不能删除，请先设置其他公司为默认");
        }
        companyRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    // === 字典 ===
    @GetMapping("/settings/dict")
    public Result<?> getDict(@RequestParam String type) {
        return Result.ok(dictItemRepository.findByDictType(type));
    }

    @PostMapping("/settings/dict")
    public Result<?> createDict(@RequestBody DictItem item) {
        item.setId(null);
        dictItemRepository.save(item);
        return Result.ok("保存成功");
    }

    @PutMapping("/settings/dict/{id}")
    public Result<?> updateDict(@PathVariable Long id, @RequestBody DictItem body) {
        DictItem item = dictItemRepository.findById(id).orElse(null);
        if (item == null) return Result.error("字典项不存在");
        if (body.getLabel() != null) item.setLabel(body.getLabel());
        if (body.getValue() != null) item.setValue(body.getValue());
        if (body.getSortOrder() != null) item.setSortOrder(body.getSortOrder());
        if (body.getStatus() != null) item.setStatus(body.getStatus());
        if (body.getIsDefault() != null) item.setIsDefault(body.getIsDefault());
        if (body.getReceivableDefault() != null) item.setReceivableDefault(body.getReceivableDefault());
        if (body.getPayableDefault() != null) item.setPayableDefault(body.getPayableDefault());
        if (body.getRemark() != null) item.setRemark(body.getRemark());
        dictItemRepository.save(item);
        return Result.ok("保存成功");
    }

    @DeleteMapping("/settings/dict/{id}")
    public Result<?> deleteDict(@PathVariable Long id) {
        dictItemRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    // === 用户/角色/部门/岗位 ===
    @GetMapping("/users") public Result<?> getUsers() { return Result.ok(userRepository.findAll()); }

    @PostMapping("/users")
    public Result<?> createUser(@RequestBody User user) {
        User existing = userRepository.findByUsername(user.getUsername());
        if (existing != null) return Result.error("登录账号已存在");
        user.setId(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getDeptName() == null) user.setDeptName("");
        if (user.getPostName() == null) user.setPostName("");
        userRepository.save(user);
        return Result.ok("保存成功");
    }

    @PutMapping("/users/{id}")
    public Result<?> updateUser(@PathVariable Long id, @RequestBody User body) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return Result.error("用户不存在");
        if (body.getRealName() != null) user.setRealName(body.getRealName());
        if (body.getGender() != null) user.setGender(body.getGender());
        if (body.getPhone() != null) user.setPhone(body.getPhone());
        if (body.getEmail() != null) user.setEmail(body.getEmail());
        if (body.getRoleNames() != null) user.setRoleNames(body.getRoleNames());
        if (body.getCompanyIds() != null) user.setCompanyIds(body.getCompanyIds());
        if (body.getCompanyNames() != null) user.setCompanyNames(body.getCompanyNames());
        if (body.getStatus() != null) user.setStatus(body.getStatus());
        if (body.getDeptName() != null) user.setDeptName(body.getDeptName());
        if (body.getPostName() != null) user.setPostName(body.getPostName());
        if (body.getRemark() != null) user.setRemark(body.getRemark());
        if (body.getPassword() != null && !body.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(body.getPassword()));
        }
        userRepository.save(user);
        return Result.ok("保存成功");
    }

    @DeleteMapping("/users/{id}")
    public Result<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    // === 角色 ===
    @GetMapping("/roles") public Result<?> getRoles() { return Result.ok(roleRepository.findAll()); }
    @PostMapping("/roles") public Result<?> createRole(@RequestBody Role role) { role.setId(null); roleRepository.save(role); return Result.ok("保存成功"); }
    @PutMapping("/roles/{id}") public Result<?> updateRole(@PathVariable Long id, @RequestBody Role body) {
        Role r = roleRepository.findById(id).orElse(null); if(r==null)return Result.error("角色不存在");
        if(body.getRoleName()!=null) r.setRoleName(body.getRoleName()); if(body.getRoleCode()!=null) r.setRoleCode(body.getRoleCode());
        if(body.getSortOrder()!=null) r.setSortOrder(body.getSortOrder()); if(body.getStatus()!=null) r.setStatus(body.getStatus());
        if(body.getRemark()!=null) r.setRemark(body.getRemark()); roleRepository.save(r); return Result.ok("保存成功");
    }
    @DeleteMapping("/roles/{id}") public Result<?> deleteRole(@PathVariable Long id) { roleRepository.deleteById(id); return Result.ok("删除成功"); }

    // === 部门 ===
    @GetMapping("/depts") public Result<?> getDepts() { return Result.ok(deptRepository.findAll()); }
    @PostMapping("/depts") public Result<?> createDept(@RequestBody Dept dept) { dept.setId(null); deptRepository.save(dept); return Result.ok("保存成功"); }
    @PutMapping("/depts/{id}") public Result<?> updateDept(@PathVariable Long id, @RequestBody Dept body) {
        Dept d = deptRepository.findById(id).orElse(null); if(d==null)return Result.error("部门不存在");
        if(body.getDeptName()!=null) d.setDeptName(body.getDeptName()); if(body.getDeptCode()!=null) d.setDeptCode(body.getDeptCode());
        if(body.getManager()!=null) d.setManager(body.getManager()); if(body.getPhone()!=null) d.setPhone(body.getPhone());
        if(body.getParentId()!=null) d.setParentId(body.getParentId()); if(body.getSortOrder()!=null) d.setSortOrder(body.getSortOrder());
        if(body.getStatus()!=null) d.setStatus(body.getStatus()); if(body.getRemark()!=null) d.setRemark(body.getRemark());
        deptRepository.save(d); return Result.ok("保存成功");
    }
    @DeleteMapping("/depts/{id}") public Result<?> deleteDept(@PathVariable Long id) { deptRepository.deleteById(id); return Result.ok("删除成功"); }

    // === 岗位 ===
    @GetMapping("/posts") public Result<?> getPosts() { return Result.ok(postRepository.findAll()); }
    @PostMapping("/posts") public Result<?> createPost(@RequestBody Post post) { post.setId(null); postRepository.save(post); return Result.ok("保存成功"); }
    @PutMapping("/posts/{id}") public Result<?> updatePost(@PathVariable Long id, @RequestBody Post body) {
        Post p = postRepository.findById(id).orElse(null); if(p==null)return Result.error("岗位不存在");
        if(body.getPostName()!=null) p.setPostName(body.getPostName()); if(body.getPostCode()!=null) p.setPostCode(body.getPostCode());
        if(body.getSortOrder()!=null) p.setSortOrder(body.getSortOrder()); if(body.getStatus()!=null) p.setStatus(body.getStatus());
        if(body.getRemark()!=null) p.setRemark(body.getRemark()); postRepository.save(p); return Result.ok("保存成功");
    }
    @DeleteMapping("/posts/{id}") public Result<?> deletePost(@PathVariable Long id) { postRepository.deleteById(id); return Result.ok("删除成功"); }

    // === 资金账户 ===
    @GetMapping("/settings/fund-accounts")
    public Result<?> getFundAccounts() { return Result.ok(fundAccountRepository.findAll()); }

    @PostMapping("/settings/fund-accounts")
    public Result<?> createFundAccount(@RequestBody FundAccount account) {
        account.setId(null);
        fundAccountRepository.save(account);
        return Result.ok("保存成功");
    }

    @PutMapping("/settings/fund-accounts/{id}")
    public Result<?> updateFundAccount(@PathVariable Long id, @RequestBody FundAccount body) {
        FundAccount acc = fundAccountRepository.findById(id).orElse(null);
        if (acc == null) return Result.error("账户不存在");
        if (body.getAccountName() != null) acc.setAccountName(body.getAccountName());
        if (body.getAccountNo() != null) acc.setAccountNo(body.getAccountNo());
        if (body.getBankName() != null) acc.setBankName(body.getBankName());
        if (body.getType() != null) acc.setType(body.getType());
        if (body.getStatus() != null) acc.setStatus(body.getStatus());
        if (body.getRemark() != null) acc.setRemark(body.getRemark());
        fundAccountRepository.save(acc);
        return Result.ok("保存成功");
    }

    @DeleteMapping("/settings/fund-accounts/{id}")
    public Result<?> deleteFundAccount(@PathVariable Long id) {
        fundAccountRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    // === 日志 ===
    @GetMapping("/logs/login")
    public Result<?> getLoginLogs() { return Result.ok(loginLogRepository.findAll()); }
    @GetMapping("/logs/operation")
    public Result<?> getOperationLogs() { return Result.ok(operationLogRepository.findAll()); }

    // === 选项 ===
    @GetMapping("/options")
    public Result<?> getOptions() {
        return Result.ok(Map.of(
            "projects", projectRepository.findAll().stream().map(Project::getProjectName).collect(Collectors.toList()),
            "customers", customerRepository.findByType("customer", PageRequest.of(0, 100)).getContent().stream().map(Customer::getName).collect(Collectors.toList()),
            "suppliers", customerRepository.findByType("supplier", PageRequest.of(0, 100)).getContent().stream().map(Customer::getName).collect(Collectors.toList())
        ));
    }
}
