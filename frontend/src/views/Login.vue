<template>
  <div class="login-container">
    <el-card class="login-card">
      <div slot="header" class="card-header">
        <span>用户登录</span>
      </div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading">登录</el-button>
          <el-button @click="goToRegister">注册</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { login } from '@/api/user'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true
          console.log('Sending login request with data:', this.loginForm)
          login(this.loginForm)
            .then(response => {
              this.loading = false
              console.log('Login response:', response)
              const { success, message, data } = response.data
              if (success) {
                // 保存用户信息到本地存储
                localStorage.setItem('currentUser', JSON.stringify(data))
                this.$message.success('登录成功')
                // 跳转到首页
                this.$router.push('/')
              } else {
                this.$message.error(message || '登录失败')
              }
            })
            .catch(error => {
              this.loading = false
              console.error('Login error:', error)
              this.$message.error('登录失败: ' + (error.message || '网络错误'))
            })
        }
      })
    },
    goToRegister() {
      this.$router.push('/register')
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}
</style>