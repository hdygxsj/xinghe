<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="welcome-text">
          <h1 class="animated-title">欢迎来到GF职业空间</h1>
          <p class="animated-subtitle">记录您的职业成长足迹，规划未来发展路径</p>
          <div class="features">
            <div class="feature-item" style="animation-delay: 0.5s">
              <el-icon><Document /></el-icon>
              <span>历程记录</span>
            </div>
            <div class="feature-item" style="animation-delay: 0.7s">
              <el-icon><Medal /></el-icon>
              <span>证书管理</span>
            </div>
            <div class="feature-item" style="animation-delay: 0.9s">
              <el-icon><Lightning /></el-icon>
              <span>智能助手</span>
            </div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <el-card class="login-card animated-card">
          <div class="card-header">
            <h2>用户登录</h2>
            <p>请输入您的账号信息</p>
          </div>
          <el-form 
            :model="loginForm" 
            :rules="loginRules" 
            ref="loginFormRef" 
            label-width="0px"
            @keyup.enter="handleLogin"
          >
            <el-form-item prop="username">
              <el-input 
                v-model="loginForm.username" 
                placeholder="请输入用户名" 
                size="large"
                :prefix-icon="User"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input 
                v-model="loginForm.password" 
                type="password" 
                placeholder="请输入密码" 
                size="large"
                :prefix-icon="Lock"
              >
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleLogin" 
                :loading="loading" 
                size="large" 
                class="login-button"
                style="width: 100%"
              >
                登录
              </el-button>
            </el-form-item>
            <div class="register-link">
              <span>还没有账号？</span>
              <el-button type="text" @click="goToRegister">立即注册</el-button>
            </div>
          </el-form>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Document, Medal, Lightning, User, Lock } from '@element-plus/icons-vue'
import { login } from '@/api/user'

export default {
  name: 'Login',
  components: {
    Document,
    Medal,
    Lightning,
    User,
    Lock
  },
  setup() {
    const router = useRouter()
    const loginFormRef = ref(null)
    const loading = ref(false)
    
    const loginForm = reactive({
      username: '',
      password: ''
    })
    
    const loginRules = {
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }

    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      await loginFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            loading.value = true
            const response = await login(loginForm)
            const { success, message, data, token } = response.data
            
            if (success) {
              // 保存用户信息和token到本地存储
              localStorage.setItem('currentUser', JSON.stringify(data))
              localStorage.setItem('token', token)
              
              // 手动触发storage事件，通知其他组件登录状态已改变
              window.dispatchEvent(new StorageEvent('storage', {
                key: 'currentUser',
                newValue: JSON.stringify(data)
              }))
              
              ElMessage.success('登录成功')
              // 根据用户角色跳转到不同页面
              if (data.role === 'ADMIN') {
                router.push('/hr-dashboard')
              } else {
                router.push('/')
              }
            } else {
              ElMessage.error(message || '登录失败')
            }
          } catch (error) {
            console.error('Login error:', error)
            ElMessage.error('登录失败: ' + (error.message || '网络错误'))
          } finally {
            loading.value = false
          }
        }
      })
    }

    const goToRegister = () => {
      router.push('/register')
    }

    return {
      loginForm,
      loginRules,
      loginFormRef,
      loading,
      handleLogin,
      goToRegister,
      User,
      Lock
    }
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: linear-gradient(135deg, #f5f7ff 0%, #e8ecff 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.login-wrapper {
  display: flex;
  width: 900px;
  height: 500px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 15px 50px rgba(102, 126, 234, 0.15);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-left::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.login-left h1 {
  font-size: 32px;
  margin-bottom: 15px;
  font-weight: 600;
  position: relative;
  z-index: 1;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.15);
  letter-spacing: 1px;
}

.animated-title {
  animation: fadeInDown 1s ease-out;
}

.animated-subtitle {
  animation: fadeInUp 1s ease-out 0.3s both;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideInLeft {
  from {
    opacity: 0;
    transform: translateX(-30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.login-left p {
  font-size: 16px;
  margin-bottom: 30px;
  opacity: 0.95;
  position: relative;
  z-index: 1;
  letter-spacing: 0.5px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
  position: relative;
  z-index: 1;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 16px;
  padding: 10px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  animation: slideInLeft 0.8s ease-out both;
  opacity: 0;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(5px);
}

.feature-item i {
  font-size: 24px;
}

.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: linear-gradient(to bottom, #fafbff 0%, #ffffff 100%);
}

.login-card {
  width: 100%;
  max-width: 350px;
  border: none;
  box-shadow: none;
  animation: fadeIn 1s ease-out 0.4s both;
}

.animated-card {
  opacity: 0;
}

.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.card-header h2 {
  font-size: 24px;
  color: #2d3748;
  margin-bottom: 10px;
  font-weight: 600;
}

.card-header p {
  color: #4a5568;
  font-size: 14px;
}

.login-button {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s ease;
}

.login-button:hover::before {
  left: 100%;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #4a5568;
  font-size: 14px;
}

.register-link .el-button {
  color: #667eea;
  font-weight: 500;
}

.register-link .el-button:hover {
  color: #764ba2;
}

/* 输入框样式调整 */
:deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.1);
  transition: all 0.3s ease;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.2);
}
</style>