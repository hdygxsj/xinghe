<template>
  <div id="app">
    <el-container>
      <el-header class="header">
        <div class="logo">GF职业空间</div>
        <div class="nav">
          <el-menu mode="horizontal" :default-active="activeIndex" @select="handleSelect" v-if="isLoggedIn">
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">我的历程</el-menu-item>
            <el-menu-item index="3">证书证明</el-menu-item>
            <el-menu-item index="4">智能助手</el-menu-item>
            <el-menu-item index="5">导师管理</el-menu-item>
            <el-menu-item index="6">职业规划</el-menu-item>
            <el-menu-item index="7">技能图谱</el-menu-item>
          </el-menu>
        </div>
        <div class="user-info" v-if="isLoggedIn">
          <span>欢迎, {{ currentUser.name }}</span>
          <el-button @click="handleLogout" type="text">退出</el-button>
        </div>
        <div class="auth-buttons" v-else>
          <el-button @click="goToLogin">登录</el-button>
          <el-button @click="goToRegister">注册</el-button>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
      <el-footer class="footer">
        <p>© 2025 GF职业空间 - 记录您的职业成长足迹</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'App',
  data() {
    return {
      activeIndex: '1',
      currentUser: null,
      isLoggedIn: false
    }
  },
  created() {
    // Check if user is already logged in
    const user = localStorage.getItem('currentUser')
    if (user) {
      this.currentUser = JSON.parse(user)
      this.isLoggedIn = true
    }

    // Redirect to login if not authenticated and trying to access protected routes
    this.$router.beforeEach((to, from, next) => {
      const publicPages = ['/login', '/register']
      const authRequired = !publicPages.includes(to.path)
      const loggedIn = localStorage.getItem('currentUser')

      if (authRequired && !loggedIn) {
        return next('/login')
      }

      next()
    })
  },
  methods: {
    handleSelect(key) {
      this.activeIndex = key
  setup() {
    const activeIndex = ref('1')
    const router = useRouter()

    const handleSelect = (key) => {
      activeIndex.value = key
      switch (key) {
        case '1':
          router.push('/')
          break
        case '2':
          router.push('/milestones')
          break
        case '3':
          router.push('/certificates')
          break
        case '4':
          router.push('/ai-assistant')
          break
        case '5':
          router.push('/mentors')
          break
        case '6':
          router.push('/career-plans')
          break
        case '7':
          router.push('/skills')
          break
      }
    },
    handleLogout() {
      localStorage.removeItem('currentUser')
      this.currentUser = null
      this.isLoggedIn = false
      this.$router.push('/login')
    },
    goToLogin() {
      this.$router.push('/login')
    },
    goToRegister() {
      this.$router.push('/register')
    }

    return {
      activeIndex,
      handleSelect
    }
  }
}
</script>

<style>
.header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #ff6b35;
}

.nav {
  flex-grow: 1;
  margin-left: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.auth-buttons {
  display: flex;
  gap: 10px;
}

.footer {
  text-align: center;
  padding: 20px;
  background-color: #f5f5f5;
}

.el-main {
  min-height: calc(100vh - 120px);
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
}
</style>