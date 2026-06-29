# 合同管理系统 (Contract Management System)

> 企业级双向合同（应收/应付）全生命周期管理平台

---

## 📋 系统概述

面向中小企业的合同财务管理平台，覆盖合同从签订、执行、收款/付款、开票到归档的全生命周期，集成项目、发票、资金、客户/供应商等关联业务，支持项目级利润核算和 RBAC 权限管理。

## 🚀 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| **前端** | Vue 3 + TypeScript + Vite | ^3.4 / ^8.1 |
| **UI 框架** | Ant Design Vue 4 | ^4.2 |
| **状态管理** | Pinia | ^2.x |
| **图表** | ECharts + vue-echarts | ^5.x |
| **后端** | Spring Boot 3 | 3.2.5 |
| **ORM** | Spring Data JPA + Hibernate | 6.4 |
| **安全** | Spring Security + JWT (jjwt) | 0.12 |
| **数据库** | MySQL 8.0 | — |

## ✨ 核心功能

### 📊 仪表盘
- 合同总数、已完成合同、收款/付款金额统计卡片
- 合同金额趋势折线图 + 合同状态分布饼图（ECharts）
- 最近提醒 + 最近交易列表

### 📄 合同管理
- **应收合同** — 向客户收款，含完整 30+ 字段（合同金额、已收款、已开票、利润等）
- **应付合同** — 向供应商付款，对称结构
- 支持新建/编辑/搜索/分页
- 进度百分比 + 收款/付款状态标签

### 🧾 发票管理
- 销项发票 + 进项发票
- 含税金额自动计算（含税→不含税→税额）
- 多税率支持（0%~13%）

### 💰 资金管理
- 收款计划 + 付款计划（三级联动：合同→计划→记录）
- 收款记录 + 付款记录（含确认状态）

### 📋 项目管理
- 项目列表 + 新建/编辑
- 项目收支统计（收入/支出/利润汇总）

### 👥 客户/供应商
- 客户列表 + 供应商列表

### ⚙️ 系统设置
- **公司管理** — 多公司管理，支持设为默认
- **资金账户** — 银行账户管理
- **基础设置** — 10 类字典表（合同类型/项目类型/支付方式/税率等）
- **用户管理** — 用户 CRUD，关联角色/部门/岗位/公司（多对多）
- **角色管理** — 角色 CRUD + 菜单权限树配置
- **部门管理** — 树形部门结构，支持子部门
- **岗位管理** — 岗位 CRUD
- **登录日志 / 操作日志** — 审计追踪

### 📈 统计图表
- 经营明细（应收/应付汇总）
- 图表分析（6 个 ECharts 图表）
- 项目统计（项目级收支利润表）

### 🔔 提醒中心
- 合同到期提醒
- 收款/付款计划到期提醒

---

## 🏗️ 项目结构

```
contract-admin/
├── contract-server/              # Spring Boot 3 后端
│   ├── pom.xml
│   └── src/main/java/com/contract/
│       ├── ContractApplication.java
│       ├── config/               # JWT、Security、数据初始化
│       ├── controller/           # REST API 控制器
│       ├── entity/               # JPA 实体（15个）
│       ├── repository/           # 数据访问层
│       └── common/               # 统一响应封装
│
├── contract-web/                 # Vue 3 前端
│   ├── src/
│   │   ├── api/                  # API 接口
│   │   ├── mock/                 # Mock 数据层（可切换）
│   │   ├── router/               # 路由配置（25+ 页面）
│   │   ├── stores/               # Pinia 状态管理
│   │   ├── utils/                # 工具函数
│   │   ├── layouts/              # 布局组件
│   │   └── views/                # 页面组件
│   │       ├── dashboard/        # 仪表盘
│   │       ├── contract/         # 合同管理
│   │       ├── invoice/          # 发票管理
│   │       ├── payment/          # 资金管理
│   │       ├── project/          # 项目管理
│   │       ├── customer/         # 客户
│   │       ├── supplier/         # 供应商
│   │       ├── reminder/         # 提醒中心
│   │       ├── statistic/        # 统计图表
│   │       ├── settings/         # 系统设置（9个子页面）
│   │       └── login/            # 登录页
│   ├── vite.config.ts
│   └── package.json
│
├── 合同管理系统-设计方案.md        # 完整设计方案文档
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
# 创建数据库（或启用自动创建）
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
前端: http://localhost:3000
后端 API: http://localhost:8080
默认账号: admin / 123456
```

---

## 🎭 Mock 模式（无后端运行）

编辑 `contract-web/.env`：

```
# 启用 Mock（前端独立运行，无需后端）
VITE_USE_MOCK=true

# 连接真实后端（通过 Vite Proxy）
VITE_USE_MOCK=false
```

Mock 模式会拦截所有 `/api/*` 请求并返回模拟数据，适合前端独立开发或演示。

---

## 🐳 Docker 部署（可选）

```bash
# 构建后端镜像
cd contract-server
docker build -t contract-server .

# 构建前端镜像
cd contract-web
docker build -t contract-web .

# 使用 docker-compose 启动
docker-compose up -d
```

---

## 📊 数据模型核心关系

```
客户/供应商 ──→ 项目 ──→ 应收合同 ──→ 销项发票
                              ├──→ 收款计划 ──→ 收款记录
                              └──→ (关联采购) 利润计算
                        
供应商/客户 ──→ 应付合同 ──→ 进项发票
                   ├──→ 付款计划 ──→ 付款记录
                   └──→ (关联应收) 成本核算

用户 ── 角色 ── 菜单权限
用户 ── 公司（多对多）
用户 ── 部门 ── 岗位
```

---

## 📄 许可证

MIT License

## 👥 贡献

欢迎提交 Issue 或 Pull Request。
