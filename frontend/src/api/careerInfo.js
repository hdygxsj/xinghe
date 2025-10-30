import apiClient from './user'

const BASE_URL = '/api/career-info'

// 根据员工ID获取里程碑
export const getMilestonesByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/milestones/${employeeId}`)
}

// 根据员工ID获取证书
export const getCertificatesByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/certificates/${employeeId}`)
}

// 生成年度考核证明
export const generateAnnualAssessmentCertificate = (employeeId, year, grade = 'C') => {
  return apiClient.post(`${BASE_URL}/certificate/annual-assessment`, null, {
    params: { employeeId, year, grade }
  })
}

// 生成在职联系人证明
export const generateEmploymentContactCertificate = (employeeId) => {
  return apiClient.post(`${BASE_URL}/certificate/employment-contact`, null, {
    params: { employeeId }
  })
}

// 生成荣誉证明
export const generateHonorCertificate = (employeeId, honorId) => {
  return apiClient.post(`${BASE_URL}/certificate/honor`, null, {
    params: { employeeId, honorId }
  })
}

// 获取统计信息
export const getStatistics = () => {
  return apiClient.get(`${BASE_URL}/statistics`)
}

// 根据员工ID获取统计信息
export const getStatisticsByEmployeeId = (employeeId) => {
  return apiClient.get(`${BASE_URL}/statistics/${employeeId}`)
}