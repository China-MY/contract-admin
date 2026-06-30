<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible theme="dark" width="240">
      <div class="logo">
        <span v-if="!collapsed" class="logo-text">项目管理系统</span>
        <span v-else class="logo-text-short">HT</span>
      </div>
      <a-menu
        v-model:openKeys="openKeys"
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="inline"
        @click="onMenuClick"
      >
        <a-menu-item key="/dashboard">
          <DashboardOutlined /><span>首页</span>
        </a-menu-item>
        <a-menu-item key="/statistic">
          <BarChartOutlined /><span>统计</span>
        </a-menu-item>
        <a-menu-item key="/project/list">
          <ProjectOutlined /><span>项目</span>
        </a-menu-item>
        <a-sub-menu key="contract">
          <template #title><FileTextOutlined /><span>合同</span></template>
          <a-menu-item key="/contract/receivable">应收合同</a-menu-item>
          <a-menu-item key="/contract/payable">应付合同</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="invoice">
          <template #title><FileProtectOutlined /><span>发票</span></template>
          <a-menu-item key="/invoice/issue">销项发票</a-menu-item>
          <a-menu-item key="/invoice/receipt">进项发票</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="payment">
          <template #title><MoneyCollectOutlined /><span>资金</span></template>
          <a-menu-item key="/payment/plan/receipt">收款计划</a-menu-item>
          <a-menu-item key="/payment/plan/pay">付款计划</a-menu-item>
          <a-menu-item key="/payment/record/receipt">收款记录</a-menu-item>
          <a-menu-item key="/payment/record/pay">付款记录</a-menu-item>
        </a-sub-menu>
        <a-menu-item key="/customer/list">
          <TeamOutlined /><span>客户</span>
        </a-menu-item>
        <a-menu-item key="/supplier/list">
          <ShopOutlined /><span>供应商</span>
        </a-menu-item>
        <a-menu-item key="/reminder">
          <BellOutlined /><span>提醒中心</span>
        </a-menu-item>
        <a-sub-menu key="settings">
          <template #title><SettingOutlined /><span>系统设置</span></template>
          <a-menu-item key="/settings/company">公司管理</a-menu-item>
          <a-menu-item key="/settings/fund-account">资金账户</a-menu-item>
          <a-menu-item key="/settings/basic">基础设置</a-menu-item>
          <a-menu-item key="/settings/user">用户管理</a-menu-item>
          <a-menu-item key="/settings/role">角色管理</a-menu-item>
          <a-menu-item key="/settings/dept">部门管理</a-menu-item>
          <a-menu-item key="/settings/post">岗位管理</a-menu-item>
          <a-menu-item key="/settings/notification">通知配置</a-menu-item>
          <a-menu-item key="/settings/reminder-config">提醒配置</a-menu-item>
          <a-menu-item key="/settings/login-log">登录日志</a-menu-item>
          <a-menu-item key="/settings/operation-log">操作日志</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="site-header">
        <div class="header-left">
          <span style="color:#999;font-size:13px;margin-right:12px">{{ userStore.currentCompany?.companyName || '' }}</span>
        </div>
        <div class="header-right">
          <a-dropdown>
            <a class="user-info" @click.prevent>
              <a-avatar size="small" style="backgroundColor: #1890ff">{{ userInitials }}</a-avatar>
              <span class="user-name">{{ userStore.userInfo?.realName || '用户' }}</span>
            </a>
            <template #overlay>
              <a-menu>
                <a-menu-item key="logout" @click="handleLogout">
                  <span>退出登录</span>
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>
      <a-layout-content class="site-content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import {
  DashboardOutlined, BarChartOutlined, ProjectOutlined,
  FileTextOutlined, FileProtectOutlined, MoneyCollectOutlined,
  TeamOutlined, ShopOutlined, BellOutlined, SettingOutlined,
} from '@ant-design/icons-vue'
// Breadcrumb removed - not needed for now

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const collapsed = ref(false)
const openKeys = ref<string[]>([])
const selectedKeys = ref<string[]>([])

const userInitials = computed(() => {
  const name = userStore.userInfo?.realName || 'U'
  return name.charAt(0)
})


function onMenuClick(info: { key: string }) {
  router.push(info.key)
}

function handleLogout() {
  userStore.logout()
}

// Sync selected keys with route
selectedKeys.value = [route.path]
</script>

<style scoped>
.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}
.logo-text-short { font-size: 22px; }
.site-header {
  background: #fff;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  height: 64px;
  line-height: 64px;
}
.header-left { display: flex; align-items: center; }
.header-right { display: flex; align-items: center; }
.user-info { cursor: pointer; display: flex; align-items: center; gap: 8px; }
.user-name { margin-left: 4px; }
.site-content {
  margin: 16px;
  min-height: calc(100vh - 64px - 32px);
  background: #f0f2f5;
}
</style>
