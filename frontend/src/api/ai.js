import axios from 'axios'

const BASE_URL = '/api/ai'

// 发送消息到AI助手
export const sendMessageToAI = (data) => {
  return axios.post(`${BASE_URL}/chat`, data)
}

// 获取职业发展建议
export const getCareerAdvice = (employeeId) => {
  return axios.get(`${BASE_URL}/career-advice`, {
    params: { employeeId }
  })
}

// 获取学习路径推荐
export const getLearningPath = (employeeId) => {
  return axios.get(`${BASE_URL}/learning-path`, {
    params: { employeeId }
  })
}

// 获取简历优化建议
export const optimizeResume = (employeeId) => {
  return axios.get(`${BASE_URL}/resume-optimize`, {
    params: { employeeId }
  })
}