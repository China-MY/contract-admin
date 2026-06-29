import request from './index'

export function getInvoices(params: { direction: string; page?: number; size?: number }) {
  return request.get('/invoices', { params })
}

export function createInvoice(data: any) {
  return request.post('/invoices', data)
}

export function updateInvoice(id: number, data: any) {
  return request.put(`/invoices/${id}`, data)
}

export function deleteInvoice(id: number) {
  return request.delete(`/invoices/${id}`)
}

export function getInvoiceSummary(direction: string) {
  return request.get('/invoices/summary', { params: { direction } })
}
