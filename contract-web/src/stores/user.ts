import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import router from '../router'

interface UserInfo {
  id: number
  username: string
  realName: string
  avatar?: string
  roleNames: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)
  const currentCompany = ref<any>(null)

  const isLoggedIn = computed(() => !!token.value)

  function setToken(t: string) {
    token.value = t
    localStorage.setItem('token', t)
  }

  function setUserInfo(info: UserInfo) {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  function setCurrentCompany(company: any) {
    currentCompany.value = company
    localStorage.setItem('currentCompany', JSON.stringify(company))
  }

  function loadCurrentCompany() {
    const stored = localStorage.getItem('currentCompany')
    if (stored) {
      try { currentCompany.value = JSON.parse(stored) } catch {}
    }
  }

  function loadUserInfo() {
    const stored = localStorage.getItem('userInfo')
    if (stored) {
      try { userInfo.value = JSON.parse(stored) } catch { /* ignore */ }
    }
    loadCurrentCompany()
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    currentCompany.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('currentCompany')
    router.push('/login')
  }

  return { token, userInfo, currentCompany, isLoggedIn, setToken, setUserInfo, setCurrentCompany, loadUserInfo, logout }
})
