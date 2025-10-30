<template>
  <div class="hr-dashboard">
    <div class="header-section">
      <h2>HR驾驶舱</h2>
      <p>企业人才发展数据总览</p>
    </div>
    
    <!-- 统计数据 -->
    <div class="stats">
      <el-row :gutter="20">
        <el-col :span="6" :xs="12">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">{{ statistics.totalEmployees }}</div>
              <div class="stat-label">员工总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="12">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">{{ statistics.totalMilestones }}</div>
              <div class="stat-label">总历程数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="12">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">{{ statistics.totalCertificates }}</div>
              <div class="stat-label">总证书数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" :xs="12">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-number">{{ statistics.totalSkills }}</div>
              <div class="stat-label">总技能数</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12" :xs="24">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">部门员工分布</div>
            </template>
            <div ref="departmentChart" class="chart-container"></div>
          </el-card>
        </el-col>
        <el-col :span="12" :xs="24">
          <el-card class="chart-card">
            <template #header>
              <div class="chart-header">员工技能雷达图</div>
            </template>
            <div ref="skillRadarChart" class="chart-container"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 最近活动 -->
    <div class="recent-activity">
      <el-card>
        <template #header>
          <div class="activity-header">
            <span>企业最近活动</span>
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
                <span class="employee-name">{{ activity.employeeName }}</span>
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
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import apiClient from '@/api/user'

export default {
  name: 'HRDashboard',
  setup() {
    const statistics = ref({
      totalEmployees: 0,
      totalMilestones: 0,
      totalCertificates: 0,
      totalSkills: 0
    })
    
    const recentActivities = ref([])
    const departmentChart = ref(null)
    const skillRadarChart = ref(null)
    let departmentChartInstance = null
    let skillRadarChartInstance = null

    // 设置API基础URL
    const BASE_URL = '/api/hr-dashboard'

    const loadStatistics = async () => {
      try {
        const response = await apiClient.get(`${BASE_URL}/statistics`)
        statistics.value = response.data
        // 加载图表数据
        await nextTick()
        loadCharts()
      } catch (error) {
        ElMessage.error('加载统计数据失败: ' + (error.response?.data?.message || error.message))
      }
    }
    
    const loadDepartmentDistribution = async () => {
      try {
        const response = await apiClient.get(`${BASE_URL}/department-distribution`)
        return response.data
      } catch (error) {
        ElMessage.error('加载部门分布数据失败: ' + (error.response?.data?.message || error.message))
        return []
      }
    }
    
    const loadSkillStatistics = async () => {
      try {
        const response = await apiClient.get(`${BASE_URL}/skill-statistics`)
        return response.data
      } catch (error) {
        ElMessage.error('加载技能统计数据失败: ' + (error.response?.data?.message || error.message))
        return {}
      }
    }
    
    const loadRecentActivities = async () => {
      try {
        const response = await apiClient.get(`${BASE_URL}/recent-activities`)
        recentActivities.value = response.data
      } catch (error) {
        ElMessage.error('加载最近活动失败: ' + (error.response?.data?.message || error.message))
      }
    }
    
    const loadCharts = async () => {
      // 部门员工分布图
      if (departmentChart.value) {
        const departmentData = await loadDepartmentDistribution()
        departmentChartInstance = echarts.init(departmentChart.value)
        const departmentOption = {
          title: {
            text: '部门员工分布',
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
              name: '部门分布',
              type: 'pie',
              radius: '50%',
              data: departmentData.map(item => ({
                value: item.count,
                name: item.department
              })),
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
        departmentChartInstance.setOption(departmentOption)
      }
      
      // 员工技能雷达图
      if (skillRadarChart.value) {
        const skillData = await loadSkillStatistics()
        skillRadarChartInstance = echarts.init(skillRadarChart.value)
        const skillRadarOption = {
          title: {
            text: '员工技能分布',
            left: 'center'
          },
          tooltip: {},
          radar: {
            indicator: [
              { name: '技术技能', max: 50 },
              { name: '软技能', max: 50 },
              { name: '专业技能', max: 50 },
              { name: '语言技能', max: 50 },
              { name: '管理技能', max: 50 }
            ],
            radius: '60%',
            center: ['50%', '55%']
          },
          series: [{
            type: 'radar',
            data: [{
              value: [
                skillData.skillDistribution['技术技能'] || 0,
                skillData.skillDistribution['软技能'] || 0,
                skillData.skillDistribution['专业技能'] || 0,
                skillData.skillDistribution['语言技能'] || 0,
                skillData.skillDistribution['管理技能'] || 0
              ],
              name: '平均技能水平'
            }]
          }]
        }
        skillRadarChartInstance.setOption(skillRadarOption)
      }
    }
    
    const getActivityType = (type) => {
      const typeMap = {
        '历程': 'primary',
        '证书': 'success',
        '荣誉': 'warning',
        '晋升': 'danger'
      }
      return typeMap[type] || 'info'
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return ''
      // 处理 "yyyy-MM-dd HH:mm:ss" 格式
      if (dateString.includes(' ')) {
        const [datePart, timePart] = dateString.split(' ')
        const [year, month, day] = datePart.split('-')
        const [hour, minute, second] = timePart.split(':')
        const date = new Date(year, month - 1, day, hour, minute, second)
        
        // 格式化为 "yyyy年MM月dd日 HH:mm"
        return `${year}年${month}月${day}日 ${hour}:${minute}`
      }
      return dateString
    }

    onMounted(() => {
      loadStatistics()
      loadRecentActivities()
      
      // 监听窗口大小变化，重新调整图表大小
      window.addEventListener('resize', () => {
        if (departmentChartInstance) {
          departmentChartInstance.resize()
        }
        if (skillRadarChartInstance) {
          skillRadarChartInstance.resize()
        }
      })
    })

    return {
      statistics,
      recentActivities,
      departmentChart,
      skillRadarChart,
      getActivityType,
      formatDate
    }
  }
}
</script>

<style scoped>
.hr-dashboard {
  padding: 20px;
}

.header-section {
  text-align: center;
  margin-bottom: 30px;
}

.header-section h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.header-section p {
  font-size: 16px;
  color: #666;
}

.stats {
  margin-bottom: 30px;
}

.stat-card {
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #ff6b35;
}

.stat-label {
  font-size: 14px;
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