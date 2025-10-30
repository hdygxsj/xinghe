import apiClient from './user'

const BASE_URL = '/api/roles'

// 创建角色
export const createRole = (roleData) => {
  return apiClient.post(BASE_URL, roleData)
}

// 获取所有角色
export const getAllRoles = () => {
  return apiClient.get(BASE_URL)
}

// 根据ID获取角色
export const getRoleById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新角色
export const updateRole = (id, roleData) => {
  return apiClient.put(`${BASE_URL}/${id}`, roleData)
}

// 删除角色
export const deleteRole = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}
