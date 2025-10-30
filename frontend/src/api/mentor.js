import axios from 'axios'

const BASE_URL = '/api/mentors'

// 获取所有导师
export const getMentors = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取导师
export const getMentorById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 创建导师
export const createMentor = (mentor) => {
  return axios.post(BASE_URL, mentor)
}

// 更新导师
export const updateMentor = (id, mentor) => {
  return axios.put(`${BASE_URL}/${id}`, mentor)
}

// 删除导师
export const deleteMentor = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}