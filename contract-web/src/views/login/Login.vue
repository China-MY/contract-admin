<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>项目管理系统</h1>
        <p>企业合同全生命周期管理平台</p>
      </div>
      <a-form :model="form" @finish="handleLogin" layout="vertical">
        <a-form-item label="登录公司" name="company" :rules="[{ required: true, message: '请选择公司' }]">
          <a-select v-model:value="form.company" placeholder="请选择公司" size="large" :loading="companiesLoading">
            <a-select-option v-for="c in companies" :key="c.id" :value="c.id">
              {{ c.companyName }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item name="username" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input v-model:value="form.username" size="large" placeholder="请输入账号">
            <template #prefix><UserOutlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item name="password" :rules="[{ required: true, message: '请输入密码' }]">
          <a-input-password v-model:value="form.password" size="large" placeholder="请输入密码">
            <template #prefix><LockOutlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" size="large" block :loading="loading">登 录</a-button>
        </a-form-item>
      </a-form>
      <div class="login-footer">
        <p>© 2026 企业软件解决方案</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { useUserStore } from '../../stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const companies = ref<any[]>([])
const companiesLoading = ref(false)

const form = reactive({ company: null, username: 'admin', password: '123456' })

onMounted(async () => {
  companiesLoading.value = true
  try {
    const res = await fetch('/api/settings/companies')
    const d = await res.json()
    if (d.code === 200) {
      companies.value = d.data || []
      // 默认选中默认公司
      const def = d.data.find((c: any) => c.isDefault)
      if (def) form.company = def.id
    }
  } catch {}
  companiesLoading.value = false
})

async function handleLogin() {
  loading.value = true
  try {
    const res = await fetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form)
    })
    const data = await res.json()
    if (data.code === 200) {
      userStore.setToken(data.data.token)
      userStore.setUserInfo(data.data.user)
      // 保存登录的公司信息
      const company = companies.value.find((c: any) => c.id === form.company)
      if (company) userStore.setCurrentCompany(company)
      message.success('登录成功')
      router.push('/dashboard')
    } else {
      message.error(data.msg || '登录失败')
    }
  } catch {
    message.error('登录失败，请检查网络')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 440px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0,0,0,0.15);
}
.login-header { text-align: center; margin-bottom: 32px; }
.login-header h1 { margin: 0; font-size: 28px; color: #1a1a2e; }
.login-header p { margin: 8px 0 0; color: #666; font-size: 14px; }
.login-footer { text-align: center; margin-top: 16px; }
.login-footer p { color: #999; font-size: 12px; }
</style>
