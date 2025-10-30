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
import { ref, computed, onMounted } from 'vue'
import { getCertificates } from '@/api/certificate'
import { generateAnnualAssessmentCertificate, generateEmploymentContactCertificate, generateHonorCertificate } from '@/api/careerInfo'
import { ElMessage } from 'element-plus'

export default {
  name: 'Certificates',
  setup() {
    const activeTab = ref('all')
    const certificates = ref([])
    const annualDialogVisible = ref(false)
    const contactDialogVisible = ref(false)
    const annualForm = ref({
      employeeId: '',
      year: ''
    })
    const contactForm = ref({
      employeeId: ''
    })
    const availableYears = ref([])
    
    const filteredCertificates = computed(() => {
      if (activeTab.value === 'all') {
        return certificates.value
      }
      return certificates.value.filter(c => c.certificateType === activeTab.value)
    })
    
    const handleTabChange = (tab) => {
      activeTab.value = tab
    }
    
    const getTagType = (type) => {
      const typeMap = {
        '年度考核证明': 'primary',
        '在职联系人证明': 'success',
        '荣誉证明': 'warning'
      }
      return typeMap[type] || 'info'
    }
    
    const generateYears = () => {
      const currentYear = new Date().getFullYear()
      availableYears.value = []
      for (let i = currentYear - 5; i <= currentYear; i++) {
        availableYears.value.push(i)
      }
    }
    
    const generateAnnualCertificate = () => {
      annualDialogVisible.value = true
    }
    
    const generateContactCertificate = () => {
      contactDialogVisible.value = true
    }
    
    const generateHonorCertificate = () => {
      ElMessage.info('请选择一个荣誉里程碑来生成证明')
    }
    
    const confirmGenerateAnnual = async () => {
      try {
        await generateAnnualAssessmentCertificate(
          annualForm.value.employeeId,
          annualForm.value.year
        )
        ElMessage.success('年度考核证明生成成功')
        annualDialogVisible.value = false
        loadCertificates()
      } catch (error) {
        ElMessage.error('生成失败')
      }
    }
    
    const confirmGenerateContact = async () => {
      try {
        await generateEmploymentContactCertificate(contactForm.value.employeeId)
        ElMessage.success('在职联系人证明生成成功')
        contactDialogVisible.value = false
        loadCertificates()
      } catch (error) {
        ElMessage.error('生成失败')
      }
    }
    
    const downloadCertificate = (certificate) => {
      // 模拟下载证书
      if (certificate.certificateUrl) {
        // 如果有证书URL，则下载文件
        const link = document.createElement('a')
        link.href = certificate.certificateUrl
        link.download = `${certificate.title}.pdf`
        link.click()
        ElMessage.success(`开始下载证书: ${certificate.title}`)
      } else {
        // 如果没有证书URL，则提供模拟下载
        const content = `证书标题: ${certificate.title}
证书类型: ${certificate.certificateType}
颁发日期: ${formatDate(certificate.issueDate)}
颁发机构: ${certificate.issuer}
描述: ${certificate.description}`
        const blob = new Blob([content], { type: 'text/plain' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${certificate.title}.txt`
        link.click()
        window.URL.revokeObjectURL(url)
        ElMessage.success(`开始下载证书: ${certificate.title}`)
      }
    }
    
    const viewCertificate = (certificate) => {
      // 模拟查看证书
      if (certificate.certificateUrl) {
        // 如果有证书URL，则在新窗口中打开
        window.open(certificate.certificateUrl, '_blank')
      } else {
        // 如果没有证书URL，则显示证书信息
        ElMessage.info(`证书信息: ${certificate.title} - ${certificate.description}`)
      }
    }
    
    const loadCertificates = async () => {
      try {
        const response = await getCertificates()
        certificates.value = response.data
      } catch (error) {
        ElMessage.error('加载证书失败')
      }
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
    
    onMounted(() => {
      generateYears()
      loadCertificates()
    })
    
    return {
      activeTab,
      certificates,
      annualDialogVisible,
      contactDialogVisible,
      annualForm,
      contactForm,
      availableYears,
      filteredCertificates,
      handleTabChange,
      getTagType,
      generateAnnualCertificate,
      generateContactCertificate,
      generateHonorCertificate,
      confirmGenerateAnnual,
      confirmGenerateContact,
      downloadCertificate,
      viewCertificate,
      formatDate,
      loadCertificates
    }
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