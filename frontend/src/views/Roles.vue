<template>
  <div class="roles">
    <div class="header-section">
      <h2>角色管理</h2>
      <el-button type="primary" @click="showCreateDialog">新增角色</el-button>
    </div>

    <el-tabs v-model="activeTab" class="role-tabs">
      <!-- 角色定义标签页 -->
      <el-tab-pane label="角色定义" name="roles">
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
      </el-tab-pane>

      <!-- 员工角色分配标签页 -->
      <el-tab-pane label="员工角色" name="employees">
        <div class="employee-role-section">
          <el-input
            v-model="searchQuery"
            placeholder="搜索员工姓名或工号"
            style="width: 300px; margin-bottom: 20px"
            clearable
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <el-table :data="filteredEmployees" style="width: 100%" v-loading="employeeLoading">
            <el-table-column prop="employeeId" label="工号" width="150"></el-table-column>
            <el-table-column prop="name" label="姓名" width="150"></el-table-column>
            <el-table-column prop="department" label="部门" width="150"></el-table-column>
            <el-table-column prop="position" label="职位" width="150"></el-table-column>
            <el-table-column label="当前角色" width="150">
              <template #default="scope">
                <el-tag :type="getRoleTagType(scope.row.role)">
                  {{ scope.row.role || 'USER' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="scope">
                <el-button size="small" type="primary" @click="showEditEmployeeRoleDialog(scope.row)">
                  编辑角色
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

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

    <!-- 编辑员工角色对话框 -->
    <el-dialog title="编辑员工角色" v-model="employeeRoleDialogVisible" width="500px">
      <el-form :model="currentEmployee" label-width="100px">
        <el-form-item label="员工姓名">
          <el-input v-model="currentEmployee.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="工号">
          <el-input v-model="currentEmployee.employeeId" disabled></el-input>
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="currentEmployee.department" disabled></el-input>
        </el-form-item>
        <el-form-item label="职位">
          <el-input v-model="currentEmployee.position" disabled></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="currentEmployee.role" placeholder="请选择角色" style="width: 100%">
            <el-option
              v-for="role in roles"
              :key="role.roleName"
              :label="role.roleName"
              :value="role.roleName"
            >
              <span>{{ role.roleName }}</span>
              <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px">
                {{ role.description }}
              </span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="employeeRoleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEmployeeRole">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue'
import { getAllRoles, createRole, updateRole, deleteRole as deleteRoleApi } from '@/api/role'
import { getAllEmployees, updateEmployeeRole } from '@/api/employee'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

export default {
  name: 'Roles',
  components: {
    Search
  },
  setup() {
    const activeTab = ref('roles')
    const roles = ref([])
    const loading = ref(false)
    const dialogVisible = ref(false)
    const dialogTitle = ref('')
    const currentRole = ref({
      roleName: '',
      description: ''
    })
    const isEditing = ref(false)

    // Employee role management
    const employees = ref([])
    const employeeLoading = ref(false)
    const employeeRoleDialogVisible = ref(false)
    const currentEmployee = ref({
      id: null,
      name: '',
      employeeId: '',
      department: '',
      position: '',
      role: ''
    })
    const searchQuery = ref('')

    // Computed property for filtered employees
    const filteredEmployees = computed(() => {
      if (!searchQuery.value) {
        return employees.value
      }
      const query = searchQuery.value.toLowerCase()
      return employees.value.filter(emp => 
        emp.name?.toLowerCase().includes(query) || 
        emp.employeeId?.toLowerCase().includes(query)
      )
    })

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

    const loadEmployees = async () => {
      employeeLoading.value = true
      try {
        const response = await getAllEmployees()
        employees.value = response.data
      } catch (error) {
        ElMessage.error('加载员工列表失败')
      } finally {
        employeeLoading.value = false
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

    const showEditEmployeeRoleDialog = (employee) => {
      currentEmployee.value = { ...employee }
      if (!currentEmployee.value.role) {
        currentEmployee.value.role = 'USER'
      }
      employeeRoleDialogVisible.value = true
    }

    const saveEmployeeRole = async () => {
      try {
        await updateEmployeeRole(currentEmployee.value.id, currentEmployee.value.role)
        ElMessage.success('员工角色更新成功')
        employeeRoleDialogVisible.value = false
        loadEmployees()
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '更新失败')
      }
    }

    const getRoleTagType = (role) => {
      const typeMap = {
        'ADMIN': 'danger',
        'MANAGER': 'warning',
        'USER': 'info'
      }
      return typeMap[role] || 'info'
    }

    onMounted(() => {
      loadRoles()
      loadEmployees()
    })

    return {
      activeTab,
      roles,
      loading,
      dialogVisible,
      dialogTitle,
      currentRole,
      isEditing,
      employees,
      employeeLoading,
      employeeRoleDialogVisible,
      currentEmployee,
      searchQuery,
      filteredEmployees,
      showCreateDialog,
      editRole,
      saveRole,
      deleteRole,
      formatDate,
      showEditEmployeeRoleDialog,
      saveEmployeeRole,
      getRoleTagType
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

.role-tabs {
  margin-top: 20px;
}

.employee-role-section {
  padding: 10px 0;
}
</style>
