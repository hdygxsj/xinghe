<template>
  <div class="ai-assistant">
    <div class="header-section">
      <h2>智能职业助手</h2>
      <p>基于AI技术为您提供个性化的职业发展建议</p>
    </div>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="assistant-card" @click="selectService('careerAdvice')">
          <div class="card-icon">
            <el-icon><lightning /></el-icon>
          </div>
          <h3>职业发展建议</h3>
          <p>根据您的职业历程提供个性化发展建议</p>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="assistant-card" @click="selectService('learningPath')">
          <div class="card-icon">
            <el-icon><reading /></el-icon>
          </div>
          <h3>学习路径推荐</h3>
          <p>为您推荐适合的技能提升路径和学习资源</p>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="assistant-card" @click="selectService('resumeOptimize')">
          <div class="card-icon">
            <el-icon><document /></el-icon>
          </div>
          <h3>简历优化建议</h3>
          <p>提供简历优化建议，帮助您更好地展示成就</p>
        </div>
      </el-col>
    </el-row>
    
    <div class="query-section" v-if="selectedService">
      <el-card>
        <template #header>
          <span>{{ getServiceTitle() }}</span>
        </template>
        <el-form :model="queryForm" label-width="80px">
          <el-form-item label="员工ID">
            <el-input v-model="queryForm.employeeId" placeholder="请输入员工ID"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="generateAdvice" :loading="loading">
              {{ loading ? '生成中...' : '生成建议' }}
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    
    <div class="result-section" v-if="result">
      <el-card>
        <template #header>
          <span>分析结果</span>
        </template>
        <div class="result-content">
          <pre>{{ result }}</pre>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import { getCareerAdvice, getLearningPath, optimizeResume } from '@/api/ai'
import { ElMessage } from 'element-plus'

export default {
  name: 'AIAssistant',
  setup() {
    const selectedService = ref('')
    const queryForm = ref({
      employeeId: ''
    })
    const loading = ref(false)
    const result = ref('')
    
    const selectService = (service) => {
      selectedService.value = service
      result.value = ''
    }
    
    const getServiceTitle = () => {
      const titles = {
        careerAdvice: '职业发展建议',
        learningPath: '学习路径推荐',
        resumeOptimize: '简历优化建议'
      }
      return titles[selectedService.value] || ''
    }
    
    const generateAdvice = async () => {
      if (!queryForm.value.employeeId) {
        ElMessage.warning('请输入员工ID')
        return
      }
      
      loading.value = true
      result.value = ''
      
      try {
        let response
        switch (selectedService.value) {
          case 'careerAdvice':
            response = await getCareerAdvice(queryForm.value.employeeId)
            break
          case 'learningPath':
            response = await getLearningPath(queryForm.value.employeeId)
            break
          case 'resumeOptimize':
            response = await optimizeResume(queryForm.value.employeeId)
            break
          default:
            ElMessage.error('未知服务类型')
            return
        }
        
        result.value = response.data
        ElMessage.success('生成成功')
      } catch (error) {
        ElMessage.error('生成失败: ' + (error.message || '未知错误'))
      } finally {
        loading.value = false
      }
    }
    
    return {
      selectedService,
      queryForm,
      loading,
      result,
      selectService,
      getServiceTitle,
      generateAdvice
    }
  }
}
</script>

<style scoped>
.ai-assistant {
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

.assistant-card {
  background: white;
  border-radius: 12px;
  padding: 30px 20px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;
  margin-bottom: 20px;
}

.assistant-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.card-icon {
  font-size: 48px;
  color: #ff6b35;
  margin-bottom: 20px;
}

.assistant-card h3 {
  margin-bottom: 15px;
  color: #333;
}

.query-section {
  margin-top: 30px;
}

.result-section {
  margin-top: 30px;
}

.result-content {
  white-space: pre-wrap;
  line-height: 1.6;
}
</style>