import request from './index'

export function getDashboardSummary() {
  return request.get('/dashboard/summary')
}

export function getRecentContracts() {
  return request.get('/dashboard/recent-contracts')
}

export function getRecentTransactions() {
  return request.get('/dashboard/recent-transactions')
}

export function getOverview() {
  return request.get('/statistics/overview')
}

export function getChartData(params?: { year?: number; months?: number }) {
  return request.get('/statistics/chart', { params })
}

export function getProjectStats() {
  return request.get('/statistics/projects')
}

export function getReminders() {
  return request.get('/reminders')
}
