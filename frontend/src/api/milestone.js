import apiClient from './user'

const BASE_URL = '/api/milestones'

// 获取所有里程碑
export const getAllMilestones = () => {
  return apiClient.get(BASE_URL)
}

// 根据ID获取里程碑
export const getMilestoneById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 根据员工ID获取里程碑
export const getMilestonesByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/employee/${employeeId}`)
}

// 创建里程碑
export const createMilestone = (milestone) => {
  return apiClient.post(BASE_URL, milestone)
}

// 更新里程碑
export const updateMilestone = (id, milestone) => {
  return apiClient.put(`${BASE_URL}/${id}`, milestone)
}

// 删除里程碑
export const deleteMilestone = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}