<template>
  <div class="skills">
    <div class="header-section">
      <h2>技能图谱</h2>
      <el-button type="primary" @click="showCreateDialog">添加技能</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="全部技能" name="all"></el-tab-pane>
      <el-tab-pane label="技术类" name="技术"></el-tab-pane>
      <el-tab-pane label="管理类" name="管理"></el-tab-pane>
      <el-tab-pane label="沟通类" name="沟通"></el-tab-pane>
    </el-tabs>

    <el-table :data="filteredSkills" style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="技能名称" width="200"></el-table-column>
      <el-table-column prop="category" label="分类" width="120"></el-table-column>
      <el-table-column prop="description" label="描述" width="300"></el-table-column>
      <el-table-column label="等级" width="120">
        <template #default="scope">
          <el-rate
            v-model="scope.row.level"
            :max="5"
            disabled
            show-score
            score-template="{value}分"
          ></el-rate>
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
      <el-form :model="currentSkill" label-width="80px">
        <el-form-item label="技能名称">
          <el-input v-model="currentSkill.name"></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="currentSkill.category" placeholder="请选择分类">
            <el-option label="技术" value="技术"></el-option>
            <el-option label="管理" value="管理"></el-option>
            <el-option label="沟通" value="沟通"></el-option>
            <el-option label="其他" value="其他"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="currentSkill.description"
            type="textarea"
            :rows="3"
            placeholder="请输入技能描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="等级">
          <el-rate
            v-model="currentSkill.level"
            :max="5"
            show-score
            score-template="{value}分"
          ></el-rate>
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
import { getSkills, createSkill, updateSkill, deleteSkill } from '@/api/skill'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Skills',
  setup() {
    const skills = ref([])
    const loading = ref(false)
    const activeTab = ref('all')
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentSkill = ref({
      name: '',
      category: '',
      description: '',
      level: 1
    })
    const isEditing = ref(false)

    const filteredSkills = computed(() => {
      if (activeTab.value === 'all') {
        return skills.value
      }
      return skills.value.filter(skill => skill.category === activeTab.value)
    })

    const loadSkills = async () => {
      loading.value = true
      try {
        const response = await getSkills()
        skills.value = response.data
      } catch (error) {
        ElMessage.error('加载技能列表失败')
      } finally {
        loading.value = false
      }
    }

    const handleTabChange = (tab) => {
      activeTab.value = tab
    }

    const showCreateDialog = () => {
      dialogTitle.value = '添加技能'
      currentSkill.value = {
        name: '',
        category: '',
        description: '',
        level: 1
      }
      isEditing.value = false
      dialogVisible.value = true
    }

    const editSkill = (skill) => {
      dialogTitle.value = '编辑技能'
      currentSkill.value = { ...skill }
      isEditing.value = true
      dialogVisible.value = true
    }

    const saveSkill = async () => {
      try {
        if (isEditing.value) {
          await updateSkill(currentSkill.value.id, currentSkill.value)
          ElMessage.success('技能信息更新成功')
        } else {
          await createSkill(currentSkill.value)
          ElMessage.success('技能添加成功')
        }
        dialogVisible.value = false
        loadSkills()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }

    const handleDeleteSkill = async (id) => {
      try {
        await deleteSkill(id)
        ElMessage.success('技能删除成功')
        loadSkills()
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

    onMounted(() => {
      loadSkills()
    })

    return {
      skills,
      filteredSkills,
      loading,
      activeTab,
      dialogVisible,
      dialogTitle,
      currentSkill,
      isEditing,
      handleTabChange,
      showCreateDialog,
      editSkill,
      saveSkill,
      deleteSkill
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