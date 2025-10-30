<template>
  <div class="mentors">
    <div class="header-section">
      <h2>导师管理</h2>
      <el-button type="primary" @click="showCreateDialog">添加导师</el-button>
    </div>

    <el-table :data="mentors" style="width: 100%" v-loading="loading">
      <el-table-column prop="name" label="姓名" width="120"></el-table-column>
      <el-table-column prop="department" label="部门" width="150"></el-table-column>
      <el-table-column prop="position" label="职位" width="150"></el-table-column>
      <el-table-column prop="expertise" label="专长" width="200"></el-table-column>
      <el-table-column prop="email" label="邮箱" width="200"></el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="editMentor(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteMentor(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑导师对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="currentMentor" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="currentMentor.name"></el-input>
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="currentMentor.department"></el-input>
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="currentMentor.position"></el-input>
        </el-form-item>
        <el-form-item label="专长">
          <el-input v-model="currentMentor.expertise"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="currentMentor.email"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="currentMentor.phone"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveMentor">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getMentors, createMentor, updateMentor, deleteMentor } from '@/api/mentor'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Mentors',
  setup() {
    const mentors = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentMentor = ref({
      name: '',
      department: '',
      position: '',
      expertise: '',
      email: '',
      phone: ''
    })
    const isEditing = ref(false)

    const loadMentors = async () => {
      loading.value = true
      try {
        const response = await getMentors()
        mentors.value = response.data
      } catch (error) {
        ElMessage.error('加载导师列表失败')
      } finally {
        loading.value = false
      }
    }

    const showCreateDialog = () => {
      dialogTitle.value = '添加导师'
      currentMentor.value = {
        name: '',
        department: '',
        position: '',
        expertise: '',
        email: '',
        phone: ''
      }
      isEditing.value = false
      dialogVisible.value = true
    }

    const editMentor = (mentor) => {
      dialogTitle.value = '编辑导师'
      currentMentor.value = { ...mentor }
      isEditing.value = true
      dialogVisible.value = true
    }

    const saveMentor = async () => {
      try {
        if (isEditing.value) {
          await updateMentor(currentMentor.value.id, currentMentor.value)
          ElMessage.success('导师信息更新成功')
        } else {
          await createMentor(currentMentor.value)
          ElMessage.success('导师添加成功')
        }
        dialogVisible.value = false
        loadMentors()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }

    const handleDeleteMentor = async (id) => {
      try {
        await deleteMentor(id)
        ElMessage.success('导师删除成功')
        loadMentors()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }

    const deleteMentor = (id) => {
      ElMessageBox.confirm('确定要删除这个导师吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleDeleteMentor(id)
      }).catch(() => {
        // 用户取消删除
      })
    }

    onMounted(() => {
      loadMentors()
    })

    return {
      mentors,
      loading,
      dialogVisible,
      dialogTitle,
      currentMentor,
      isEditing,
      showCreateDialog,
      editMentor,
      saveMentor,
      deleteMentor
    }
  }
}
</script>

<style scoped>
.mentors {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>