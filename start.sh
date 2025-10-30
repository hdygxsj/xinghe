#!/bin/bash

echo "正在启动GF职业空间项目..."

# 启动后端服务
echo "正在启动后端服务..."
cd backend
mvn spring-boot:run &
BACKEND_PID=$!
cd ..

# 等待后端服务启动
sleep 10

# 启动前端服务
echo "正在启动前端服务..."
cd frontend
npm run dev &
FRONTEND_PID=$!
cd ..

echo "项目启动完成！"
echo "后端服务地址: http://localhost:8080"
echo "前端服务地址: http://localhost:3000"
echo "H2控制台: http://localhost:8080/h2-console"

# 等待用户按键
echo "按任意键停止服务..."
read -n 1 -s

# 停止服务
echo "正在停止服务..."
kill $BACKEND_PID
kill $FRONTEND_PID

echo "服务已停止"