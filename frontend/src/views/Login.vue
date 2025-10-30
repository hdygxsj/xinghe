<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="welcome-text">
          <h1>欢迎来到GF职业空间</h1>
          <p>记录您的职业成长足迹，规划未来发展路径</p>
          <div class="features">
            <div class="feature-item">
              <el-icon><Document /></el-icon>
              <span>历程记录</span>
            </div>
            <div class="feature-item">
              <el-icon><Medal /></el-icon>
              <span>证书管理</span>
            </div>
            <div class="feature-item">
              <el-icon><Lightning /></el-icon>
              <span>智能助手</span>
            </div>
          </div>
        </div>
      </div>
      <div class="login-right">
        <el-card class="login-card">
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
              ElMessage.success('登录成功')
              // 跳转到首页
              router.push('/')
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
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-wrapper {
  display: flex;
  width: 900px;
  height: 500px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
  color: white;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-left h1 {
  font-size: 32px;
  margin-bottom: 15px;
  font-weight: 600;
}

.login-left p {
  font-size: 16px;
  margin-bottom: 30px;
  opacity: 0.9;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 16px;
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
}

.login-card {
  width: 100%;
  max-width: 350px;
  border: none;
  box-shadow: none;
}

.card-header {
  text-align: center;
  margin-bottom: 30px;
}

.card-header h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 10px;
}

.card-header p {
  color: #666;
  font-size: 14px;
}

.login-button {
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
  border: none;
  font-weight: 500;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.register-link .el-button {
  color: #ff6b35;
  font-weight: 500;
}
</style>