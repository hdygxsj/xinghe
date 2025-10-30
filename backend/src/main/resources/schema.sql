-- 员工表
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    position VARCHAR(100),
    hire_date DATETIME,
    email VARCHAR(100),
    phone VARCHAR(20),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'USER',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 里程碑表
CREATE TABLE IF NOT EXISTS milestone (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL,
    event_date DATETIME,
    attachment_url VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

-- 证书表
CREATE TABLE IF NOT EXISTS certificate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    certificate_type VARCHAR(100) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    issue_date DATETIME,
    issuer VARCHAR(100),
    certificate_url VARCHAR(500),
    verification_code VARCHAR(20),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    expiry_date DATETIME,
    tags VARCHAR(500),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

-- 导师表
CREATE TABLE IF NOT EXISTS mentor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    position VARCHAR(100),
    expertise VARCHAR(200),
    email VARCHAR(100),
    phone VARCHAR(20),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 职业规划表
CREATE TABLE IF NOT EXISTS career_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    target_position VARCHAR(100),
    target_date DATETIME,
    status VARCHAR(20) DEFAULT 'DRAFT',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

-- 技能表
CREATE TABLE IF NOT EXISTS skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    description TEXT,
    level INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 使用INSERT IGNORE方式插入技能类型示例数据（避免主键冲突）
INSERT INTO skill (name, category, description, level) SELECT 'Java编程', '技术技能', 'Java编程语言的掌握程度', 3 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = 'Java编程');
INSERT INTO skill (name, category, description, level) SELECT '项目管理', '软技能', '项目规划、执行和监控的能力', 4 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '项目管理');
INSERT INTO skill (name, category, description, level) SELECT '数据分析', '技术技能', '使用统计方法和工具分析数据的能力', 2 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '数据分析');
INSERT INTO skill (name, category, description, level) SELECT '沟通能力', '软技能', '有效沟通和表达的能力', 5 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '沟通能力');
INSERT INTO skill (name, category, description, level) SELECT '团队协作', '软技能', '在团队中有效合作的能力', 4 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '团队协作');
INSERT INTO skill (name, category, description, level) SELECT '英语能力', '语言技能', '英语听说读写能力', 3 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '英语能力');
INSERT INTO skill (name, category, description, level) SELECT '产品设计', '技术技能', '产品设计和用户体验设计能力', 3 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '产品设计');
INSERT INTO skill (name, category, description, level) SELECT '领导力', '软技能', '带领团队和影响他人的能力', 3 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '领导力');
INSERT INTO skill (name, category, description, level) SELECT '财务分析', '专业技能', '财务报表分析和财务规划能力', 2 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '财务分析');
INSERT INTO skill (name, category, description, level) SELECT '市场营销', '专业技能', '市场调研和营销策略制定能力', 3 WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '市场营销');

-- 员工技能表
CREATE TABLE IF NOT EXISTS employee_skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency_level INT DEFAULT 1,
    acquired_date DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skill(id) ON DELETE CASCADE
);

-- 导师关系表
CREATE TABLE IF NOT EXISTS mentorship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mentor_id BIGINT NOT NULL,
    mentee_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'REQUESTED',
    goals TEXT,
    start_date DATETIME,
    end_date DATETIME,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (mentor_id) REFERENCES mentor(id) ON DELETE CASCADE,
    FOREIGN KEY (mentee_id) REFERENCES employee(id) ON DELETE CASCADE
);

-- 角色表
CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);