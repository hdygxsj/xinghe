import axios from 'axios'

const BASE_URL = '/api/certificates'

// 获取所有证书
export const getCertificates = () => {
  return axios.get(BASE_URL)
}

// 根据ID获取证书
export const getCertificateById = (id) => {
  return axios.get(`${BASE_URL}/${id}`)
}

// 根据员工ID获取证书
export const getCertificatesByEmployeeId = (employeeId) => {
  return axios.get(`${BASE_URL}/employee/${employeeId}`)
}

// 创建证书
export const createCertificate = (certificate) => {
  return axios.post(BASE_URL, certificate)
}

// 更新证书
export const updateCertificate = (id, certificate) => {
  return axios.put(`${BASE_URL}/${id}`, certificate)
}

// 删除证书
export const deleteCertificate = (id) => {
  return axios.delete(`${BASE_URL}/${id}`)
}