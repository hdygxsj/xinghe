import apiClient from './user'

const BASE_URL = '/api/career-info'

// 获取最近活动（支持分页）
export const getRecentActivities = (page = 0, size = 5) => {
  return apiClient.get(`${BASE_URL}/recent-activities?page=${page}&size=${size}`)
}