<template>
  <div class="certificates">
    <div class="header-section">
      <h2>证书与证明</h2>
      <div class="actions">
        <el-button type="primary" @click="generateAnnualCertificate">年度考核证明</el-button>
        <el-button type="success" @click="generateContactCertificate">在职联系人证明</el-button>
        <el-button type="warning" @click="generateHonorCertificate">荣誉证明</el-button>
      </div>
    </div>
    
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="年度考核证明" name="年度考核证明"></el-tab-pane>
      <el-tab-pane label="在职联系人证明" name="在职联系人证明"></el-tab-pane>
      <el-tab-pane label="荣誉证明" name="荣誉证明"></el-tab-pane>
    </el-tabs>
    
    <div class="certificates-grid">
      <el-row :gutter="20">
        <el-col 
          :span="8" 
          v-for="certificate in filteredCertificates" 
          :key="certificate.id"
        >
          <el-card class="certificate-card">
            <div class="certificate-header">
              <h3>{{ certificate.title }}</h3>
              <el-tag :type="getTagType(certificate.certificateType)">
                {{ certificate.certificateType }}
              </el-tag>
            </div>
            <div class="certificate-content">
              <p>{{ certificate.description }}</p>
              <div class="certificate-info">
                <div class="info-item">
                  <label>颁发日期:</label>
                  <span>{{ formatDate(certificate.issueDate) }}</span>
                </div>
                <div class="info-item">
                  <label>颁发机构:</label>
                  <span>{{ certificate.issuer }}</span>
                </div>
              </div>
            </div>
            <div class="certificate-footer">
              <el-button size="small" type="primary" @click="downloadCertificate(certificate)">
                下载
              </el-button>
              <el-button size="small" @click="viewCertificate(certificate)">
                查看
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <!-- 生成证书对话框 -->
    <el-dialog
      title="生成年度考核证明"
      v-model="annualDialogVisible"
      width="400px"
    >
      <el-form :model="annualForm" label-width="80px">
        <el-form-item label="员工ID">
          <el-input v-model="annualForm.employeeId"></el-input>
        </el-form-item>
        <el-form-item label="年份">
          <el-select v-model="annualForm.year" placeholder="请选择年份">
            <el-option
              v-for="year in availableYears"
              :key="year"
              :label="year"
              :value="year"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="annualDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmGenerateAnnual">生成</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 在职联系人证明对话框 -->
    <el-dialog
      title="生成在职联系人证明"
      v-model="contactDialogVisible"
      width="400px"
    >
      <el-form :model="contactForm" label-width="80px">
        <el-form-item label="员工ID">
          <el-input v-model="contactForm.employeeId"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="contactDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmGenerateContact">生成</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'Certificates',
  data() {
    return {
      activeTab: 'all',
      certificates: [],
      annualDialogVisible: false,
      contactDialogVisible: false,
      annualForm: {
        employeeId: '',
        year: ''
      },
      contactForm: {
        employeeId: ''
      },
      availableYears: []
    }
  },
  computed: {
    filteredCertificates() {
      if (this.activeTab === 'all') {
        return this.certificates
      }
      return this.certificates.filter(c => c.certificateType === this.activeTab)
    }
  },
  methods: {
    handleTabChange(tab) {
      this.activeTab = tab
    },
    
    getTagType(type) {
      const typeMap = {
        '年度考核证明': 'primary',
        '在职联系人证明': 'success',
        '荣誉证明': 'warning'
      }
      return typeMap[type] || 'info'
    },
    
    generateYears() {
      const currentYear = new Date().getFullYear()
      this.availableYears = []
      for (let i = currentYear - 5; i <= currentYear; i++) {
        this.availableYears.push(i)
      }
    },
    
    generateAnnualCertificate() {
      this.annualDialogVisible = true
    },
    
    generateContactCertificate() {
      this.contactDialogVisible = true
    },
    
    generateHonorCertificate() {
      this.$message.info('请选择一个荣誉里程碑来生成证明')
    },
    
    confirmGenerateAnnual() {
      this.annualDialogVisible = false
      // 这里应该调用API生成证书
    },
    
    confirmGenerateContact() {
      this.contactDialogVisible = false
      // 这里应该调用API生成证书
    },
    
    downloadCertificate(certificate) {
      this.$message.success(`开始下载证书: ${certificate.title}`)
    },
    
    viewCertificate(certificate) {
      this.$message.success(`查看证书: ${certificate.title}`)
    },
    
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
  },
  mounted() {
    this.generateYears()
  }
}
</script>

<style scoped>
.certificates {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  gap: 10px;
}

.certificates-grid {
  margin-top: 20px;
}

.certificate-card {
  margin-bottom: 20px;
}

.certificate-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.certificate-header h3 {
  margin: 0;
}

.certificate-content p {
  color: #666;
  line-height: 1.6;
}

.certificate-info {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.info-item {
  display: flex;
  margin-bottom: 8px;
}

.info-item label {
  width: 80px;
  font-weight: bold;
}

.certificate-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}
</style>