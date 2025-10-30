<template>
  <div class="career-plans">
    <div class="header-section">
      <h2>职业规划</h2>
      <el-button type="primary" @click="showCreateDialog">制定规划</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部" name="all"></el-tab-pane>
      <el-tab-pane label="草稿" name="DRAFT"></el-tab-pane>
      <el-tab-pane label="进行中" name="ACTIVE"></el-tab-pane>
      <el-tab-pane label="已完成" name="COMPLETED"></el-tab-pane>
    </el-tabs>

    <el-table :data="filteredPlans" style="width: 100%" v-loading="loading">
      <el-table-column prop="title" label="规划标题" width="200"></el-table-column>
      <el-table-column prop="targetPosition" label="目标职位" width="150"></el-table-column>
      <el-table-column label="目标日期" width="120">
        <template #default="scope">
          {{ formatDate(scope.row.targetDate) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="120">
        <template #default="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="viewPlan(scope.row)">查看</el-button>
          <el-button size="small" @click="editPlan(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deletePlan(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑职业规划对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="currentPlan" label-width="100px">
        <el-form-item label="规划标题">
          <el-input v-model="currentPlan.title"></el-input>
        </el-form-item>
        <el-form-item label="目标职位">
          <el-input v-model="currentPlan.targetPosition"></el-input>
        </el-form-item>
        <el-form-item label="目标日期">
          <el-date-picker
            v-model="currentPlan.targetDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="currentPlan.status" placeholder="请选择状态">
            <el-option label="草稿" value="DRAFT"></el-option>
            <el-option label="进行中" value="ACTIVE"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="已取消" value="CANCELLED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="currentPlan.description"
            type="textarea"
            :rows="4"
            placeholder="请输入规划描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="savePlan">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看职业规划对话框 -->
    <el-dialog title="职业规划详情" v-model="viewDialogVisible" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="规划标题">{{ currentPlan.title }}</el-descriptions-item>
        <el-descriptions-item label="目标职位">{{ currentPlan.targetPosition }}</el-descriptions-item>
        <el-descriptions-item label="目标日期">{{ formatDate(currentPlan.targetDate) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTagType(currentPlan.status)">
            {{ getStatusText(currentPlan.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述">{{ currentPlan.description }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { getCareerPlans, createCareerPlan, updateCareerPlan, deleteCareerPlan } from '@/api/careerPlan'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'CareerPlans',
  setup() {
    const plans = ref([])
    const loading = ref(false)
    const activeTab = ref('all')
    const dialogVisible = ref(false)
    const viewDialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentPlan = ref({
      title: '',
      targetPosition: '',
      targetDate: '',
      status: 'DRAFT',
      description: ''
    })
    const isEditing = ref(false)

    const filteredPlans = computed(() => {
      if (activeTab.value === 'all') {
        return plans.value
      }
      return plans.value.filter(plan => plan.status === activeTab.value)
    })

    const loadPlans = async () => {
      loading.value = true
      try {
        const response = await getCareerPlans()
        plans.value = response.data
      } catch (error) {
        ElMessage.error('加载职业规划列表失败')
      } finally {
        loading.value = false
      }
    }

    const handleTabChange = (tab) => {
      activeTab.value = tab
    }

    const showCreateDialog = () => {
      dialogTitle.value = '制定职业规划'
      currentPlan.value = {
        title: '',
        targetPosition: '',
        targetDate: '',
        status: 'DRAFT',
        description: ''
      }
      isEditing.value = false
      dialogVisible.value = true
    }

    const editPlan = (plan) => {
      dialogTitle.value = '编辑职业规划'
      currentPlan.value = { ...plan }
      isEditing.value = true
      dialogVisible.value = true
    }

    const viewPlan = (plan) => {
      currentPlan.value = { ...plan }
      viewDialogVisible.value = true
    }

    const savePlan = async () => {
      try {
        if (isEditing.value) {
          await updateCareerPlan(currentPlan.value.id, currentPlan.value)
          ElMessage.success('职业规划更新成功')
        } else {
          await createCareerPlan(currentPlan.value)
          ElMessage.success('职业规划制定成功')
        }
        dialogVisible.value = false
        loadPlans()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }

    const handleDeletePlan = async (id) => {
      try {
        await deleteCareerPlan(id)
        ElMessage.success('职业规划删除成功')
        loadPlans()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }

    const deletePlan = (id) => {
      ElMessageBox.confirm('确定要删除这个职业规划吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleDeletePlan(id)
      }).catch(() => {
        // 用户取消删除
      })
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    const getStatusText = (status) => {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    }

    const getStatusTagType = (status) => {
      const typeMap = {
        'DRAFT': 'info',
        'ACTIVE': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return typeMap[status] || 'info'
    }

    onMounted(() => {
      loadPlans()
    })

    return {
      plans,
      filteredPlans,
      loading,
      activeTab,
      dialogVisible,
      viewDialogVisible,
      dialogTitle,
      currentPlan,
      isEditing,
      handleTabChange,
      showCreateDialog,
      editPlan,
      viewPlan,
      savePlan,
      deletePlan,
      formatDate,
      getStatusText,
      getStatusTagType
    }
  }
}
</script>

<style scoped>
.career-plans {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>