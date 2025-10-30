# GF职业空间

## 项目简介

GF职业空间是一个记录员工职场成长过程的系统。该系统允许员工上传和查询各类里程碑事件，如入职培训、转正、部门调整、职位晋升、参赛荣誉、重大项目等，并提供职业成长相关信息的查询服务。

## 技术栈

### 后端
- Spring Boot 3.2.0
- MyBatis Plus
- H2 Database (模拟数据源)

### 前端
- Vue 3
- Element Plus
- Vite

## 项目结构

```
xinghe/
├── backend/              # 后端服务
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/gf/career/space/
│   │   │   │       ├── controller/     # 控制器层
│   │   │   │       ├── entity/         # 实体类
│   │   │   │       ├── mapper/         # 数据访问层
│   │   │   │       ├── service/        # 业务逻辑层
│   │   │   │       └── CareerSpaceApplication.java  # 启动类
│   │   │   └── resources/
│   │   │       ├── application.yml     # 配置文件
│   │   │       └── static/            # 静态资源
│   │   └── test/                      # 测试代码
│   └── pom.xml                        # Maven配置文件
│
├── frontend/            # 前端应用
│   ├── public/          # 公共资源
│   ├── src/
│   │   ├── assets/      # 静态资源
│   │   ├── components/  # 组件
│   │   ├── views/       # 页面视图
│   │   ├── router/      # 路由配置
│   │   ├── api/         # API接口
│   │   ├── utils/       # 工具类
│   │   ├── App.vue      # 根组件
│   │   └── main.js      # 入口文件
│   ├── index.html       # HTML模板
│   ├── vite.config.js   # Vite配置
│   └── package.json     # npm配置
│
└── README.md            # 项目说明文档
```

## 功能模块

### 1. 员工管理
- 员工信息的增删改查

### 2. 里程碑事件管理
- 记录员工的各类里程碑事件：
  - 入职培训
  - 转正
  - 部门调整
  - 职位晋升
  - 参赛荣誉
  - 重大项目

### 3. 证书与证明管理
- 年度考核证明
- 在职联系人证明
- 荣誉证明

### 4. 智能服务
- 职业发展建议
- 学习路径推荐
- 简历优化建议

## 环境要求

- Java 17+
- Node.js 16+
- Maven 3.8+

## 运行项目

### 后端服务

```bash
# 进入后端目录
cd backend

# 编译项目
mvn clean install

# 运行项目
mvn spring-boot:run
```

### 前端应用

```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

## 访问地址

- 后端API: http://localhost:8080
- 前端页面: http://localhost:3000
- H2控制台: http://localhost:8080/h2-console

## UI/UX 设计规范

### 配色方案
- 主色调：橙色 (#ff6b35) - 活力、温暖
- 辅色：蓝色 (#1a73e8) - 可靠、安全
- 辅助色：白色和浅灰色

### 字体规范
- 默认字体：微信标准字体
- 重点信息：加粗或较大字号

### 设计原则
- 流程清晰，操作简单
- 重点突出（尤其是安全认证信息）
- 减少用户思考成本

## 数据库设计

### 员工表 (employee)
| 字段名 | 类型 | 描述 |
|-------|------|-----|
| id | BIGINT | 主键 |
| employee_id | VARCHAR | 员工工号 |
| name | VARCHAR | 姓名 |
| department | VARCHAR | 部门 |
| position | VARCHAR | 职位 |
| hire_date | DATETIME | 入职日期 |
| email | VARCHAR | 邮箱 |
| phone | VARCHAR | 电话 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 里程碑表 (milestone)
| 字段名 | 类型 | 描述 |
|-------|------|-----|
| id | BIGINT | 主键 |
| employee_id | BIGINT | 员工ID |
| title | VARCHAR | 标题 |
| description | VARCHAR | 描述 |
| type | VARCHAR | 类型（入职培训、转正等） |
| event_date | DATETIME | 事件日期 |
| attachment_url | VARCHAR | 附件链接 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

### 证书表 (certificate)
| 字段名 | 类型 | 描述 |
|-------|------|-----|
| id | BIGINT | 主键 |
| employee_id | BIGINT | 员工ID |
| certificate_type | VARCHAR | 证书类型 |
| title | VARCHAR | 标题 |
| description | VARCHAR | 描述 |
| issue_date | DATETIME | 颁发日期 |
| issuer | VARCHAR | 颁发机构 |
| certificate_url | VARCHAR | 证书链接 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |