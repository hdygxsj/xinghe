import apiClient from './user'

const BASE_URL = '/api/career-plans'

// 创建职业规划
export const createCareerPlan = (careerPlanData) => {
  return apiClient.post(BASE_URL, careerPlanData)
}

// 获取所有职业规划
export const getAllCareerPlans = () => {
  return apiClient.get(BASE_URL)
}

// 根据员工ID获取职业规划
export const getCareerPlansByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/employee/${employeeId}`)
}

// 根据ID获取职业规划
export const getCareerPlanById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新职业规划
export const updateCareerPlan = (id, careerPlanData) => {
  return apiClient.put(`${BASE_URL}/${id}`, careerPlanData)
}

// 删除职业规划
export const deleteCareerPlan = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}