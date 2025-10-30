import axios from 'axios'

const BASE_URL = '/api/milestones'

// 获取所有里程碑
export const getMilestones = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取里程碑
export const getMilestoneById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 根据员工ID获取里程碑
export const getMilestonesByEmployeeId = (employeeId) => {
  return axios.get(`${BASE_URL}/employee/${employeeId}`)
}

// 创建里程碑
export const createMilestone = (milestone) => {
  return axios.post(BASE_URL, milestone)
}

// 更新里程碑
export const updateMilestone = (id, milestone) => {
  return axios.put(`${BASE_URL}/${id}`, milestone)
}

// 删除里程碑
export const deleteMilestone = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}