import request from './index'

// Payment Plans
export function getPaymentPlans(params: { direction: string; page?: number; size?: number }) {
  return request.get('/payment-plans', { params })
}

export function createPaymentPlan(data: any) {
  return request.post('/payment-plans', data)
}

export function updatePaymentPlan(id: number, data: any) {
  return request.put(`/payment-plans/${id}`, data)
}

export function deletePaymentPlan(id: number) {
  return request.delete(`/payment-plans/${id}`)
}

// Payment Records
export function getPaymentRecords(params: { direction: string; page?: number; size?: number }) {
  return request.get('/payment-records', { params })
}

export function createPaymentRecord(data: any) {
  return request.post('/payment-records', data)
}

export function updatePaymentRecord(id: number, data: any) {
  return request.put(`/payment-records/${id}`, data)
}

export function deletePaymentRecord(id: number) {
  return request.delete(`/payment-records/${id}`)
}

export function confirmPaymentRecord(id: number) {
  return request.post('/payment-records/confirm', { id })
}
