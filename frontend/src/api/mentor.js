import apiClient from './user'

const BASE_URL = '/api/mentors'

// 创建导师
export const createMentor = (mentorData) => {
  return apiClient.post(BASE_URL, mentorData)
}

// 获取所有导师
export const getMentors = () => {
  return apiClient.get(BASE_URL)
}

// 根据ID获取导师
export const getMentorById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新导师
export const updateMentor = (id, mentorData) => {
  return apiClient.put(`${BASE_URL}/${id}`, mentorData)
}

// 删除导师
export const deleteMentor = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}