import axios from 'axios'

const BASE_URL = '/api/career-info'

// 根据员工ID获取里程碑
export const getMilestonesByEmployeeId = (employeeId) => {
  return axios.get(`${BASE_URL}/milestones/${employeeId}`)
}

// 根据员工ID获取证书
export const getCertificatesByEmployeeId = (employeeId) => {
  return axios.get(`${BASE_URL}/certificates/${employeeId}`)
}

// 生成年度考核证明
export const generateAnnualAssessmentCertificate = (employeeId, year) => {
  return axios.post(`${BASE_URL}/certificate/annual-assessment`, null, {
    params: { employeeId, year }
  })
}

// 生成在职联系人证明
export const generateEmploymentContactCertificate = (employeeId) => {
  return axios.post(`${BASE_URL}/certificate/employment-contact`, null, {
    params: { employeeId }
  })
}

// 生成荣誉证明
export const generateHonorCertificate = (employeeId, honorId) => {
  return axios.post(`${BASE_URL}/certificate/honor`, null, {
    params: { employeeId, honorId }
  })
}