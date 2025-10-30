<template>
  <div id="app">
    <el-container>
      <el-header class="header">
        <div class="logo">GF职业空间</div>
        <div class="nav">
          <el-menu mode="horizontal" :default-active="activeIndex" @select="handleSelect">
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2">我的历程</el-menu-item>
            <el-menu-item index="3">证书证明</el-menu-item>
            <el-menu-item index="4">智能助手</el-menu-item>
            <el-menu-item index="5">导师管理</el-menu-item>
            <el-menu-item index="6">职业规划</el-menu-item>
            <el-menu-item index="7">技能管理</el-menu-item>
            <el-menu-item index="8" v-if="isAdmin">角色管理</el-menu-item>
            <el-menu-item index="9" v-if="isAdmin">HR驾驶舱</el-menu-item>

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

export default {
  name: 'App',
  setup() {
    const router = useRouter()
    const activeIndex = ref('1')
    const isLoggedIn = ref(false)
    const isAdmin = ref(false)
    const currentUser = ref(null)

    // 检查登录状态
    const checkLoginStatus = () => {
      const user = localStorage.getItem('currentUser')
      const token = localStorage.getItem('token')
      
      if (user && token) {
        currentUser.value = JSON.parse(user)
        isLoggedIn.value = true
        // 检查是否为管理员
        isAdmin.value = currentUser.value.role === 'ADMIN'
      } else {
        isLoggedIn.value = false
        isAdmin.value = false
        currentUser.value = null
      }
    }

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
        case '9':
          router.push('/hr-dashboard')
          break
      }
    }

    const handleLogout = () => {
      // 清除本地存储
      localStorage.removeItem('currentUser')
      localStorage.removeItem('token')
      
      // 更新状态
      isLoggedIn.value = false
      isAdmin.value = false
      currentUser.value = null
      
      // 跳转到登录页
      router.push('/login')
    }

    const goToLogin = () => {
      router.push('/login')
    }

    const goToRegister = () => {
      router.push('/register')
    }

    // 监听storage变化（用于处理登录状态变化）
    const handleStorageChange = (e) => {
      if (e.key === 'currentUser') {
        checkLoginStatus()
      }
    }

    onMounted(() => {
      checkLoginStatus()
      window.addEventListener('storage', handleStorageChange)
    })

    // 清理事件监听器
    // 注意：在Vue 3 Composition API中，这通常在组件卸载时自动处理

    return {
      activeIndex,
      isLoggedIn,
      isAdmin,
      currentUser,
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
  color: #1d54c1;
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