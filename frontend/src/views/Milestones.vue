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
          <el-card>
            <template #header>
              <h4>{{ milestone.title }}</h4>
            </template>
            <p>{{ milestone.description }}</p>
            <div class="milestone-footer">
              <span class="milestone-type">{{ milestone.type }}</span>
              <div class="actions">
                <el-button size="small" @click="editMilestone(milestone)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteMilestone(milestone.id)">删除</el-button>
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
            value-format="YYYY-MM-DD"
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
import { getMilestones, createMilestone, updateMilestone, deleteMilestone } from '@/api/milestone'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Milestones',
  setup() {
    const activeTab = ref('all')
    const milestones = ref([])
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentMilestone = ref({
      id: null,
      title: '',
      description: '',
      type: '',
      eventDate: ''
    })
    const isEditing = ref(false)
    
    const filteredMilestones = computed(() => {
      if (activeTab.value === 'all') {
        return milestones.value
      }
      return milestones.value.filter(m => m.type === activeTab.value)
    })
    
    const handleTabChange = (tab) => {
      activeTab.value = tab
    }
    
    const showAddDialog = () => {
      dialogTitle.value = '添加里程碑'
      currentMilestone.value = {
        id: null,
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
    
    const saveMilestone = async () => {
      try {
        if (isEditing.value) {
          await updateMilestone(currentMilestone.value.id, currentMilestone.value)
          ElMessage.success('更新成功')
        } else {
          await createMilestone(currentMilestone.value)
          ElMessage.success('创建成功')
        }
        dialogVisible.value = false
        loadMilestones()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }
    
    const deleteMilestone = async (id) => {
      try {
        await ElMessageBox.confirm('确认删除该里程碑吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await deleteMilestone(id)
        ElMessage.success('删除成功')
        loadMilestones()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }
    
    const loadMilestones = async () => {
      try {
        const response = await getMilestones()
        milestones.value = response.data
      } catch (error) {
        ElMessage.error('加载里程碑失败')
      }
    }
    
    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }
    
    onMounted(() => {
      loadMilestones()
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
      deleteMilestone,
      formatDate,
      loadMilestones
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

.timeline {
  margin-top: 20px;
}

.milestone-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.milestone-type {
  background-color: #ff6b35;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}

.actions {
  display: flex;
  gap: 10px;
}
</style>