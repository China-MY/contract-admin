import request from './index'

export function login(data: { username: string; password: string }) {
  return request.post('/auth/login', data)
}

export function logout() {
  return request.post('/auth/logout')
}

export function getUserInfo() {
  return request.get('/auth/user-info')
}

export function changePassword(data: { username: string; oldPassword: string; newPassword: string }) {
  return request.put('/auth/password', data)
}
