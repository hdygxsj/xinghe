<template>
  <div class="home">
    <div class="hero">
      <h1>欢迎来到GF职业空间</h1>
      <p>记录您的职业成长足迹，规划未来发展路径</p>
      <el-button type="primary" size="large" @click="goToMilestones">开始记录</el-button>
    </div>
    
    <div class="features">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="el-icon-document"></i>
            </div>
            <h3>历程记录</h3>
            <p>记录入职培训、转正、晋升等重要时刻</p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="el-icon-medal"></i>
            </div>
            <h3>证书管理</h3>
            <p>管理各类证明文件和荣誉证书</p>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="feature-card">
            <div class="feature-icon">
              <i class="el-icon-lightning"></i>
            </div>
            <h3>智能助手</h3>
            <p>AI驱动的职业发展建议和学习路径</p>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <div class="stats">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">128</div>
            <div class="stat-label">注册用户</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">356</div>
            <div class="stat-label">里程碑事件</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">89</div>
            <div class="stat-label">证书证明</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-number">95%</div>
            <div class="stat-label">用户满意度</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    goToMilestones() {
      this.$router.push('/milestones')
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