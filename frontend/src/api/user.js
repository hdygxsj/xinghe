import axios from 'axios'

const BASE_URL = '/api/auth'

// 请求拦截器，自动添加token
axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器，处理401错误
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      // 清除本地存储的用户信息和token
      localStorage.removeItem('currentUser')
      localStorage.removeItem('token')
      // 重定向到登录页面
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// 用户注册
export const register = (userData) => {
  return axios.post(`${BASE_URL}/register`, userData)
}

// 用户登录
export const login = (credentials) => {
  return axios.post(`${BASE_URL}/login`, credentials)
}

// 导出axios实例作为默认导出
export default axios