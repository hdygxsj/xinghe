import apiClient from './user'

const BASE_URL = '/api/employees'

// 获取所有员工
export const getAllEmployees = () => {
  return apiClient.get(BASE_URL)
}

// 根据ID获取员工
export const getEmployeeById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新员工
export const updateEmployee = (id, employeeData) => {
  return apiClient.put(`${BASE_URL}/${id}`, employeeData)
}

// 删除员工
export const deleteEmployee = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}

// 更新员工角色
export const updateEmployeeRole = (id, role) => {
  return apiClient.patch(`${BASE_URL}/${id}/role`, { role })
}