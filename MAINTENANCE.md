# 合同管理系统 — 维护手册

> 企业级双向合同（应收/应付）全生命周期管理平台

---

## 1. 📁 项目结构

```
contract-admin/
├── contract-server/          # Spring Boot 3 后端
│   ├── config/
│   │   └── application.yml   # ← 外部配置文件（修改无需重新打包）
│   ├── target/
│   │   └── contract-server-1.0.0.jar  # 构建产物
│   ├── pom.xml
│   └── src/main/java/com/contract/
│       ├── ContractApplication.java
│       ├── config/            # JWT / Security / CORS / AOP / 定时任务
│       ├── controller/        # REST API 控制器
│       ├── entity/            # JPA 实体（19个）
│       ├── repository/        # 数据访问层（19个）
│       └── common/            # 统一响应 Result<T>
│
└── contract-web/             # Vue 3 + TypeScript 前端
    ├── dist/                  # 构建产物（46个文件）
    ├── src/
    │   ├── api/               # Axios API 封装
    │   ├── components/        # 通用组件
    │   ├── router/            # 路由（30+ 页面）
    │   ├── stores/            # Pinia 状态管理
    │   ├── utils/             # 工具函数
    │   └── views/             # 页面视图
    ├── vite.config.ts
    └── package.json
```

---

## 2. 🔧 环境要求

| 工具 | 版本 | 说明 |
|------|------|------|
| **Java** | 17+ | JDK 17（`C:\Program Files\Java\jdk-17`） |
| **Maven** | 3.9+ | 位置: `C:\Program Files\apache-maven-3.9.15` |
| **Node.js** | 18+ | v24.15.0 |
| **npm** | 10+ | 11.12.1 |
| **MySQL** | 8.0 | 端口 3306，数据库 `contract_db` |

> **注意**: 系统中同时有 Java 8，必须在 PATH 中优先使用 Java 17。

---

## 3. 🚀 启动项目

### 3.1 启动后端

```bash
cd contract-server

# 方式 A：Maven 开发模式（热加载）
export JAVA_HOME="/c/Program Files/Java/jdk-17"
export MAVEN_HOME="/c/Program Files/apache-maven-3.9.15"
export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"
mvn spring-boot:run

# 方式 B：JAR 包运行（生产模式）
mvn clean package -DskipTests
java -jar target/contract-server-1.0.0.jar

# 方式 C：使用外部配置
java -jar target/contract-server-1.0.0.jar \
  --spring.config.location=file:./config/application.yml
```

### 3.2 启动前端

```bash
cd contract-web
npm install        # 首次或依赖变更后执行
npm run dev        # 开发模式（localhost:3000，代理 /api → :8080）
npm run build      # 生产构建 → dist/
```

### 3.3 访问

| 页面 | 地址 |
|------|------|
| 前端 | http://localhost:3000 |
| 后端 API | http://localhost:8080 |
| 默认账号 | `admin` / `123456` |

---

## 4. ⚙️ 配置说明

### 4.1 外部配置（`contract-server/config/application.yml`）

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/contract_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update     # 自动建表/更新（开发用），生产推荐 validate
    show-sql: false

app:
  jwt:
    secret: <JWT密钥>
    expiration-ms: 86400000  # 24小时

logging:
  level:
    com.contract: debug
```

> **Spring Boot 配置加载顺序（高 → 低）**:
> 1. `config/application.yml`（当前目录，`contract-server/config/`）
> 2. `application.yml`（当前目录）
> 3. classpath 内部 `config/application.yml`
> 4. classpath 内部 `application.yml`

### 4.2 前端代理（`contract-web/vite.config.ts`）

```ts
server: {
  port: 3000,
  proxy: {
    '/api': { target: 'http://localhost:8080', changeOrigin: true }
  }
}
```

---

## 5. 🗄️ 数据库

### 5.1 初始化

首次启动时 `DataInitializer.java` 自动执行：
- 创建 admin 用户（密码 `123456`）
- 创建默认公司
- 初始化字典数据（合同类型、税率、支付方式等 11 类）
- 创建角色/部门/岗位
- 创建示例业务数据（5 个应收合同、3 个应付合同、发票、收付款记录）

### 5.2 实体关系

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

### 5.3 JPA 配置

`ddl-auto: update` — 实体变更后自动更新表结构。
生产环境建议改为 `validate` 并用 FlyWay/Liquibase 管理迁移。

---

## 6. 🔐 认证与安全

- **认证方式**: JWT（jjwt 0.12.5）
- **登录接口**: `POST /api/auth/login`，返回 token
- **请求头**: `Authorization: Bearer <token>`
- **过滤器**: `JwtAuthFilter` 验证所有 `/api/*` 请求
- **密码加密**: Spring Security `PasswordEncoder`（BCrypt）
- **密码过期注意**: Spring Boot 启动日志中的 `Using generated security password` 不影响 JWT 登录

---

## 7. 📈 关键 API

| 接口 | 用途 | 返回数据 |
|------|------|----------|
| `GET /api/statistics/chart` | 图表分析数据 | trendLabels, trend, monthlyIncome/Expense, 类型分布, 状态分析 |
| `GET /api/statistics/overview` | 经营明细概览 | summary 卡片, receivable/payable 表格 |
| `GET /api/statistics/projects` | 项目统计 | 项目列表 + 收支利润 |
| `GET /api/dashboard/summary` | 仪表盘 | 统计卡片, trendMonths/Values, 状态分布, 提醒, 交易 |
| `POST /api/auth/login` | 登录 | token + 用户信息 |

### 年度合同趋势数据流

```
dashboard 中的 "合同金额趋势" 和 statistic 中的 "年度合同趋势"
均从 contractRepository.findAll() 计算:
  → 过滤 receivable 合同
  → 按 signDate.getYear() 分组
  → 求和 contractAmount
  → 输出当前年份往前 6 年
```

> **注意**: 之前在 `Statistics.vue` 中存在硬编码的 `yearLabels` 后备数组，已经移除。
> 现在图表只显示 API 返回的真实数据。

---

## 8. 🧩 常见问题

### 8.1 Java 版本冲突

**症状**: `mvn` 报错要求 Java 17，但 `java -version` 显示 1.8

**解决**:
```bash
export JAVA_HOME="/c/Program Files/Java/jdk-17"
export PATH="$JAVA_HOME/bin:$PATH"
```

### 8.2 Maven 找不到

**症状**: `mvn: command not found`

**解决**: Maven 位置 `/c/Program Files/apache-maven-3.9.15/bin/mvn`
```bash
export MAVEN_HOME="/c/Program Files/apache-maven-3.9.15"
export PATH="$MAVEN_HOME/bin:$PATH"
```

### 8.3 mvnw.cmd 循环递归

**症状**: `setlocal 递归达到最大值`

**原因**: Git Bash 中运行 `cmd //c mvnw.cmd` 时 JAVA_HOME 传递有问题

**解决**: 使用系统安装的 Maven（见 8.2）而非 `mvnw.cmd`

### 8.4 数据库连接失败

**症状**: 启动报错 `HikariPool-1 - Start completed` 后无法查询

**解决**:
1. 确认 MySQL 运行中: `netstat -an | findstr 3306`
2. 确认数据库存在: `mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS contract_db CHARACTER SET utf8mb4;"`
3. 修改 `config/application.yml` 中的连接信息

### 8.5 前端代理 403

**症状**: 前端页面能打开，但 API 请求返回 403

**解决**: JWT token 过期或未正确传递。重新登录获取新 token。

### 8.6 前端接口返回数据中文字符乱码

**症状**: 终端 curl 显示 Unicode 转义如 `\u93c0\u8235`

**原因**: 终端编码问题，实际 HTTP 响应编码正常，不影响浏览器展示。

---

## 9. 📦 打包部署

### 9.1 构建

```bash
# 后端
cd contract-server
export JAVA_HOME="/c/Program Files/Java/jdk-17"
export MAVEN_HOME="/c/Program Files/apache-maven-3.9.15"
export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"
mvn clean package -DskipTests
# → target/contract-server-1.0.0.jar (53MB)

# 前端
cd contract-web
npm run build
# → dist/ (46个文件，含 HTML + JS/CSS assets)
```

### 9.2 部署

```bash
# 后端
java -jar contract-server/target/contract-server-1.0.0.jar

# 前端（静态文件服务）
cd contract-web
npx vite preview --port 3000
# 或用 Nginx 指向 dist/ 目录
```

---

## 10. 🔄 维护检查清单

- [ ] 确认 Java 17 在 PATH 中优先
- [ ] 确认 Maven 3.9+ 可用
- [ ] 确认 MySQL 3306 端口监听中
- [ ] 确认 `contract_db` 数据库存在
- [ ] 外部配置 `contract-server/config/application.yml` 数据库连接信息正确
- [ ] `ddl-auto: update` 仅用于开发，生产请改为 `validate`
- [ ] 前端 `npm install` 后再 `npm run dev`
- [ ] 前端 `/api` 代理指向正确的后端地址
- [ ] JWT secret 生产环境应更换为强随机字符串

---

## 11. 📝 版本历史

| 日期 | 变更 |
|------|------|
| 2026-06-30 | 初始维护手册创建 |
| 2026-06-30 | 修复年度合同趋势图表硬编码数据问题 |
| 2026-06-30 | 添加 Maven 3.9.15 和 JDK 17 发现路径 |
