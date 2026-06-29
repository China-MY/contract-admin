import axios from 'axios'
import { message } from 'ant-design-vue'

const request = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' }
})

// Request interceptor — attach JWT token
request.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor — handle common errors
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 401) {
      localStorage.removeItem('token')
      window.location.hash = '#/login'
      return Promise.reject(new Error(res.msg || '未认证'))
    }
    if (res.code && res.code !== 200) {
      message.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg))
    }
    return res
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      window.location.hash = '#/login'
    }
    message.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
