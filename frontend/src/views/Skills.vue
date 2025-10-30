<template>
  <div class="skills">
    <div class="header-section">
      <h2>我的技能图谱</h2>
      <el-button type="primary" @click="showSkillDialog">添加技能</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部技能" name="all"></el-tab-pane>
      <el-tab-pane label="技术类" name="技术"></el-tab-pane>
      <el-tab-pane label="管理类" name="管理"></el-tab-pane>
      <el-tab-pane label="沟通类" name="沟通"></el-tab-pane>
    </el-tabs>

    <el-table :data="filteredSkills" style="width: 100%" v-loading="loading">
      <el-table-column prop="skillName" label="技能名称" width="200"></el-table-column>
      <el-table-column prop="skillCategory" label="分类" width="120"></el-table-column>
      <el-table-column prop="skillDescription" label="描述" width="300"></el-table-column>
      <el-table-column label="熟练度" width="120">
        <template #default="scope">
          <el-rate
            v-model="scope.row.proficiencyLevel"
            :max="5"
            disabled
            show-score
            score-template="{value}分"
          ></el-rate>
        </template>
      </el-table-column>
      <el-table-column label="获得日期" width="120">
        <template #default="scope">
          {{ formatDate(scope.row.acquiredDate) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editSkill(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteSkill(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑技能对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form :model="currentEmployeeSkill" label-width="80px">
        <el-form-item label="技能">
          <el-select 
            v-model="currentEmployeeSkill.skillId" 
            placeholder="请选择技能"
            filterable
          >
            <el-option
              v-for="skill in allSkills"
              :key="skill.id"
              :label="skill.name"
              :value="skill.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="熟练度">
          <el-rate
            v-model="currentEmployeeSkill.proficiencyLevel"
            :max="5"
            show-score
            score-template="{value}分"
          ></el-rate>
        </el-form-item>
        <el-form-item label="获得日期">
          <el-date-picker
            v-model="currentEmployeeSkill.acquiredDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD 00:00:00"
          ></el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveSkill">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { getEmployeeSkillsByEmployeeId, createEmployeeSkill, updateEmployeeSkill, deleteEmployeeSkill } from '@/api/employeeSkill'
import { getSkills } from '@/api/skill'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Skills',
  setup() {
    const employeeSkills = ref([])
    const allSkills = ref([])
    const loading = ref(false)
    const activeTab = ref('all')
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentUser = ref(null)
    const currentEmployeeSkill = ref({
      employeeId: null,
      skillId: null,
      proficiencyLevel: 1,
      acquiredDate: ''
    })
    const isEditing = ref(false)

    const filteredSkills = computed(() => {
      // 合并员工技能和技能信息
      const skillsWithDetails = employeeSkills.value.map(empSkill => {
        const skill = allSkills.value.find(s => s.id === empSkill.skillId) || {}
        return {
          ...empSkill,
          skillName: skill.name,
          skillCategory: skill.category,
          skillDescription: skill.description
        }
      })
      
      if (activeTab.value === 'all') {
        return skillsWithDetails
      }
      return skillsWithDetails.filter(skill => skill.skillCategory === activeTab.value)
    })

    const loadEmployeeSkills = async () => {
      loading.value = true
      try {
        if (currentUser.value && currentUser.value.id) {
          const response = await getEmployeeSkillsByEmployeeId(currentUser.value.id)
          employeeSkills.value = response.data
        }
      } catch (error) {
        ElMessage.error('加载员工技能列表失败')
      } finally {
        loading.value = false
      }
    }

    const loadAllSkills = async () => {
      try {
        const response = await getSkills()
        allSkills.value = response.data
      } catch (error) {
        ElMessage.error('加载技能列表失败')
      }
    }

    const handleTabChange = (tab) => {
      activeTab.value = tab
    }

    const showSkillDialog = () => {
      dialogTitle.value = '添加技能'
      currentEmployeeSkill.value = {
        employeeId: currentUser.value ? currentUser.value.id : null,
        skillId: null,
        proficiencyLevel: 1,
        acquiredDate: ''
      }
      isEditing.value = false
      dialogVisible.value = true
    }

    const editSkill = (employeeSkill) => {
      dialogTitle.value = '编辑技能'
      currentEmployeeSkill.value = { ...employeeSkill }
      isEditing.value = true
      dialogVisible.value = true
    }

    const saveSkill = async () => {
      try {
        // 确保员工ID正确
        if (!currentEmployeeSkill.value.employeeId && currentUser.value) {
          currentEmployeeSkill.value.employeeId = currentUser.value.id;
        }
        
        if (isEditing.value) {
          await updateEmployeeSkill(currentEmployeeSkill.value.id, currentEmployeeSkill.value)
          ElMessage.success('技能信息更新成功')
        } else {
          await createEmployeeSkill(currentEmployeeSkill.value)
          ElMessage.success('技能添加成功')
        }
        dialogVisible.value = false
        loadEmployeeSkills()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }

    const handleDeleteSkill = async (id) => {
      try {
        await deleteEmployeeSkill(id)
        ElMessage.success('技能删除成功')
        loadEmployeeSkills()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }

    const deleteSkill = (id) => {
      ElMessageBox.confirm('确定要删除这个技能吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleDeleteSkill(id)
      }).catch(() => {
        // 用户取消删除
      })
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
        loadEmployeeSkills()
        loadAllSkills()
      }
    })

    return {
      employeeSkills,
      allSkills,
      filteredSkills,
      loading,
      activeTab,
      dialogVisible,
      dialogTitle,
      currentEmployeeSkill,
      isEditing,
      handleTabChange,
      showSkillDialog,
      editSkill,
      saveSkill,
      deleteSkill,
      formatDate,
      loadEmployeeSkills
    }
  }
}
</script>

<style scoped>
.skills {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>