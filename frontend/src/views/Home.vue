<template>
  <div class="home">
    <!-- 英雄区域 -->
    <div class="hero">
      <ParticleBackground />
      <div class="hero-content">
        <h1 class="animated-title">欢迎来到职业空间</h1>
        <p class="animated-subtitle">记录和展示您的职业发展历程，规划未来方向</p>
      </div>
    </div>
    
    <!-- 功能卡片 -->
    <div class="features">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="feature-card" @click="goToMilestones">
            <div class="feature-icon">
              <el-icon><Document /></el-icon>
            </div>
            <h3>历程记录</h3>
            <p>记录入职培训、转正、晋升等重要时刻</p>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="feature-card" @click="goToCertificates">
            <div class="feature-icon">
              <el-icon><Medal /></el-icon>
            </div>
            <h3>证书管理</h3>
            <p>管理各类证明文件和荣誉证书</p>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="feature-card" @click="goToAI">
            <div class="feature-icon">
              <el-icon><Lightning /></el-icon>
            </div>
            <h3>智能助手</h3>
            <p>AI驱动的职业发展建议和学习路径</p>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="feature-card" @click="goToCareerPlans">
            <div class="feature-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <h3>职业规划</h3>
            <p>制定和跟踪您的职业发展计划</p>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <!-- 统计数据 -->
    <div class="stats">
      <el-row :gutter="20">
        <el-col :span="6" :xs="12">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.milestoneCount }}</div>
            <div class="stat-label">里程碑</div>
          </div>
        </el-col>
        <el-col :span="6" :xs="12">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.certificateCount }}</div>
            <div class="stat-label">证书</div>
          </div>
        </el-col>
        <el-col :span="6" :xs="12">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.skillCount }}</div>
            <div class="stat-label">技能</div>
          </div>
        </el-col>
        <el-col :span="6" :xs="12">
          <div class="stat-item">
            <div class="stat-number">{{ statistics.satisfactionRate }}%</div>
            <div class="stat-label">满意度</div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="8" :xs="24">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">里程碑类型分布</div>
            </template>
            <div ref="milestoneChart" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="8" :xs="24">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">证书类型统计</div>
            </template>
            <div ref="certificateChart" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="8" :xs="24">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">技能雷达图</div>
            </template>
            <div ref="skillChart" class="chart-container"></div>
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
        <!-- 分页控件 -->
        <div class="pagination-container" v-if="totalActivities > pageSize">
          <el-pagination
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="totalActivities"
            layout="prev, pager, next"
            background
          />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { getStatisticsByEmployeeId } from '@/api/careerInfo'
import { getRecentActivities } from '@/api/activity'
import { getEmployeeSkillStatistics } from '@/api/employeeSkill'
import { ElMessage } from 'element-plus'
import { Document, Medal, Lightning, TrendCharts } from '@element-plus/icons-vue'
import ParticleBackground from '@/components/ParticleBackground.vue'
import * as echarts from 'echarts'

export default {
  name: 'Home',
  components: {
    Document,
    Medal,
    Lightning,
    TrendCharts,
    ParticleBackground
  },
  setup() {
    const router = useRouter()
    const currentUser = ref(null)
    const statistics = ref({
      milestoneCount: 0,
      certificateCount: 0,
      satisfactionRate: 95,
      skillCount: 0
    })
    const recentActivities = ref([])
    const milestoneChart = ref(null)
    const certificateChart = ref(null)
    const skillChart = ref(null)
    let milestoneChartInstance = null
    let certificateChartInstance = null
    let skillChartInstance = null
    
    // 分页相关
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalActivities = ref(0)

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
    
    const loadRecentActivities = async (page = 1) => {
      try {
        const response = await getRecentActivities(page - 1, pageSize.value)
        recentActivities.value = response.data.activities || response.data
        totalActivities.value = response.data.total || (response.data.activities ? response.data.activities.length : response.data.length) * 10 // 估算总数量
      } catch (error) {
        ElMessage.error('加载最近活动失败')
      }
    }
    
    const handlePageChange = (page) => {
      currentPage.value = page
      loadRecentActivities(page)
    }
    
    const loadCharts = async () => {
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
                { value: Math.round(statistics.value.milestoneCount * 0.3), name: '入职培训' },
                { value: Math.round(statistics.value.milestoneCount * 0.2), name: '转正' },
                { value: Math.round(statistics.value.milestoneCount * 0.2), name: '职位晋升' },
                { value: Math.round(statistics.value.milestoneCount * 0.15), name: '参赛荣誉' },
                { value: Math.round(statistics.value.milestoneCount * 0.15), name: '重大项目' }
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
                Math.round(statistics.value.certificateCount * 0.4),
                Math.round(statistics.value.certificateCount * 0.3),
                Math.round(statistics.value.certificateCount * 0.3)
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
      
      // 技能雷达图
      if (skillChart.value && currentUser.value && currentUser.value.id) {
        try {
          // 获取员工技能统计
          const skillStatsResponse = await getEmployeeSkillStatistics(currentUser.value.id)
          const skillStats = skillStatsResponse.data
          
          skillChartInstance = echarts.init(skillChart.value)
          const skillOption = {
            title: {
              text: '技能分布',
              left: 'center'
            },
            tooltip: {},
            radar: {
              indicator: [
                { name: '技术技能', max: 3 },
                { name: '软技能', max: 3 },
                { name: '专业技能', max: 3 },
                { name: '语言技能', max: 3 },
                { name: '管理技能', max: 3 },
                { name: '协同技能', max: 3 },
              ],
              radius: '55%',
              center: ['50%', '55%'],
              splitNumber: 6,
              name: {
                textStyle: {
                  color: '#666',
                  fontSize: 12
                }
              },
              splitLine: {
                lineStyle: {
                  color: '#e0e0e0',
                  width: 1
                }
              },
              splitArea: {
                show: true,
                areaStyle: {
                  color: ['rgba(250, 250, 250, 0.8)', 'rgba(230, 230, 230, 0.8)']
                }
              },
              axisLine: {
                lineStyle: {
                  color: '#e0e0e0',
                  width: 1
                }
              }
            },
            series: [{
              type: 'radar',
              data: [{
                value: [
                  skillStats.skillTypeCount['技术技能'] || 0,
                  skillStats.skillTypeCount['软技能'] || 0,
                  skillStats.skillTypeCount['专业技能'] || 0,
                  skillStats.skillTypeCount['语言技能'] || 0,
                  skillStats.skillTypeCount['管理技能'] || 0,
                  skillStats.skillTypeCount['协同技能'] || 0
                ],
                name: '技能评分',
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                  width: 2,
                  color: '#ff6b35'
                },
                itemStyle: {
                  color: '#ff6b35',
                  borderColor: '#fff',
                  borderWidth: 2
                },
                areaStyle: {
                  color: 'rgba(255, 107, 53, 0.25)'
                }
              }]
            }]
          }
          skillChartInstance.setOption(skillOption)
        } catch (error) {
          console.error('加载技能统计失败:', error)
          // 使用默认数据
          skillChartInstance = echarts.init(skillChart.value)
          const skillOption = {
            title: {
              text: '技能分布',
              left: 'center'
            },
            tooltip: {},
            radar: {
              indicator: [
                { name: '技术技能', max: 3 },
                { name: '软技能', max: 3 },
                { name: '专业技能', max: 3 },
                { name: '语言技能', max: 3 },
                { name: '管理技能', max: 3 },
                { name: '协同技能', max: 3 }
              ],
              radius: '60%',
              center: ['50%', '55%'],
              splitNumber: 5,
              name: {
                textStyle: {
                  color: '#666',
                  fontSize: 12
                }
              },
              splitLine: {
                lineStyle: {
                  color: '#ddd'
                }
              },
              splitArea: {
                show: false
              },
              axisLine: {
                lineStyle: {
                  color: '#ddd'
                }
              }
            },
            series: [{
              type: 'radar',
              data: [{
                value: [
                  Math.min(statistics.value.skillCount * 0.3, 10),
                  Math.min(statistics.value.skillCount * 0.25, 10),
                  Math.min(statistics.value.skillCount * 0.2, 10),
                  Math.min(statistics.value.skillCount * 0.15, 10),
                  Math.min(statistics.value.skillCount * 0.1, 10)
                ],
                name: '技能评分',
                symbol: 'circle',
                symbolSize: 6,
                lineStyle: {
                  width: 2,
                  color: '#ff6b35'
                },
                itemStyle: {
                  color: '#ff6b35',
                  borderColor: '#fff',
                  borderWidth: 2
                },
                areaStyle: {
                  color: 'rgba(255, 107, 53, 0.25)'
                }
              }]
            }]
          }
          skillChartInstance.setOption(skillOption)
        }
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
      // 处理不同的日期格式
      let date
      if (typeof dateString === 'string') {
        // 处理 "yyyy-MM-dd HH:mm:ss" 格式
        if (dateString.includes(' ')) {
          const [datePart, timePart] = dateString.split(' ')
          const [year, month, day] = datePart.split('-')
          const [hour, minute, second] = timePart.split(':')
          date = new Date(year, month - 1, day, hour, minute, second)
        } else {
          // 处理 ISO 格式或其他标准格式
          date = new Date(dateString)
        }
      } else {
        // 处理已经是 Date 对象的情况
        date = new Date(dateString)
      }
      
      // 格式化为 "yyyy年MM月dd日 HH:mm"
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      
      return `${year}年${month}月${day}日 ${hours}:${minutes}`
    }

    const getAvatarText = () => {
      if (!currentUser.value || !currentUser.value.name) {
        return 'U'
      }
      // 获取姓名的最后两个字符作为头像文字
      return currentUser.value.name.length >= 2 
        ? currentUser.value.name.slice(-2) 
        : currentUser.value.name
    }

    onMounted(() => {
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
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
        if (skillChartInstance) {
          skillChartInstance.resize()
        }
      })
    })

    return {
      currentUser,
      statistics,
      recentActivities,
      milestoneChart,
      certificateChart,
      skillChart,
      goToMilestones,
      goToCertificates,
      goToAI,
      goToCareerPlans,
      getActivityType,
      formatDate,
      getAvatarText,
      // 分页相关
      currentPage,
      pageSize,
      totalActivities,
      handlePageChange
    }
  }
}
</script>

<style scoped>
.home {
  padding: 20px;
  position: relative;
  background: linear-gradient(to bottom, #f5f7ff 0%, #ffffff 100%);
  min-height: 100vh;
}

.user-header {
  position: absolute;
  top: 20px;
  right: 20px;
  z-index: 10;
}

.user-info-container {
  display: flex;
  align-items: center;
  gap: 12px;
  background: rgba(255, 255, 255, 0.95);
  padding: 10px 20px;
  border-radius: 50px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.15);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid rgba(102, 126, 234, 0.2);
  backdrop-filter: blur(10px);
}

.user-info-container:hover {
  box-shadow: 0 6px 25px rgba(102, 126, 234, 0.25);
  transform: translateY(-2px);
  border-color: rgba(102, 126, 234, 0.4);
}

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 18px;
  font-weight: bold;
  flex-shrink: 0;
}

.user-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.user-name {
  font-size: 15px;
  font-weight: 600;
  color: #2d3748;
  line-height: 1.2;
}

.user-meta {
  font-size: 12px;
  color: #718096;
  display: flex;
  gap: 8px;
  align-items: center;
}

.user-meta .department::before {
  content: '|';
  margin-right: 8px;
  color: #cbd5e0;
}

.hero {
  text-align: center;
  padding: 80px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  color: white;
  margin-bottom: 40px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(102, 126, 234, 0.3);
}

.hero::before {
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

.hero-content {
  position: relative;
  z-index: 1;
}

.animated-title {
  font-size: 42px;
  margin-bottom: 20px;
  font-weight: 700;
  animation: fadeInDown 1s ease-out;
  text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.15);
  letter-spacing: 1px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.animated-subtitle {
  font-size: 20px;
  margin-bottom: 0;
  animation: fadeInUp 1s ease-out 0.3s both;
  opacity: 0.95;
  letter-spacing: 0.5px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
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

.features {
  margin-bottom: 40px;
}

.feature-card {
  background: white;
  border-radius: 16px;
  padding: 30px 20px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  cursor: pointer;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(102, 126, 234, 0.1), transparent);
  transition: left 0.5s ease;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-card:hover {
  transform: translateY(-10px) scale(1.02);
  box-shadow: 0 12px 30px rgba(102, 126, 234, 0.2);
  border-color: #667eea;
}

.feature-icon {
  font-size: 56px;
  color: #667eea;
  margin-bottom: 20px;
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
}

.feature-card h3 {
  margin-bottom: 12px;
  color: #2d3748;
  font-size: 20px;
  font-weight: 600;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.feature-card p {
  color: #4a5568;
  font-size: 14px;
  margin: 0;
  line-height: 1.6;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.stats {
  background: linear-gradient(135deg, #ffffff 0%, #f8f9ff 100%);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  margin-bottom: 40px;
  margin-top: 80px;
  border: 1px solid rgba(102, 126, 234, 0.1);
}

.stat-item {
  text-align: center;
  padding: 20px;
  transition: transform 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-5px);
}

.stat-number {
  font-size: 42px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: countUp 1.5s ease-out;
}

@keyframes countUp {
  from {
    opacity: 0;
    transform: scale(0.5);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.stat-label {
  font-size: 16px;
  color: #4a5568;
  margin-top: 8px;
  font-weight: 500;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.charts-section {
  margin-bottom: 30px;
}

.chart-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  border: 1px solid rgba(102, 126, 234, 0.1);
  overflow: hidden;
}

.chart-card:hover {
  box-shadow: 0 8px 30px rgba(102, 126, 234, 0.15);
  transform: translateY(-5px);
}

.chart-header {
  font-size: 18px;
  font-weight: 600;
  color: #2d3748;
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.chart-header::before {
  content: '';
  width: 4px;
  height: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.chart-container {
  width: 100%;
  height: 320px; /* 增加高度以更好地显示雷达图 */
}

.recent-activity {
  margin-bottom: 30px;
}

.activity-header {
  font-size: 18px;
  font-weight: 600;
  color: #2d3748;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
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
  color: #2d3748;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.employee-name {
  font-size: 14px;
  color: #718096;
  font-weight: normal;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 20px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-header {
    position: static;
    margin-bottom: 15px;
  }
  
  .user-info-container {
    justify-content: center;
  }
  
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