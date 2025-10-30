import apiClient from './user'

const BASE_URL = '/api/employee-skills'

// 创建员工技能
export const createEmployeeSkill = (employeeSkillData) => {
  return apiClient.post(BASE_URL, employeeSkillData)
}

// 获取所有员工技能
export const getAllEmployeeSkills = () => {
  return apiClient.get(BASE_URL)
}

// 根据员工ID获取员工技能
export const getEmployeeSkillsByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/employee/${employeeId}`)
}

// 根据ID获取员工技能
export const getEmployeeSkillById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新员工技能
export const updateEmployeeSkill = (id, employeeSkillData) => {
  return apiClient.put(`${BASE_URL}/${id}`, employeeSkillData)
}

// 删除员工技能
export const deleteEmployeeSkill = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}

// 获取员工技能统计（按技能类型分组）
export const getEmployeeSkillStatistics = (employeeId) => {
  return apiClient.get(`${BASE_URL}/statistics/${employeeId}`)
}