import apiClient from './user'

const BASE_URL = '/api/skills'

// 创建技能
export const createSkill = (skillData) => {
  return apiClient.post(BASE_URL, skillData)
}

// 获取所有技能
export const getSkills = () => {
  return apiClient.get(BASE_URL)
}

// 根据ID获取技能
export const getSkillById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新技能
export const updateSkill = (id, skillData) => {
  return apiClient.put(`${BASE_URL}/${id}`, skillData)
}

// 删除技能
export const deleteSkill = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}