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
        <el-col :span="isAdmin ? 6 : 8">
          <div class="feature-card" @click="goToMilestones">
            <div class="feature-icon">
              <el-icon><Document /></el-icon>
            </div>
            <h3>历程记录</h3>
            <p>记录入职培训、转正、晋升等重要时刻</p>
          </div>
        </el-col>
        <el-col :span="isAdmin ? 6 : 8">
          <div class="feature-card" @click="goToCertificates">
            <div class="feature-icon">
              <el-icon><Medal /></el-icon>
            </div>
            <h3>证书管理</h3>
            <p>管理各类证明文件和荣誉证书</p>
          </div>
        </el-col>
        <el-col :span="isAdmin ? 6 : 8">
          <div class="feature-card" @click="goToAI">
            <div class="feature-icon">
              <el-icon><Lightning /></el-icon>
            </div>
            <h3>智能助手</h3>
            <p>AI驱动的职业发展建议和学习路径</p>
          </div>
        </el-col>
        <el-col :span="6" v-if="isAdmin">
          <div class="feature-card admin-card" @click="goToRoles">
            <div class="feature-icon">
              <el-icon><User /></el-icon>
            </div>
            <h3>角色管理</h3>
            <p>管理系统角色和权限设置</p>
            <el-tag class="admin-badge" type="danger" size="small">管理员</el-tag>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="stats">
      <el-row :gutter="20">
        <el-col :span="6">
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
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.skillCount || 0 }}</div>
            <div class="stat-label">技能数量</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据分析图表 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>里程碑类型分布</span>
              </div>
            </template>
            <div ref="milestoneChart" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">
                <span>证书类型统计</span>
              </div>
            </template>
            <div ref="certificateChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activity">
      <el-card>
        <template #header>
          <div class="activity-header">
            <span>最近活动</span>
          </div>
        </template>
        <el-timeline>
          <el-timeline-item
            v-for="activity in recentActivities"
            :key="activity.id"
            :timestamp="formatDate(activity.createTime)"
            placement="top"
          >
            <el-card class="activity-card">
              <div class="activity-header-info">
                <h4>{{ activity.title }}</h4>
                <span v-if="activity.employeeName" class="employee-name">{{ activity.employeeName }}</span>
              </div>
              <p>{{ activity.description }}</p>
              <el-tag :type="getActivityType(activity.type)">{{ activity.type }}</el-tag>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { getStatisticsByEmployeeId } from '@/api/careerInfo'
import { getRecentActivities } from '@/api/activity'
import { ElMessage } from 'element-plus'
import { Document, Medal, Lightning, User, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

export default {
  name: 'Home',
  components: {
    Document,
    Medal,
    Lightning,
    User,
    TrendCharts
  },
  setup() {
    const router = useRouter()
    const currentUser = ref(null)
    const isAdmin = ref(false)
    const statistics = ref({
      milestoneCount: 0,
      certificateCount: 0,
      satisfactionRate: 95,
      skillCount: 0
    })
    const recentActivities = ref([])
    const milestoneChart = ref(null)
    const certificateChart = ref(null)
    let milestoneChartInstance = null
    let certificateChartInstance = null

    const goToMilestones = () => {
      router.push('/milestones')
    }

    const goToCertificates = () => {
      router.push('/certificates')
    }

    const goToAI = () => {
      router.push('/ai-assistant')
    }

    const goToCareerPlans = () => {
      router.push('/career-plans')
    }

    const goToRoles = () => {
      router.push('/roles')
    }

    const loadStatistics = async () => {
      try {
        if (currentUser.value && currentUser.value.id) {
          const response = await getStatisticsByEmployeeId(currentUser.value.id)
          statistics.value = { ...statistics.value, ...response.data }

          // 加载图表数据
          await nextTick()
          loadCharts()
        }
      } catch (error) {
        ElMessage.error('加载统计数据失败')
      }
    }

    const loadRecentActivities = async () => {
      try {
        const response = await getRecentActivities()
        recentActivities.value = response.data
      } catch (error) {
        ElMessage.error('加载最近活动失败')
      }
    }

    const loadCharts = () => {
      // 里程碑类型分布图
      if (milestoneChart.value) {
        milestoneChartInstance = echarts.init(milestoneChart.value)
        const milestoneOption = {
          title: {
            text: '里程碑类型分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              name: '里程碑类型',
              type: 'pie',
              radius: '50%',
              data: [
                { value: statistics.value.milestoneCount * 0.3, name: '入职培训' },
                { value: statistics.value.milestoneCount * 0.2, name: '转正' },
                { value: statistics.value.milestoneCount * 0.2, name: '职位晋升' },
                { value: statistics.value.milestoneCount * 0.15, name: '参赛荣誉' },
                { value: statistics.value.milestoneCount * 0.15, name: '重大项目' }
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
        milestoneChartInstance.setOption(milestoneOption)
      }

      // 证书类型统计图
      if (certificateChart.value) {
        certificateChartInstance = echarts.init(certificateChart.value)
        const certificateOption = {
          title: {
            text: '证书类型统计',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: ['年度考核', '在职证明', '荣誉证书']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: [
                statistics.value.certificateCount * 0.4,
                statistics.value.certificateCount * 0.3,
                statistics.value.certificateCount * 0.3
              ],
              type: 'bar',
              itemStyle: {
                color: '#ff6b35'
              }
            }
          ]
        }
        certificateChartInstance.setOption(certificateOption)
      }
    }

    const getActivityType = (type) => {
      const typeMap = {
        '里程碑': 'primary',
        '证书': 'success',
        '技能': 'warning',
        '职业规划': 'danger'
      }
      return typeMap[type] || 'info'
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    onMounted(() => {
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
        isAdmin.value = currentUser.value.role === 'ADMIN'
      }
      
      loadStatistics()
      loadRecentActivities()

      // 监听窗口大小变化，重新调整图表大小
      window.addEventListener('resize', () => {
        if (milestoneChartInstance) {
          milestoneChartInstance.resize()
        }
        if (certificateChartInstance) {
          certificateChartInstance.resize()
        }
      })
    })

    return {
      currentUser,
      isAdmin,
      statistics,
      recentActivities,
      milestoneChart,
      certificateChart,
      goToMilestones,
      goToCertificates,
      goToRoles,
      goToAI,
      goToCareerPlans,
      getActivityType,
      formatDate
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
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
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

.feature-card.admin-card {
  border: 2px solid #ff6b35;
  position: relative;
}

.admin-badge {
  position: absolute;
  top: 10px;
  right: 10px;
}

.stats {
  background: white;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 30px;
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

.charts-section {
  margin-bottom: 30px;
}

.chart-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.chart-header {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.recent-activity {
  margin-bottom: 30px;
}

.activity-header {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.activity-card {
  border: none;
  box-shadow: none;
}

.activity-header-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.activity-header-info h4 {
  margin: 0;
  color: #333;
}

.employee-name {
  font-size: 14px;
  color: #999;
  font-weight: normal;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero {
    padding: 40px 15px;
  }

  .hero h1 {
    font-size: 28px;
  }

  .hero p {
    font-size: 16px;
  }

  .features :deep(.el-col) {
    margin-bottom: 20px;
  }

  .stats :deep(.el-col) {
    margin-bottom: 20px;
  }

  .charts-section :deep(.el-col) {
    margin-bottom: 20px;
  }

  .chart-container {
    height: 250px;
  }
}
</style>