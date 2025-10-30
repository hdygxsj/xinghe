<template>
  <div class="roles">
    <div class="header-section">
      <h2>角色管理</h2>
      <el-button type="primary" @click="showCreateDialog">新增角色</el-button>
    </div>

    <el-table :data="roles" style="width: 100%" v-loading="loading">
      <el-table-column prop="roleName" label="角色名称" width="200"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editRole(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteRole(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/编辑角色对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="currentRole" label-width="100px">
        <el-form-item label="角色名称">
          <el-input v-model="currentRole.roleName" placeholder="例如: MANAGER"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="currentRole.description"
            type="textarea"
            :rows="4"
            placeholder="请输入角色描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveRole">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getAllRoles, createRole, updateRole, deleteRole as deleteRoleApi } from '@/api/role'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'Roles',
  setup() {
    const roles = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentRole = ref({
      roleName: '',
      description: ''
    })
    const isEditing = ref(false)

    const loadRoles = async () => {
      loading.value = true
      try {
        const response = await getAllRoles()
        roles.value = response.data
      } catch (error) {
        ElMessage.error('加载角色列表失败')
      } finally {
        loading.value = false
      }
    }

    const showCreateDialog = () => {
      dialogTitle.value = '新增角色'
      currentRole.value = {
        roleName: '',
        description: ''
      }
      isEditing.value = false
      dialogVisible.value = true
    }

    const editRole = (role) => {
      dialogTitle.value = '编辑角色'
      currentRole.value = { ...role }
      isEditing.value = true
      dialogVisible.value = true
    }

    const saveRole = async () => {
      try {
        if (isEditing.value) {
          await updateRole(currentRole.value.id, currentRole.value)
          ElMessage.success('角色更新成功')
        } else {
          await createRole(currentRole.value)
          ElMessage.success('角色创建成功')
        }
        dialogVisible.value = false
        loadRoles()
      } catch (error) {
        ElMessage.error('保存失败')
      }
    }

    const handleDeleteRole = async (id) => {
      try {
        await deleteRoleApi(id)
        ElMessage.success('角色删除成功')
        loadRoles()
      } catch (error) {
        ElMessage.error('删除失败')
      }
    }

    const deleteRole = (id) => {
      ElMessageBox.confirm('确定要删除这个角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        handleDeleteRole(id)
      }).catch(() => {
        // 用户取消删除
      })
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN')
    }

    onMounted(() => {
      loadRoles()
    })

    return {
      roles,
      loading,
      dialogVisible,
      dialogTitle,
      currentRole,
      isEditing,
      showCreateDialog,
      editRole,
      saveRole,
      deleteRole,
      formatDate
    }
  }
}
</script>

<style scoped>
.roles {
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
