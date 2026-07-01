// 统一认证请求工具 — 每次调用时动态读取 token，自动处理 401 跳转登录
export async function authFetch(url: string, options: any = {}) {
  const token = localStorage.getItem('token') || ''
  const headers: Record<string, string> = { 'Content-Type': 'application/json', ...options.headers }
  if (token) headers['Authorization'] = 'Bearer ' + token
  const res = await fetch(url, { ...options, headers })
  if (res.status === 401) {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('currentCompany')
    window.location.hash = '#/login'
    throw new Error('未认证或token已过期')
  }
  return res
}
