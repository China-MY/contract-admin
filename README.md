# 合同管理系统 (Contract Management System)

> 企业级双向合同（应收/应付）全生命周期管理平台

---

## 📋 系统概述

面向中小企业的合同财务管理平台，覆盖合同从签订、执行、收款/付款、开票到归档的全生命周期，集成**项目阶段追踪**、发票、资金、客户/供应商等关联业务，支持**自动关联填充**、**项目级利润核算**、**钉钉/飞书到期提醒**和 RBAC 权限管理。

## 🚀 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **前端** | Vue 3 + TypeScript + Vite | ^3.4 / ^8.1 |
| **UI 框架** | Ant Design Vue 4 | ^4.2 |
| **状态管理** | Pinia | ^2.x |
| **图表** | ECharts | ^5.x |
| **后端** | Spring Boot 3 | 3.2.5 |
| **ORM** | Spring Data JPA + Hibernate | 6.4 |
| **安全** | Spring Security + JWT (jjwt) | 0.12 |
| **数据库** | MySQL 8.0 | — |
| **定时任务** | Spring @Scheduled | — |

## ✨ 核心功能

### 📊 仪表盘
- 合同总数、收款/付款金额统计卡片
- 合同金额趋势折线图 + 合同状态分布饼图（ECharts）
- 最近提醒 + 最近交易列表
- **点击提醒跳转提醒中心**

### 📄 合同管理
- **应收合同** — 向客户收款，含完整 30+ 字段（合同金额、已收款、已开票、利润等）
- **应付合同** — 向供应商付款，对称结构
- **自动关联填充** — 选择项目/客户后自动填充相关信息
- 进度百分比 + 收款/付款状态标签

### 🧾 发票管理
- 销项发票 + 进项发票
- 含税金额自动计算（含税→不含税→税额）
- 多税率支持（0%~13%）
- **选择合同自动填充开票方/收票方**
- 发票合计金额统计

### 💰 资金管理
- 收款计划 + 付款计划（三级联动：合同→计划→记录）
- 收款记录 + 付款记录（含确认状态）
- **选择合同自动填充付款方/收款方**
- 确认记录后自动更新合同已收/已付金额

### 📋 项目管理
- 项目列表 + 新建/编辑
- **项目详情页**（信息卡片 + 进度条 + 阶段时间线）
- **项目阶段管理** — 每个阶段可设置交付内容、计划日期、负责人
- **阶段自动计算进度** — 已完成阶段数 / 总阶段数 × 100%

### 📈 经营统计
- **经营明细** — 应收/应付汇总指标卡片
- **图表分析** — 6 个 ECharts 图表（合同趋势、类型分布、月度收支、状态分析）
- **项目统计** — 项目级收支利润表

### 🔔 提醒中心 & 自动通知
- 合同到期提醒 + 计划到期提醒
- **钉钉/飞书机器人推送** — 配置 Webhook 即可接收通知
- **多级提醒** — 开始前提醒、到期前提醒、到期日提醒、超期提醒
- **自定义执行时间** — 可在提醒配置页面设置每日执行小时
- **通知日志** — 查看每次发送结果和错误详情

### 👥 客户/供应商
- 客户列表 + 供应商列表 CRUD
- 支持税号、开户行、银行账号字段
- SelectCreate 通用可创建下拉组件

### ⚙️ 系统设置
- **公司管理** — 多公司管理，支持设为默认
- **资金账户** — 银行账户管理（含余额和默认标记）
- **基础设置** — 11 类字典表（合同类型/项目类型/支付方式/税率等）
- **通知配置** — 钉钉机器人/飞书机器人/邮件 Webhook 管理
- **提醒配置** — 提前天数、提醒类型开关、执行时间
- **用户管理** — 用户 CRUD，关联角色/部门/岗位/公司（多对多）
- **角色管理** — 角色 CRUD + 菜单权限分配
- **部门管理** — 树形部门结构
- **岗位管理** — 岗位 CRUD
- **登录日志 / 操作日志** — 审计追踪（**AOP 自动记录**）

### 🆕 新手指南
- 侧边栏「新手指南」引导页面
- 7 步操作流程时间线，每步可点击按钮直接跳转

---

## 🏗️ 项目结构

```
contract-admin/
├── contract-server/              # Spring Boot 3 后端
│   ├── pom.xml
│   └── src/main/java/com/contract/
│       ├── ContractApplication.java
│       ├── config/               # JWT、Security、CORS、数据初始化、操作日志AOP、通知调度
│       │   ├── SecurityConfig.java
│       │   ├── JwtUtil.java / JwtAuthFilter.java
│       │   ├── DataInitializer.java
│       │   ├── OperationLogAspect.java   # AOP 自动记录操作日志
│       │   └── NotificationScheduler.java # 定时检查到期提醒
│       ├── controller/           # REST API 控制器
│       │   ├── AuthController.java
│       │   ├── ContractController.java
│       │   ├── DashboardController.java
│       │   └── ProjectMilestoneController.java  # 阶段管理 + 通知发送
│       ├── entity/               # JPA 实体（19个）
│       │   ├── SystemConfig.java  # 系统配置（提醒设置等）
│       │   ├── ProjectMilestone.java  # 项目阶段
│       │   ├── NotificationConfig.java  # 通知渠道
│       │   └── NotificationLog.java  # 通知日志
│       ├── repository/           # 数据访问层（19个）
│       └── common/               # 统一响应封装
│
├── contract-web/                 # Vue 3 前端
│   ├── src/
│   │   ├── api/                  # Axios API 封装层（10个文件）
│   │   ├── components/           # 通用组件
│   │   │   ├── SelectCreate.vue  # 通用可创建下拉组件
│   │   │   ├── DictSelect.vue    # 字典下拉选择器
│   │   │   ├── StatusTag.vue     # 状态标签组件
│   │   │   ├── SearchForm.vue    # 通用搜索表单
│   │   │   ├── FileUpload.vue    # 附件上传组件
│   │   │   └── AmountInput.vue   # 金额输入组件
│   │   ├── router/               # 路由配置（30+ 页面）
│   │   ├── stores/               # Pinia 状态管理
│   │   ├── utils/                # 工具函数
│   │   ├── layouts/              # 主布局（侧边栏+顶栏）
│   │   └── views/
│   │       ├── guide/            # 🆕 新手指南
│   │       ├── dashboard/        # 仪表盘
│   │       ├── contract/         # 合同管理
│   │       ├── invoice/          # 发票管理
│   │       ├── payment/          # 资金管理
│   │       ├── project/          # 项目管理（含项目详情+阶段时间线）
│   │       ├── customer/         # 客户
│   │       ├── supplier/         # 供应商
│   │       ├── reminder/         # 提醒中心
│   │       ├── statistic/        # 经营统计（一体页面 Tab 切换）
│   │       ├── settings/         # 系统设置（11个子页面）
│   │       │   ├── Company.vue / FundAccount.vue / Basic.vue
│   │       │   ├── User.vue / Role.vue / Dept.vue / Post.vue
│   │       │   ├── NotificationConfig.vue  # 🆕 通知配置
│   │       │   ├── ReminderConfig.vue      # 🆕 提醒配置
│   │       │   ├── LoginLog.vue / OperationLog.vue
│   │       └── login/            # 登录页
│   ├── vite.config.ts
│   └── package.json
│
├── 合同管理系统-设计方案.md
├── 开发快照.md
└── README.md
```

---

## 🔧 本地开发运行

### 环境要求

- **Java 17+**（JDK 17）
- **Maven 3.9+**
- **Node.js 18+**
- **MySQL 8.0**

### 1. 启动数据库

```bash
# 确保 MySQL 运行在 3306 端口
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS contract_db DEFAULT CHARACTER SET utf8mb4;"
```

### 2. 启动后端

```bash
cd contract-server

# Windows (PowerShell)
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:Path="C:\Program Files\Java\jdk-17\bin;C:\Program Files\apache-maven-3.9.15\bin;$env:Path"
mvn clean package -DskipTests
java -jar target/contract-server-1.0.0.jar --spring.profiles.active=mysql --server.port=8080

# Linux/Mac
JAVA_HOME=/path/to/jdk-17 mvn clean package -DskipTests
java -jar target/contract-server-1.0.0.jar --spring.profiles.active=mysql --server.port=8080
```

> 首次启动会自动创建数据库表并初始化数据（admin 用户、公司、字典等）

### 3. 启动前端

```bash
cd contract-web
npm install
npm run dev -- --port 3000
```

### 4. 访问系统

```
前端: http://localhost:3000（局域网: http://192.168.x.x:3000）
后端 API: http://localhost:8080
默认账号: admin / 123456
```

---

## 🔗 核心关联逻辑

```
选择合同后自动填充 ↓

发票    → 开票方 = 合同.我方公司, 收票方 = 合同.对方单位
付款计划 → 付款方 = 合同.我方公司, 收款方 = 合同.对方单位
收款计划 → 付款方 = 合同.对方单位, 收款方 = 合同.我方公司
付款记录 → 同上
收款记录 → 同上

项目进度 = 已完成阶段数 / 总阶段数 × 100%
```

## 🔔 自动通知配置

1. 在「系统设置 → 通知配置」中添加钉钉/飞书机器人
2. 在「系统设置 → 提醒配置」中设置提前天数和执行时间
3. 在「项目详情 → 阶段管理」中创建阶段并填好计划日期
4. 系统每天在指定时间自动检查到期阶段并推送通知

**通知渠道：**
| 渠道 | 格式 | 说明 |
|------|------|------|
| 钉钉 | Webhook + 加签密钥 | 支持签名验证 |
| 飞书 | Webhook | 标准机器人消息 |
| 邮件 | SMTP（开发中） | — |

## 📊 数据模型核心关系

```
客户/供应商 ──→ 项目 ──→ 应收合同 ──→ 销项发票
                              ├──→ 收款计划 ──→ 收款记录
                              └──→ (关联采购) 利润计算

供应商/客户 ──→ 应付合同 ──→ 进项发票
                   ├──→ 付款计划 ──→ 付款记录
                   └──→ (关联应收) 成本核算

项目 ──→ 项目阶段（进度自动计算）
项目阶段 ──→ 到期提醒 ──→ 钉钉/飞书通知

用户 ── 角色 ── 菜单权限
用户 ── 公司（多对多）
用户 ── 部门 ── 岗位
```

## 📄 许可证

MIT License
