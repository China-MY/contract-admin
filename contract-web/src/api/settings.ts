import request from './index'

// Dict
export function getDict(type: string) {
  return request.get('/settings/dict', { params: { type } })
}

export function createDict(data: any) {
  return request.post('/settings/dict', data)
}

export function updateDict(id: number, data: any) {
  return request.put(`/settings/dict/${id}`, data)
}

export function deleteDict(id: number) {
  return request.delete(`/settings/dict/${id}`)
}

// Companies
export function getCompanies() {
  return request.get('/settings/companies')
}

export function createCompany(data: any) {
  return request.post('/settings/companies', data)
}

export function updateCompany(id: number, data: any) {
  return request.put(`/settings/companies/${id}`, data)
}

export function setDefaultCompany(id: number) {
  return request.put(`/settings/companies/${id}/default`)
}

export function deleteCompany(id: number) {
  return request.delete(`/settings/companies/${id}`)
}

// Fund Accounts
export function getFundAccounts() {
  return request.get('/settings/fund-accounts')
}

export function createFundAccount(data: any) {
  return request.post('/settings/fund-accounts', data)
}

export function updateFundAccount(id: number, data: any) {
  return request.put(`/settings/fund-accounts/${id}`, data)
}

export function deleteFundAccount(id: number) {
  return request.delete(`/settings/fund-accounts/${id}`)
}

// Users
export function getUsers() {
  return request.get('/users')
}

export function createUser(data: any) {
  return request.post('/users', data)
}

export function updateUser(id: number, data: any) {
  return request.put(`/users/${id}`, data)
}

export function deleteUser(id: number) {
  return request.delete(`/users/${id}`)
}

export function resetUserPassword(id: number, password?: string) {
  return request.put(`/users/${id}/password`, { password: password || '123456' })
}

// Roles
export function getRoles() {
  return request.get('/roles')
}

export function createRole(data: any) {
  return request.post('/roles', data)
}

export function updateRole(id: number, data: any) {
  return request.put(`/roles/${id}`, data)
}

export function deleteRole(id: number) {
  return request.delete(`/roles/${id}`)
}

export function assignRoleMenus(id: number, menus: string[]) {
  return request.put(`/roles/${id}/menus`, { menus })
}

// Depts
export function getDepts() {
  return request.get('/depts')
}

export function createDept(data: any) {
  return request.post('/depts', data)
}

export function updateDept(id: number, data: any) {
  return request.put(`/depts/${id}`, data)
}

export function deleteDept(id: number) {
  return request.delete(`/depts/${id}`)
}

// Posts
export function getPosts() {
  return request.get('/posts')
}

export function createPost(data: any) {
  return request.post('/posts', data)
}

export function updatePost(id: number, data: any) {
  return request.put(`/posts/${id}`, data)
}

export function deletePost(id: number) {
  return request.delete(`/posts/${id}`)
}

// Logs
export function getLoginLogs() {
  return request.get('/logs/login')
}

export function getOperationLogs() {
  return request.get('/logs/operation')
}
