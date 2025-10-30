<template>
  <div class="register-container">
    <div class="register-wrapper">
      <div class="register-left">
        <div class="welcome-text">
          <h1>欢迎加入GF职业空间</h1>
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
      <div class="register-right">
        <el-card class="register-card">
          <div class="card-header">
            <h2>用户注册</h2>
            <p>请填写您的基本信息</p>
          </div>
          <el-form 
            :model="registerForm" 
            :rules="registerRules" 
            ref="registerFormRef" 
            label-width="0px"
          >
            <el-form-item prop="employeeId">
              <el-input 
                v-model="registerForm.employeeId" 
                placeholder="请输入员工ID" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="name">
              <el-input 
                v-model="registerForm.name" 
                placeholder="请输入姓名" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="username">
              <el-input 
                v-model="registerForm.username" 
                placeholder="请输入用户名" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input 
                v-model="registerForm.password" 
                type="password" 
                placeholder="请输入密码" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="department">
              <el-input 
                v-model="registerForm.department" 
                placeholder="请输入部门" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="position">
              <el-input 
                v-model="registerForm.position" 
                placeholder="请输入职位" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="email">
              <el-input 
                v-model="registerForm.email" 
                placeholder="请输入邮箱" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="phone">
              <el-input 
                v-model="registerForm.phone" 
                placeholder="请输入电话" 
                size="large"
              >
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-button 
                type="primary" 
                @click="handleRegister" 
                :loading="loading" 
                size="large" 
                class="register-button"
                style="width: 100%"
              >
                注册
              </el-button>
            </el-form-item>
            <div class="login-link">
              <span>已有账号？</span>
              <el-button type="text" @click="goToLogin">立即登录</el-button>
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
import { Document, Medal, Lightning } from '@element-plus/icons-vue'
import { register } from '@/api/user'

export default {
  name: 'Register',
  components: {
    Document,
    Medal,
    Lightning
  },
  setup() {
    const router = useRouter()
    const registerFormRef = ref(null)
    const loading = ref(false)
    
    const registerForm = reactive({
      employeeId: '',
      name: '',
      username: '',
      password: '',
      department: '',
      position: '',
      email: '',
      phone: ''
    })
    
    const registerRules = {
      employeeId: [
        { required: true, message: '请输入员工ID', trigger: 'blur' }
      ],
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' }
      ],
      username: [
        { required: true, message: '请输入用户名', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }

    const handleRegister = async () => {
      if (!registerFormRef.value) return
      
      await registerFormRef.value.validate(async (valid) => {
        if (valid) {
          try {
            loading.value = true
            const response = await register(registerForm)
            const { success, message } = response.data
            
            if (success) {
              ElMessage.success('注册成功')
              // 跳转到登录页
              router.push('/login')
            } else {
              ElMessage.error(message || '注册失败')
            }
          } catch (error) {
            console.error('Register error:', error)
            ElMessage.error('注册失败: ' + (error.message || '网络错误'))
          } finally {
            loading.value = false
          }
        }
      })
    }

    const goToLogin = () => {
      router.push('/login')
    }

    return {
      registerForm,
      registerRules,
      registerFormRef,
      loading,
      handleRegister,
      goToLogin
    }
  }
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-wrapper {
  display: flex;
  width: 1000px;
  height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.register-left {
  flex: 1;
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
  color: white;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.register-left h1 {
  font-size: 32px;
  margin-bottom: 15px;
  font-weight: 600;
}

.register-left p {
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

.register-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  overflow-y: auto;
}

.register-card {
  width: 100%;
  max-width: 400px;
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

.register-button {
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
  border: none;
  font-weight: 500;
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
  font-size: 14px;
}

.login-link .el-button {
  color: #ff6b35;
  font-weight: 500;
}
</style>