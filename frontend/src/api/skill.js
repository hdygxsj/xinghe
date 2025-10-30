import axios from 'axios'

const BASE_URL = '/api/skills'

// 获取所有技能
export const getSkills = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取技能
export const getSkillById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 创建技能
export const createSkill = (skill) => {
  return axios.post(BASE_URL, skill)
}

// 更新技能
export const updateSkill = (id, skill) => {
  return axios.put(`${BASE_URL}/${id}`, skill)
}

// 删除技能
export const deleteSkill = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}