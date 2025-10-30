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
            <i class="el-icon-lightning"></i>
          </div>
          <h3>职业发展建议</h3>
          <p>根据您的职业历程提供个性化发展建议</p>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="assistant-card" @click="selectService('learningPath')">
          <div class="card-icon">
            <i class="el-icon-reading"></i>
          </div>
          <h3>学习路径推荐</h3>
          <p>为您推荐适合的技能提升路径和学习资源</p>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="assistant-card" @click="selectService('resumeOptimize')">
          <div class="card-icon">
            <i class="el-icon-document"></i>
          </div>
          <h3>简历优化建议</h3>
          <p>提供简历优化建议，帮助您更好地展示成就</p>
        </div>
      </el-col>
    </el-row>
    
    <div class="query-section" v-if="selectedService">
      <el-card>
        <div slot="header">
          <span>{{ getServiceTitle() }}</span>
        </div>
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
        <div slot="header">
          <span>分析结果</span>
        </div>
        <div class="result-content">
          <pre>{{ result }}</pre>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AIAssistant',
  data() {
    return {
      selectedService: '',
      queryForm: {
        employeeId: ''
      },
      loading: false,
      result: ''
    }
  },
  methods: {
    selectService(service) {
      this.selectedService = service
      this.result = ''
    },
    
    getServiceTitle() {
      const titles = {
        careerAdvice: '职业发展建议',
        learningPath: '学习路径推荐',
        resumeOptimize: '简历优化建议'
      }
      return titles[this.selectedService] || ''
    },
    
    generateAdvice() {
      if (!this.queryForm.employeeId) {
        this.$message.warning('请输入员工ID')
        return
      }
      
      this.loading = true
      this.result = ''
      
      // 模拟AI服务调用
      setTimeout(() => {
        this.result = this.getMockResult()
        this.loading = false
        this.$message.success('生成成功')
      }, 1000)
    },
    
    getMockResult() {
      const mockResults = {
        careerAdvice: '根据您的职业历程，我们为您提供以下建议：\n1. 继续在当前领域深耕，提升专业技能\n2. 考虑横向发展，拓展跨部门协作能力\n3. 关注行业趋势，持续学习新技术',
        learningPath: '根据您的职业发展需求，我们为您推荐以下学习路径：\n1. 技术技能提升：参加相关技术培训课程\n2. 管理能力培养：学习项目管理知识\n3. 软技能发展：提升沟通与领导力',
        resumeOptimize: '根据您的职业信息，我们为您提供以下简历优化建议：\n1. 突出关键成就和量化结果\n2. 使用行业关键词和技能标签\n3. 结构化展示项目经验和职责'
      }
      return mockResults[this.selectedService] || '暂无建议'
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