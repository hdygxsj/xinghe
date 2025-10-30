import apiClient from './user'

const BASE_URL = '/api/career-info'

// 获取最近活动
export const getRecentActivities = () => {
  return apiClient.get(`${BASE_URL}/recent-activities`)
}