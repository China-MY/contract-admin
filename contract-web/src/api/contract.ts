import request from './index'

export function getContracts(params: {
  direction: string
  page?: number
  size?: number
  keyword?: string
  project?: string
  customer?: string
  receiptStatus?: string
  paymentStatus?: string
}) {
  return request.get('/contracts', { params })
}

export function createContract(data: any) {
  return request.post('/contracts', data)
}

export function updateContract(id: number, data: any) {
  return request.put(`/contracts/${id}`, data)
}

export function getContractSummary(direction: string) {
  return request.get('/contracts/summary', { params: { direction } })
}
