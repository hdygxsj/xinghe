<template>
  <div class="milestones">
    <div class="header-section">
      <h2>我的职业历程</h2>
      <el-button type="primary" @click="showAddDialog">添加里程碑</el-button>
    </div>
    
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="入职培训" name="入职培训"></el-tab-pane>
      <el-tab-pane label="转正" name="转正"></el-tab-pane>
      <el-tab-pane label="部门调整" name="部门调整"></el-tab-pane>
      <el-tab-pane label="职位晋升" name="职位晋升"></el-tab-pane>
      <el-tab-pane label="参赛荣誉" name="参赛荣誉"></el-tab-pane>
      <el-tab-pane label="重大项目" name="重大项目"></el-tab-pane>
    </el-tabs>
    
    <div class="timeline">
      <el-timeline>
        <el-timeline-item
          v-for="milestone in filteredMilestones"
          :key="milestone.id"
          :timestamp="formatDate(milestone.eventDate)"
          placement="top"
        >
          <el-card class="milestone-card" shadow="hover">
            <template #header>
              <div class="milestone-header">
                <h3 class="milestone-title">{{ milestone.title }}</h3>
                <el-tag class="milestone-type-tag" :type="getMilestoneTypeTag(milestone.type)">
                  {{ milestone.type }}
                </el-tag>
              </div>
            </template>
            <div class="milestone-content">
              <p class="milestone-description">{{ milestone.description }}</p>
              <div class="milestone-footer">
                <div class="milestone-date">
                  <el-icon><Calendar /></el-icon>
                  <span>{{ formatDate(milestone.eventDate) }}</span>
                </div>
                <div class="actions">
                  <el-button size="small" @click="editMilestone(milestone)">编辑</el-button>
                  <el-button size="small" type="danger" @click="handleDeleteMilestone(milestone.id)">删除</el-button>
                </div>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
    
    <!-- 添加/编辑里程碑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
    >
      <el-form :model="currentMilestone" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="currentMilestone.title"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="currentMilestone.description"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="currentMilestone.type" placeholder="请选择类型">
            <el-option label="入职培训" value="入职培训"></el-option>
            <el-option label="转正" value="转正"></el-option>
            <el-option label="部门调整" value="部门调整"></el-option>
            <el-option label="职位晋升" value="职位晋升"></el-option>
            <el-option label="参赛荣誉" value="参赛荣誉"></el-option>
            <el-option label="重大项目" value="重大项目"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
            v-model="currentMilestone.eventDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveMilestone">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { getAllMilestones, getMilestonesByEmployeeId, createMilestone, updateMilestone, deleteMilestone } from '@/api/milestone'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Calendar } from '@element-plus/icons-vue'

export default {
  name: 'Milestones',
  components: {
    Calendar
  },
  setup() {
    const activeTab = ref('all')
    const milestones = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentMilestone = ref({
      id: null,
      employeeId: null, // 将在加载时设置为当前用户ID
      title: '',
      description: '',
      type: '',
      eventDate: ''
    })
    const isEditing = ref(false)
    const currentUser = ref(null)
    
    const filteredMilestones = computed(() => {
      if (activeTab.value === 'all') {
        return milestones.value
      }
      return milestones.value.filter(m => m.type === activeTab.value)
    })
    
    const handleTabChange = (tab) => {
      activeTab.value = tab
    }
    
    const getMilestoneTypeTag = (type) => {
      const typeMap = {
        '入职培训': 'primary',
        '转正': 'success',
        '部门调整': 'warning',
        '职位晋升': 'danger',
        '参赛荣誉': 'info',
        '重大项目': ''
      }
      return typeMap[type] || 'info'
    }
    
    const showAddDialog = () => {
      dialogTitle.value = '添加里程碑'
      currentMilestone.value = {
        id: null,
        employeeId: currentUser.value ? currentUser.value.id : null,
        title: '',
        description: '',
        type: '',
        eventDate: ''
      }
      isEditing.value = false
      dialogVisible.value = true
    }
    
    const editMilestone = (milestone) => {
      dialogTitle.value = '编辑里程碑'
      currentMilestone.value = { ...milestone }
      isEditing.value = true
      dialogVisible.value = true
    }
    
    const handleDeleteMilestone = async (id) => {
      try {
        await ElMessageBox.confirm('确认删除该里程碑吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await deleteMilestone(id)
        // 检查后端返回的结果
        if (response.data.success !== false) {
          ElMessage.success('删除成功')
          loadMilestones()
        } else {
          ElMessage.error(response.data.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel' && error !== '取消') {
          ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message || '未知错误'))
        }
      }
    }
    
    const saveMilestone = async () => {
      try {
        // 确保传递正确的数据格式
        const milestoneData = {
          ...currentMilestone.value
        };
        
        // 如果eventDate是日期对象，转换为字符串
        if (milestoneData.eventDate instanceof Date) {
          milestoneData.eventDate = formatDateForBackend(milestoneData.eventDate);
        }
        
        // 确保员工ID正确
        if (!milestoneData.employeeId && currentUser.value) {
          milestoneData.employeeId = currentUser.value.id;
        }
        
        // 如果是编辑操作，确保ID不为空
        if (isEditing.value && !milestoneData.id) {
          ElMessage.error('里程碑ID不能为空')
          return
        }
        
        let response;
        if (isEditing.value) {
          response = await updateMilestone(milestoneData.id, milestoneData)
        } else {
          response = await createMilestone(milestoneData)
        }
        
        // 检查后端返回的结果
        if (response.data.success !== false) {
          ElMessage.success(isEditing.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          loadMilestones()
        } else {
          ElMessage.error(response.data.message || (isEditing.value ? '更新失败' : '创建失败'))
        }
      } catch (error) {
        console.error('保存失败:', error)
        ElMessage.error('保存失败: ' + (error.response?.data?.message || error.message || '未知错误'))
      }
    }
    
    const loadMilestones = async () => {
      try {
        if (currentUser.value && currentUser.value.id) {
          const response = await getMilestonesByEmployeeId(currentUser.value.id)
          // 按事件日期倒序排列（最新的在前面）
          milestones.value = response.data.sort((a, b) => {
            const dateA = new Date(a.eventDate)
            const dateB = new Date(b.eventDate)
            return dateB - dateA
          })
        }
      } catch (error) {
        ElMessage.error('加载里程碑失败: ' + (error.response?.data?.message || error.message || '未知错误'))
      }
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
    
    const formatDateForBackend = (date) => {
      if (!date) return ''
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
    
    onMounted(() => {
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
        loadMilestones()
      }
    })
    
    return {
      activeTab,
      milestones,
      dialogVisible,
      dialogTitle,
      currentMilestone,
      isEditing,
      filteredMilestones,
      handleTabChange,
      showAddDialog,
      editMilestone,
      saveMilestone,
      handleDeleteMilestone,
      formatDate,
      loadMilestones,
      getMilestoneTypeTag
    }
  }
}
</script>

<style scoped>
.milestones {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header-section h2 {
  color: #333;
  font-size: 28px;
  font-weight: 600;
}

.timeline {
  margin-top: 20px;
}

.milestone-card {
  border-radius: 12px;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
}

.milestone-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.milestone-card :deep(.el-card__header) {
  padding: 20px 20px 10px 20px;
  border-bottom: 1px solid #f0f0f0;
  background-color: rgba(255, 255, 255, 0.8);
}

.milestone-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 15px;
}

.milestone-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #333;
  flex: 1;
}

.milestone-type-tag {
  height: 24px;
  line-height: 22px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.milestone-card :deep(.el-card__body) {
  padding: 20px;
}

.milestone-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.milestone-description {
  margin: 0;
  color: #666;
  font-size: 15px;
  line-height: 1.6;
}

.milestone-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  padding-top: 15px;
  border-top: 1px dashed #eee;
}

.milestone-date {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #999;
  font-size: 14px;
}

.milestone-date .el-icon {
  font-size: 16px;
}

.actions {
  display: flex;
  gap: 10px;
}

.actions .el-button {
  border-radius: 6px;
  font-size: 13px;
  padding: 8px 12px;
}

/* 时间轴样式优化 */
:deep(.el-timeline-item__wrapper) {
  padding-bottom: 30px;
}

:deep(.el-timeline-item__tail) {
  left: 3px;
  border-left: 2px solid #e4e7ed;
}

:deep(.el-timeline-item__node) {
  width: 8px;
  height: 8px;
  left: 0;
  background-color: #ff6b35;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .milestones {
    padding: 15px;
  }
  
  .header-section {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .milestone-header {
    flex-direction: column;
    gap: 10px;
  }
  
  .milestone-footer {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
  
  .actions {
    align-self: flex-end;
  }
}
</style>