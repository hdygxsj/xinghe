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
          :xs="24"
          :sm="12"
          :md="8"
          :lg="8"
          v-for="certificate in filteredCertificates" 
          :key="certificate.id"
        >
          <el-card class="certificate-card">
            <div class="certificate-header">
              <h3>{{ certificate.title }}</h3>
              <div class="header-tags">
                <el-tag :type="getTagType(certificate.certificateType)">
                  {{ certificate.certificateType }}
                </el-tag>
                <el-tag 
                  v-if="certificate.grade && certificate.certificateType === '年度考核证明'" 
                  :type="getGradeTagType(certificate.grade)"
                  class="grade-tag"
                >
                  等级: {{ certificate.grade }} - {{ getGradeLabel(certificate.grade) }}
                </el-tag>
              </div>
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
      <el-form :model="annualForm" label-width="100px">
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
        <el-form-item label="考核等级">
          <el-select v-model="annualForm.grade" placeholder="请选择考核等级">
            <el-option label="A - 优秀" value="A"></el-option>
            <el-option label="B - 良好" value="B"></el-option>
            <el-option label="C - 合格" value="C"></el-option>
            <el-option label="D - 待改进" value="D"></el-option>
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
      title="证书预览"
      v-model="viewDialogVisible"
      width="90%"
      top="5vh"
      :close-on-click-modal="false"
      class="certificate-dialog"
    >
      <div v-if="selectedCertificate" class="certificate-preview" v-loading="previewLoading">
        <iframe 
          :src="certificateViewUrl" 
          width="100%" 
          height="600px" 
          frameborder="0"
          v-if="certificateViewUrl"
          @load="handleIframeLoad"
          @error="handleIframeError"
        ></iframe>
        <div v-if="previewError" class="preview-error">
          <el-alert
            title="预览失败"
            type="error"
            :closable="false"
            description="无法加载证书预览，请尝试下载PDF文件查看。"
          >
          </el-alert>
        </div>
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
import { getCertificatesByEmployeeId, downloadCertificatePdf } from '@/api/certificate'
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
    const previewLoading = ref(false)
    const previewError = ref(false)
    const currentUser = ref(null)
    const annualForm = ref({
      employeeId: null,
      year: '',
      grade: 'A'
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
    
    const getGradeTagType = (grade) => {
      const gradeMap = {
        'A': 'danger',
        'B': 'warning',
        'C': 'success',
        'D': 'info'
      }
      return gradeMap[grade] || 'info'
    }
    
    const getGradeLabel = (grade) => {
      const labelMap = {
        'A': '优秀',
        'B': '良好',
        'C': '合格',
        'D': '待改进'
      }
      return labelMap[grade] || grade
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
        
        if (!annualForm.value.year) {
          ElMessage.error('请选择年份')
          return
        }
        
        if (!annualForm.value.grade) {
          ElMessage.error('请选择考核等级')
          return
        }
        
        await generateAnnualAssessmentCertificate(
          currentUser.value.id,
          annualForm.value.year,
          annualForm.value.grade
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
        const response = await downloadCertificatePdf(certificate.id)
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
    
    const viewCertificate = async (certificate) => {
      selectedCertificate.value = certificate
      previewError.value = false
      // 使用后端API的完整地址，加时间戳强制重新加载
      const timestamp = new Date().getTime()
      certificateViewUrl.value = `http://localhost:8080/certificates/${certificate.id}/view?t=${timestamp}`
      viewDialogVisible.value = true
      // 设置超时，防止长时间转圈
      previewLoading.value = true
      setTimeout(() => {
        if (previewLoading.value) {
          previewLoading.value = false
          previewError.value = true
          ElMessage.error('证书加载超时，请检查网络连接')
        }
      }, 10000)
    }
    
    const handleIframeLoad = () => {
      previewLoading.value = false
      previewError.value = false
    }
    
    const handleIframeError = () => {
      previewLoading.value = false
      previewError.value = true
      ElMessage.error('证书预览加载失败，请尝试下载')
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
      previewLoading,
      previewError,
      annualForm,
      contactForm,
      availableYears,
      filteredCertificates,
      handleTabChange,
      getTagType,
      getGradeTagType,
      getGradeLabel,
      generateAnnualCertificate,
      generateContactCertificate,
      generateHonorCertificate,
      confirmGenerateAnnual,
      confirmGenerateContact,
      downloadCertificate: downloadCertificateHandler,
      viewCertificate,
      handleIframeLoad,
      handleIframeError,
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
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 15px;
}

.header-section h2 {
  margin: 0;
  flex-shrink: 0;
}

.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.certificates-grid {
  margin-top: 20px;
}

.certificate-card {
  margin-bottom: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: box-shadow 0.3s ease, transform 0.3s ease;
}

.certificate-card:hover {
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.certificate-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
  gap: 10px;
}

.certificate-header h3 {
  margin: 0;
  flex: 1;
  font-size: 16px;
  line-height: 1.4;
  word-break: break-word;
}

.certificate-header .el-tag {
  white-space: nowrap;
  flex-shrink: 0;
}

.header-tags {
  display: flex;
  gap: 8px;
  align-items: center;
  flex-shrink: 0;
  flex-wrap: wrap;
}

.grade-tag {
  font-weight: bold;
}

.certificate-content {
  flex: 1;
}

.certificate-content p {
  color: #666;
  line-height: 1.6;
  margin: 0 0 15px 0;
  word-break: break-word;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.certificate-info {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.info-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 8px;
  word-break: break-word;
}

.info-item label {
  font-weight: bold;
  font-size: 12px;
  color: #999;
  margin-bottom: 3px;
}

.info-item span {
  font-size: 14px;
  color: #333;
}

.certificate-footer {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
  margin-top: auto;
  padding-top: 15px;
  align-items: center;
}

.certificate-footer .el-button {
  padding: 6px 12px;
  font-size: 12px;
  height: 28px;
  line-height: 28px;
  white-space: nowrap;
}

.certificate-preview {
  text-align: center;
  min-height: 400px;
  max-height: calc(85vh - 200px);
  overflow: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.certificate-preview iframe {
  border: 1px solid #eee;
  border-radius: 4px;
}

.preview-error {
  width: 100%;
  padding: 50px;
}

/* 证书预览对话框样式 */
:deep(.certificate-dialog) {
  display: flex;
  flex-direction: column;
  max-height: 85vh;
  margin-top: 5vh !important;
}

:deep(.certificate-dialog .el-dialog__body) {
  flex: 1;
  overflow: auto;
  padding: 20px;
  max-height: calc(85vh - 120px);
}

:deep(.certificate-dialog .el-dialog__footer) {
  border-top: 1px solid #eee;
  padding: 15px 20px;
  background: #fff;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .certificates {
    padding: 15px;
  }
  
  .header-section {
    flex-direction: column;
    align-items: stretch;
  }
  
  .header-section h2 {
    width: 100%;
  }
  
  .actions {
    width: 100%;
  }
  
  .actions .el-button {
    flex: 1;
  }
  
  .certificate-card {
    margin-bottom: 15px;
  }
  
  .certificate-footer .el-button {
    min-width: auto;
  }
  
  :deep(.certificate-dialog) {
    width: 95% !important;
  }
}
</style>