import axios from 'axios'

const BASE_URL = '/api/employees'

// 获取所有员工
export const getEmployees = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取员工
export const getEmployeeById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 创建员工
export const createEmployee = (employee) => {
  return axios.post(BASE_URL, employee)
}

// 更新员工
export const updateEmployee = (id, employee) => {
  return axios.put(`${BASE_URL}/${id}`, employee)
}

// 删除员工
export const deleteEmployee = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}