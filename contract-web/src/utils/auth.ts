// 统一认证请求工具 — 每次调用时动态读取 token
export function authFetch(url: string, options: any = {}) {
  const token = localStorage.getItem('token') || ''
  const headers: Record<string, string> = { 'Content-Type': 'application/json', ...options.headers }
  if (token) headers['Authorization'] = 'Bearer ' + token
  return fetch(url, { ...options, headers })
}
