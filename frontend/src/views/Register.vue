<template>
  <div class="register-container">
    <el-card class="register-card">
      <div slot="header" class="card-header">
        <span>用户注册</span>
      </div>
      <el-form :model="registerForm" :rules="registerRules" ref="registerForm" label-width="80px">
        <el-form-item label="员工ID" prop="employeeId">
          <el-input v-model="registerForm.employeeId" placeholder="请输入员工ID"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="registerForm.department" placeholder="请输入部门"></el-input>
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="registerForm.position" placeholder="请输入职位"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading">注册</el-button>
          <el-button @click="goToLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { register } from '@/api/user'

export default {
  name: 'Register',
  data() {
    return {
      registerForm: {
        employeeId: '',
        name: '',
        username: '',
        password: '',
        department: '',
        position: '',
        email: '',
        phone: ''
      },
      registerRules: {
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
      },
      loading: false
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          this.loading = true
          console.log('Sending register request with data:', this.registerForm)
          register(this.registerForm)
            .then(response => {
              this.loading = false
              console.log('Register response:', response)
              const { success, message } = response.data
              if (success) {
                this.$message.success('注册成功')
                // 跳转到登录页
                this.$router.push('/login')
              } else {
                this.$message.error(message || '注册失败')
              }
            })
            .catch(error => {
              this.loading = false
              console.error('Register error:', error)
              this.$message.error('注册失败: ' + (error.message || '网络错误'))
            })
        }
      })
    },
    goToLogin() {
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  overflow-y: auto;
}

.register-card {
  width: 500px;
}

.card-header {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}
</style>