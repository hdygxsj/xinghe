import axios from 'axios'

const BASE_URL = '/api/auth'

// 用户注册
export const register = (userData) => {
  return axios.post(`${BASE_URL}/register`, userData)
}

// 用户登录
export const login = (credentials) => {
  return axios.post(`${BASE_URL}/login`, credentials)
}