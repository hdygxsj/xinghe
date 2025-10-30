import apiClient from './user'

const BASE_URL = '/api/mentorships'

// 创建导师关系
export const createMentorship = (mentorshipData) => {
  return apiClient.post(BASE_URL, mentorshipData)
}

// 获取所有导师关系
export const getAllMentorships = () => {
  return apiClient.get(BASE_URL)
}

// 根据导师ID获取导师关系
export const getMentorshipsByMentorId = (mentorId) => {
  return apiClient.get(`${BASE_URL}/mentor/${mentorId}`)
}

// 根据学员ID获取导师关系
export const getMentorshipsByMenteeId = (menteeId) => {
  return apiClient.get(`${BASE_URL}/mentee/${menteeId}`)
}

// 根据ID获取导师关系
export const getMentorshipById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新导师关系
export const updateMentorship = (id, mentorshipData) => {
  return apiClient.put(`${BASE_URL}/${id}`, mentorshipData)
}

// 删除导师关系
export const deleteMentorship = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}