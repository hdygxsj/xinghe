import axios from 'axios'

const BASE_URL = '/api/career-plans'

// 获取所有职业规划
export const getCareerPlans = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取职业规划
export const getCareerPlanById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 根据员工ID获取职业规划
export const getCareerPlansByEmployeeId = (employeeId) => {
  return axios.get(`${BASE_URL}/employee/${employeeId}`)
}

// 创建职业规划
export const createCareerPlan = (careerPlan) => {
  return axios.post(BASE_URL, careerPlan)
}

// 更新职业规划
export const updateCareerPlan = (id, careerPlan) => {
  return axios.put(`${BASE_URL}/${id}`, careerPlan)
}

// 删除职业规划
export const deleteCareerPlan = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}