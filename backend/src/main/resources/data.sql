-- 初始化员工数据
INSERT INTO employee (employee_id, name, department, position, hire_date, email, phone) VALUES
('1', '张三', '技术部', '高级工程师', '2020-01-15 09:00:00', 'zhangsan@company.com', '13800138001'),
('2', '李四', '市场部', '市场经理', '2019-03-22 09:00:00', 'lisi@company.com', '13800138002'),
('3', '王五', '人事部', 'HR专员', '2021-07-10 09:00:00', 'wangwu@company.com', '13800138003');

-- 初始化里程碑数据
INSERT INTO milestone (employee_id, title, description, type, event_date) VALUES
(1, '入职培训完成', '完成了新员工入职培训', '入职培训', '2020-01-15 17:00:00'),
(1, '转正', '通过试用期考核，正式转正', '转正', '2020-04-15 00:00:00'),
(1, '晋升为高级工程师', '因项目表现优秀，晋升为高级工程师', '职位晋升', '2022-01-20 00:00:00'),
(2, '入职培训完成', '完成了新员工入职培训', '入职培训', '2019-03-22 17:00:00'),
(2, '转正', '通过试用期考核，正式转正', '转正', '2019-06-22 00:00:00'),
(3, '入职培训完成', '完成了新员工入职培训', '入职培训', '2021-07-10 17:00:00');

-- 初始化证书数据
INSERT INTO certificate (employee_id, certificate_type, title, description, issue_date, issuer) VALUES
(1, '年度考核证明', '2021年度考核证明', '该员工在2021年度表现优秀，考核结果为合格', '2022-01-15 00:00:00', '人力资源部'),
(1, '荣誉证明', '优秀员工奖', '获得2021年度优秀员工奖', '2022-01-20 00:00:00', '人力资源部'),
(2, '年度考核证明', '2020年度考核证明', '该员工在2020年度表现优秀，考核结果为合格', '2021-01-15 00:00:00', '人力资源部');

-- 初始化导师数据
INSERT INTO mentor (name, department, position, expertise, email, phone) VALUES
('赵六', '技术部', '技术总监', 'Java开发,系统架构', 'zhaoliu@company.com', '13800138004'),
('孙七', '市场部', '市场总监', '市场营销,品牌推广', 'sunqi@company.com', '13800138005'),
('周八', '人事部', 'HR经理', '人力资源管理,员工培训', 'zhouba@company.com', '13800138006');

-- 初始化技能数据
INSERT INTO skill (name, category, description, level) VALUES
('Java编程', '技术', 'Java语言编程技能', 5),
('Spring框架', '技术', 'Spring框架使用经验', 4),
('数据库设计', '技术', '数据库设计与优化', 4),
('项目管理', '管理', '项目规划与执行管理', 3),
('团队协作', '沟通', '团队合作与协调能力', 5),
('沟通表达', '沟通', '清晰表达与沟通技巧', 4);

-- 初始化员工技能数据
INSERT INTO employee_skill (employee_id, skill_id, proficiency_level, acquired_date) VALUES
(1, 1, 5, '2020-01-15 00:00:00'),
(1, 2, 4, '2020-04-15 00:00:00'),
(1, 3, 4, '2020-07-15 00:00:00'),
(2, 5, 5, '2019-03-22 00:00:00'),
(2, 6, 4, '2019-06-22 00:00:00'),
(3, 5, 4, '2021-07-10 00:00:00'),
(3, 6, 5, '2021-10-10 00:00:00');

-- 初始化导师关系数据
INSERT INTO mentorship (mentor_id, mentee_id, status, goals, start_date) VALUES
(1, 1, 'ACCEPTED', '提升技术架构能力', '2022-02-01 00:00:00'),
(2, 2, 'ACCEPTED', '提升市场分析能力', '2022-03-01 00:00:00');

-- 初始化职业规划数据
INSERT INTO career_plan (employee_id, title, description, target_position, target_date, status) VALUES
(1, '技术专家发展路径', '计划在3年内成为技术专家', '技术专家', '2025-01-15 00:00:00', 'ACTIVE'),
(2, '市场总监发展路径', '计划在5年内成为市场总监', '市场总监', '2026-03-22 00:00:00', 'ACTIVE');