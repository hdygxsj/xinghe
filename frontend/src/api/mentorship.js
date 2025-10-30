import axios from 'axios'

const BASE_URL = '/api/mentorships'

// 获取所有导师关系
export const getMentorships = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取导师关系
export const getMentorshipById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 根据导师ID获取导师关系
export const getMentorshipsByMentorId = (mentorId) => {
  return axios.get(`${BASE_URL}/mentor/${mentorId}`)
}

// 根据学员ID获取导师关系
export const getMentorshipsByMenteeId = (menteeId) => {
  return axios.get(`${BASE_URL}/mentee/${menteeId}`)
}

// 创建导师关系
export const createMentorship = (mentorship) => {
  return axios.post(BASE_URL, mentorship)
}

// 更新导师关系
export const updateMentorship = (id, mentorship) => {
  return axios.put(`${BASE_URL}/${id}`, mentorship)
}

// 删除导师关系
export const deleteMentorship = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}