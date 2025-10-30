<template>
  <div class="certificates">
    <div class="header-section">
      <h2>我的证书与证明</h2>
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
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="contactDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmGenerateContact">生成</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 证书查看对话框 -->
    <el-dialog
      title="证书详情"
      v-model="viewDialogVisible"
      width="80%"
      top="50px"
    >
      <div v-if="selectedCertificate" class="certificate-preview">
        <iframe 
          :src="certificateViewUrl" 
          width="100%" 
          height="600px" 
          frameborder="0"
          v-if="certificateViewUrl"
        ></iframe>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="downloadCertificate(selectedCertificate)">下载PDF</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { getCertificatesByEmployeeId, downloadCertificate } from '@/api/certificate'
import { generateAnnualAssessmentCertificate, generateEmploymentContactCertificate, generateHonorCertificate } from '@/api/careerInfo'
import { ElMessage } from 'element-plus'

export default {
  name: 'Certificates',
  setup() {
    const activeTab = ref('all')
    const certificates = ref([])
    const annualDialogVisible = ref(false)
    const contactDialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const selectedCertificate = ref(null)
    const certificateViewUrl = ref('')
    const currentUser = ref(null)
    const annualForm = ref({
      employeeId: null,
      year: ''
    })
    const contactForm = ref({
      employeeId: null
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
        if (!currentUser.value || !currentUser.value.id) {
          ElMessage.error('请先登录')
          return
        }
        
        await generateAnnualAssessmentCertificate(
          currentUser.value.id,
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
        if (!currentUser.value || !currentUser.value.id) {
          ElMessage.error('请先登录')
          return
        }
        
        await generateEmploymentContactCertificate(currentUser.value.id)
        ElMessage.success('在职联系人证明生成成功')
        contactDialogVisible.value = false
        loadCertificates()
      } catch (error) {
        ElMessage.error('生成失败')
      }
    }
    
    const downloadCertificateHandler = async (certificate) => {
      try {
        const response = await downloadCertificate(certificate.id)
        // 创建下载链接
        const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `${certificate.title}.pdf`)
        document.body.appendChild(link)
        link.click()
        link.remove()
        window.URL.revokeObjectURL(url)
        ElMessage.success(`证书下载成功: ${certificate.title}`)
      } catch (error) {
        ElMessage.error('下载失败')
      }
    }
    
    const viewCertificate = (certificate) => {
      selectedCertificate.value = certificate
      certificateViewUrl.value = `/certificates/${certificate.id}/view`
      viewDialogVisible.value = true
    }
    
    const loadCertificates = async () => {
      try {
        if (currentUser.value && currentUser.value.id) {
          const response = await getCertificatesByEmployeeId(currentUser.value.id)
          certificates.value = response.data
        }
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
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
        // 设置表单中的员工ID
        annualForm.value.employeeId = currentUser.value.id
        contactForm.value.employeeId = currentUser.value.id
      }
      
      generateYears()
      loadCertificates()
    })
    
    return {
      activeTab,
      certificates,
      annualDialogVisible,
      contactDialogVisible,
      viewDialogVisible,
      selectedCertificate,
      certificateViewUrl,
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
      downloadCertificate: downloadCertificateHandler,
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

.certificate-preview {
  text-align: center;
}
</style>