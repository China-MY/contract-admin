import request from './index'

export function getProjects(params?: { page?: number; size?: number }) {
  return request.get('/projects', { params })
}

export function createProject(data: any) {
  return request.post('/projects', data)
}

export function updateProject(id: number, data: any) {
  return request.put(`/projects/${id}`, data)
}

export function deleteProject(id: number) {
  return request.delete(`/projects/${id}`)
}
