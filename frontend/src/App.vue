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
            <el-menu-item index="7">技能管理</el-menu-item>
            <el-menu-item index="8" v-if="isAdmin">角色管理</el-menu-item>
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
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

export default {
  name: 'App',
  setup() {
    const activeIndex = ref('1')
    const currentUser = ref(null)
    const isLoggedIn = ref(false)
    const isAdmin = ref(false)
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
        case '8':
          router.push('/roles')
          break
      }
    }

    const handleLogout = () => {
      localStorage.removeItem('currentUser')
      localStorage.removeItem('token')
      currentUser.value = null
      isLoggedIn.value = false
      // 移除axios默认头部的Authorization
      delete axios.defaults.headers.common['Authorization']
      router.push('/login')
    }

    const goToLogin = () => {
      router.push('/login')
    }

    const goToRegister = () => {
      router.push('/register')
    }

    const checkLoginStatus = () => {
      // Check if user is already logged in
      const user = localStorage.getItem('currentUser')
      const token = localStorage.getItem('token')
      
      if (user && token) {
        currentUser.value = JSON.parse(user)
        isLoggedIn.value = true
        isAdmin.value = currentUser.value.role === 'ADMIN'
        // 设置axios默认头部的Authorization
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
      } else {
        currentUser.value = null
        isLoggedIn.value = false
        isAdmin.value = false
      }
    }

    // 监听localStorage的变化
    const handleStorageChange = (e) => {
      if (e.key === 'currentUser' || e.key === 'token') {
        checkLoginStatus()
      }
    }

    onMounted(() => {
      checkLoginStatus()
      
      // 监听storage事件
      window.addEventListener('storage', handleStorageChange)

      // Redirect to login if not authenticated and trying to access protected routes
      router.beforeEach((to, from, next) => {
        const publicPages = ['/login', '/register']
        const authRequired = !publicPages.includes(to.path)
        const loggedIn = localStorage.getItem('currentUser') && localStorage.getItem('token')

        if (authRequired && !loggedIn) {
          return next('/login')
        }

        next()
      })
    })

    // 在组件卸载时移除事件监听器
    // 注意：在Vue 3的Composition API中，需要使用onUnmounted生命周期钩子
    // 但由于这里是在setup函数中，我们需要在返回之前定义它
    // 我们将在返回的对象中添加onUnmounted钩子

    return {
      activeIndex,
      currentUser,
      isLoggedIn,
      isAdmin,
      handleSelect,
      handleLogout,
      goToLogin,
      goToRegister
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