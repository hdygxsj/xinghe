<template>
  <div class="mentors">
    <div class="header-section">
      <h2>导师管理</h2>
      <el-button type="primary" @click="showMentorshipDialog">申请导师</el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="我的导师" name="mentors"></el-tab-pane>
      <el-tab-pane label="我的学员" name="mentees"></el-tab-pane>
      <el-tab-pane label="所有导师" name="all"></el-tab-pane>
    </el-tabs>

    <!-- 我的导师列表 -->
    <div v-if="activeTab === 'mentors'">
      <el-table :data="myMentors" style="width: 100%" v-loading="loading" class="full-width-table">
        <el-table-column prop="mentorName" label="导师姓名" min-width="120"></el-table-column>
        <el-table-column prop="mentorDepartment" label="部门" min-width="150"></el-table-column>
        <el-table-column prop="mentorPosition" label="职位" min-width="150"></el-table-column>
        <el-table-column prop="mentorExpertise" label="专长" min-width="200"></el-table-column>
        <el-table-column label="状态" min-width="100">
          <template #default="scope">
            <el-tag :type="getMentorshipStatusType(scope.row.status)">
              {{ getMentorshipStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开始日期" min-width="120">
          <template #default="scope">
            {{ formatDate(scope.row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" min-width="150">
          <template #default="scope">
            <el-button size="small" @click="viewMentorship(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 我的学员列表 -->
    <div v-if="activeTab === 'mentees'">
      <el-table :data="myMentees" style="width: 100%" v-loading="loading" class="full-width-table">
        <el-table-column prop="menteeName" label="学员姓名" min-width="120"></el-table-column>
        <el-table-column prop="menteeDepartment" label="部门" min-width="150"></el-table-column>
        <el-table-column prop="menteePosition" label="职位" min-width="150"></el-table-column>
        <el-table-column label="状态" min-width="100">
          <template #default="scope">
            <el-tag :type="getMentorshipStatusType(scope.row.status)">
              {{ getMentorshipStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开始日期" min-width="120">
          <template #default="scope">
            {{ formatDate(scope.row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" min-width="150">
          <template #default="scope">
            <el-button size="small" @click="viewMentorship(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 所有导师列表 -->
    <div v-if="activeTab === 'all'">
      <el-table :data="allMentors" style="width: 100%" v-loading="loading" class="full-width-table">
        <el-table-column prop="name" label="姓名" min-width="120"></el-table-column>
        <el-table-column prop="department" label="部门" min-width="150"></el-table-column>
        <el-table-column prop="position" label="职位" min-width="150"></el-table-column>
        <el-table-column prop="expertise" label="专长" min-width="200"></el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="200"></el-table-column>
        <el-table-column label="操作" fixed="right" min-width="150">
          <template #default="scope">
            <el-button size="small" @click="applyForMentor(scope.row)">申请指导</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 选择导师对话框 -->
    <el-dialog title="选择导师" v-model="selectMentorDialogVisible" width="900px">
      <el-table 
        :data="allMentors" 
        style="width: 100%" 
        v-loading="loading"
        highlight-current-row
        @row-click="handleMentorRowClick"
      >
        <el-table-column type="index" width="50" label="序号"></el-table-column>
        <el-table-column prop="name" label="姓名" width="100"></el-table-column>
        <el-table-column prop="department" label="部门" width="120"></el-table-column>
        <el-table-column prop="position" label="职位" width="120"></el-table-column>
        <el-table-column prop="expertise" label="专长" width="200"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="180"></el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="selectMentor(scope.row)">选择</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="selectMentorDialogVisible = false">取消</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 申请导师对话框 -->
    <el-dialog title="申请导师指导" v-model="mentorshipDialogVisible" width="500px">
      <el-form :model="currentMentorship" label-width="80px">
        <el-form-item label="导师">
          <el-input v-model="selectedMentor.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="指导目标">
          <el-input
            v-model="currentMentorship.goals"
            type="textarea"
            :rows="4"
            placeholder="请输入您希望获得的指导目标"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="mentorshipDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveMentorship">申请</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 导师关系详情对话框 -->
    <el-dialog :title="mentorshipDetailTitle" v-model="mentorshipDetailDialogVisible" width="600px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="导师">{{ currentMentorship.mentorName }}</el-descriptions-item>
        <el-descriptions-item label="学员">{{ currentMentorship.menteeName }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getMentorshipStatusType(currentMentorship.status)">
            {{ getMentorshipStatusText(currentMentorship.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="指导目标">{{ currentMentorship.goals }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ formatDate(currentMentorship.startDate) }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ formatDate(currentMentorship.endDate) }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="mentorshipDetailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { getMentors } from '@/api/mentor'
import { getMentorshipsByMenteeId, getMentorshipsByMentorId, createMentorship } from '@/api/mentorship'
import { ElMessage } from 'element-plus'

export default {
  name: 'Mentors',
  setup() {
    const allMentors = ref([])
    const myMentorships = ref([])
    const loading = ref(false)
    const activeTab = ref('mentors')
    const selectMentorDialogVisible = ref(false)
    const mentorshipDialogVisible = ref(false)
    const mentorshipDetailDialogVisible = ref(false)
    const mentorshipDetailTitle = ref('')
    const currentUser = ref(null)
    const selectedMentor = ref({})
    const currentMentorship = ref({
      mentorId: null,
      menteeId: null,
      status: 'REQUESTED',
      goals: ''
    })

    const myMentors = ref([])
    const myMentees = ref([])

    const loadMentors = async () => {
      loading.value = true
      try {
        const response = await getMentors()
        allMentors.value = response.data
      } catch (error) {
        ElMessage.error('加载导师列表失败')
      } finally {
        loading.value = false
      }
    }

    const loadMyMentorships = async () => {
      loading.value = true
      try {
        if (currentUser.value && currentUser.value.id) {
          // 加载作为学员的导师关系
          const menteeResponse = await getMentorshipsByMenteeId(currentUser.value.id)
          const menteeMentorships = menteeResponse.data.map(m => ({
            ...m,
            mentorName: allMentors.value.find(mentor => mentor.id === m.mentorId)?.name || '未知导师',
            mentorDepartment: allMentors.value.find(mentor => mentor.id === m.mentorId)?.department || '',
            mentorPosition: allMentors.value.find(mentor => mentor.id === m.mentorId)?.position || '',
            mentorExpertise: allMentors.value.find(mentor => mentor.id === m.mentorId)?.expertise || ''
          }))
          myMentors.value = menteeMentorships

          // 加载作为导师的导师关系
          const mentorResponse = await getMentorshipsByMentorId(currentUser.value.id)
          const mentorMentorships = mentorResponse.data.map(m => ({
            ...m,
            menteeName: '学员姓名', // 这里需要通过员工API获取学员信息
            menteeDepartment: '学员部门',
            menteePosition: '学员职位'
          }))
          myMentees.value = mentorMentorships
        }
      } catch (error) {
        ElMessage.error('加载导师关系失败')
      } finally {
        loading.value = false
      }
    }

    const handleTabChange = (tab) => {
      activeTab.value = tab
      if (tab === 'mentors' || tab === 'mentees') {
        loadMyMentorships()
      } else if (tab === 'all') {
        loadMentors()
      }
    }

    const showMentorshipDialog = async () => {
      // 加载所有导师列表
      await loadMentors()
      // 打开选择导师对话框
      selectMentorDialogVisible.value = true
    }

    const selectMentor = (mentor) => {
      selectedMentor.value = mentor
      currentMentorship.value = {
        mentorId: mentor.id,
        menteeId: currentUser.value ? currentUser.value.id : null,
        status: 'REQUESTED',
        goals: ''
      }
      // 关闭选择对话框，打开申请对话框
      selectMentorDialogVisible.value = false
      mentorshipDialogVisible.value = true
    }

    const handleMentorRowClick = (row) => {
      // 可选：点击行也可以选择导师
      // selectMentor(row)
    }

    const applyForMentor = (mentor) => {
      selectedMentor.value = mentor
      currentMentorship.value = {
        mentorId: mentor.id,
        menteeId: currentUser.value ? currentUser.value.id : null,
        status: 'REQUESTED',
        goals: ''
      }
      mentorshipDialogVisible.value = true
    }

    const saveMentorship = async () => {
      if (!currentMentorship.value.goals || currentMentorship.value.goals.trim() === '') {
        ElMessage.warning('请输入指导目标')
        return
      }
      try {
        await createMentorship(currentMentorship.value)
        ElMessage.success('导师申请已提交，请等待导师确认')
        mentorshipDialogVisible.value = false
        // 切换到“我的导师”标签页
        activeTab.value = 'mentors'
        await loadMyMentorships()
      } catch (error) {
        ElMessage.error('申请失败，请重试')
      }
    }

    const viewMentorship = (mentorship) => {
      currentMentorship.value = { ...mentorship }
      mentorshipDetailTitle.value = '导师关系详情'
      mentorshipDetailDialogVisible.value = true
    }

    const formatDate = (dateString) => {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('zh-CN')
    }

    const getMentorshipStatusText = (status) => {
      const statusMap = {
        'REQUESTED': '已申请',
        'ACCEPTED': '已接受',
        'REJECTED': '已拒绝',
        'COMPLETED': '已完成'
      }
      return statusMap[status] || status
    }

    const getMentorshipStatusType = (status) => {
      const typeMap = {
        'REQUESTED': 'warning',
        'ACCEPTED': 'success',
        'REJECTED': 'danger',
        'COMPLETED': 'info'
      }
      return typeMap[status] || 'info'
    }

    onMounted(() => {
      // Get current user from local storage
      const user = localStorage.getItem('currentUser')
      if (user) {
        currentUser.value = JSON.parse(user)
        loadMentors()
        loadMyMentorships()
      }
    })

    return {
      allMentors,
      myMentors,
      myMentees,
      loading,
      activeTab,
      selectMentorDialogVisible,
      mentorshipDialogVisible,
      mentorshipDetailDialogVisible,
      mentorshipDetailTitle,
      selectedMentor,
      currentMentorship,
      handleTabChange,
      showMentorshipDialog,
      selectMentor,
      handleMentorRowClick,
      applyForMentor,
      saveMentorship,
      viewMentorship,
      formatDate,
      getMentorshipStatusText,
      getMentorshipStatusType,
      loadMentors,
      loadMyMentorships
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

.full-width-table {
  width: 100%;
  margin-top: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-section {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .full-width-table :deep(.el-table__column) {
    padding: 5px 0;
  }
}
</style>