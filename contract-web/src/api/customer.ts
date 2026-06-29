import request from './index'

export function getCustomers(params?: { page?: number; size?: number; keyword?: string }) {
  return request.get('/customers', { params })
}

export function createCustomer(data: any) {
  return request.post('/customers', data)
}

export function updateCustomer(id: number, data: any) {
  return request.put(`/customers/${id}`, data)
}

export function deleteCustomer(id: number) {
  return request.delete(`/customers/${id}`)
}

export function getSuppliers(params?: { page?: number; size?: number; keyword?: string }) {
  return request.get('/suppliers', { params })
}

export function createSupplier(data: any) {
  return request.post('/suppliers', data)
}

export function updateSupplier(id: number, data: any) {
  return request.put(`/suppliers/${id}`, data)
}

export function deleteSupplier(id: number) {
  return request.delete(`/suppliers/${id}`)
}
