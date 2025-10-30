import axios from 'axios'

const BASE_URL = '/api/employee-skills'

// 获取所有员工技能
export const getEmployeeSkills = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取员工技能
export const getEmployeeSkillById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 根据员工ID获取技能
export const getEmployeeSkillsByEmployeeId = (employeeId) => {
  return axios.get(`${BASE_URL}/employee/${employeeId}`)
}

// 创建员工技能
export const createEmployeeSkill = (employeeSkill) => {
  return axios.post(BASE_URL, employeeSkill)
}

// 更新员工技能
export const updateEmployeeSkill = (id, employeeSkill) => {
  return axios.put(`${BASE_URL}/${id}`, employeeSkill)
}

// 删除员工技能
export const deleteEmployeeSkill = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}