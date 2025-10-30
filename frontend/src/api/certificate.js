import apiClient from './user'

const BASE_URL = '/api/certificates'

// 创建证书
export const createCertificate = (certificateData) => {
  return apiClient.post(BASE_URL, certificateData)
}

// 获取所有证书
export const getAllCertificates = () => {
  return apiClient.get(BASE_URL)
}

// 根据员工ID获取证书
export const getCertificatesByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/employee/${employeeId}`)
}

// 根据ID获取证书
export const getCertificateById = (id) => {
  return apiClient.get(`${BASE_URL}/${id}`)
}

// 更新证书
export const updateCertificate = (id, certificateData) => {
  return apiClient.put(`${BASE_URL}/${id}`, certificateData)
}

// 删除证书
export const deleteCertificate = (id) => {
  return apiClient.delete(`${BASE_URL}/${id}`)
}

// 下载证书PDF
export const downloadCertificatePdf = (id) => {
  return apiClient.get(`${BASE_URL}/${id}/download`, { responseType: 'blob' })
}