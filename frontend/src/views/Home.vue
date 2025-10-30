<template>
  <div class="home">
    <div class="hero">
      <h1 v-if="currentUser">欢迎回来, {{ currentUser.name }}!</h1>
      <h1 v-else>欢迎来到GF职业空间</h1>
      <p v-if="currentUser">继续记录您的职业成长足迹，规划未来发展路径</p>
      <p v-else>记录您的职业成长足迹，规划未来发展路径</p>
      <el-button type="primary" size="large" @click="goToMilestones">开始记录</el-button>
    </div>
    
    <div class="features">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Document /></el-icon>
            </div>
            <h3>历程记录</h3>
            <p>记录入职培训、转正、晋升等重要时刻</p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Medal /></el-icon>
            </div>
            <h3>证书管理</h3>
            <p>管理各类证明文件和荣誉证书</p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon><Lightning /></el-icon>
            </div>
            <h3>智能助手</h3>
            <p>AI驱动的职业发展建议和学习路径</p>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="stats">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.milestoneCount }}</div>
            <div class="stat-label">我的里程碑</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.certificateCount }}</div>
            <div class="stat-label">我的证书</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.satisfactionRate }}%</div>
            <div class="stat-label">满意度</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getStatisticsByEmployeeId } from '@/api/careerInfo'
import { ElMessage } from 'element-plus'
import { Document, Medal, Lightning } from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    Document,
    Medal,
    Lightning
  },
  setup() {
    const router = useRouter()
    const currentUser = ref(null)
    const statistics = ref({
      milestoneCount: 0,
      certificateCount: 0,
      satisfactionRate: 95
    })

    const goToMilestones = () => {
      router.push('/milestones')
    }

    const loadStatistics = async () => {
      try {
        if (currentUser.value && currentUser.value.id) {
          const response = await getStatisticsByEmployeeId(currentUser.value.id)
          statistics.value = response.data
        }
      } catch (error) {
        ElMessage.error('加载统计数据失败')
      }
    }

    onMounted(() => {
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
      }
      
      loadStatistics()
    })

    return {
      currentUser,
      statistics,
      goToMilestones
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.hero {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
  border-radius: 12px;
  color: white;
  margin-bottom: 30px;
}

.hero h1 {
  font-size: 36px;
  margin-bottom: 15px;
}

.hero p {
  font-size: 18px;
  margin-bottom: 30px;
}

.features {
  margin-bottom: 40px;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 30px 20px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 48px;
  color: #ff6b35;
  margin-bottom: 20px;
}

.feature-card h3 {
  margin-bottom: 15px;
  color: #333;
}

.stats {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  color: #ff6b35;
}

.stat-label {
  font-size: 16px;
  color: #666;
}
</style>