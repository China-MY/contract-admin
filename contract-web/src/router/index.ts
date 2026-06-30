import { createRouter, createWebHashHistory, type RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  { path: '/login', name: 'Login', component: () => import('../views/login/Login.vue'), meta: { noAuth: true } },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: 'guide', name: 'Guide', component: () => import('../views/guide/Guide.vue'), meta: { title: '新手指南' } },
      // 统计
      { path: 'statistic', name: 'Statistics', component: () => import('../views/statistic/Statistics.vue'), meta: { title: '经营统计' } },
      // 合同
      { path: 'contract/receivable', name: 'ContractReceivable', component: () => import('../views/contract/Receivable.vue'), meta: { title: '应收合同' } },
      { path: 'contract/payable', name: 'ContractPayable', component: () => import('../views/contract/Payable.vue'), meta: { title: '应付合同' } },
      // 发票
      { path: 'invoice/issue', name: 'InvoiceIssue', component: () => import('../views/invoice/Issue.vue'), meta: { title: '销项发票' } },
      { path: 'invoice/receipt', name: 'InvoiceReceipt', component: () => import('../views/invoice/Receipt.vue'), meta: { title: '进项发票' } },
      // 资金
      { path: 'payment/plan/receipt', name: 'PaymentPlanReceipt', component: () => import('../views/payment/PlanReceipt.vue'), meta: { title: '收款计划' } },
      { path: 'payment/plan/pay', name: 'PaymentPlanPay', component: () => import('../views/payment/PlanPay.vue'), meta: { title: '付款计划' } },
      { path: 'payment/record/receipt', name: 'PaymentRecordReceipt', component: () => import('../views/payment/RecordReceipt.vue'), meta: { title: '收款记录' } },
      { path: 'payment/record/pay', name: 'PaymentRecordPay', component: () => import('../views/payment/RecordPay.vue'), meta: { title: '付款记录' } },
      // 项目
      { path: 'project/list', name: 'ProjectList', component: () => import('../views/project/ProjectList.vue'), meta: { title: '项目列表' } },
      { path: 'project/detail/:id', name: 'ProjectDetail', component: () => import('../views/project/ProjectDetail.vue'), meta: { title: '项目详情' } },
      // 客户/供应商
      { path: 'customer/list', name: 'CustomerList', component: () => import('../views/customer/CustomerList.vue'), meta: { title: '客户' } },
      { path: 'supplier/list', name: 'SupplierList', component: () => import('../views/supplier/SupplierList.vue'), meta: { title: '供应商' } },
      // 提醒
      { path: 'reminder', name: 'Reminder', component: () => import('../views/reminder/Reminder.vue'), meta: { title: '提醒中心' } },
      // 设置
      { path: 'settings/company', name: 'SettingsCompany', component: () => import('../views/settings/Company.vue'), meta: { title: '公司管理' } },
      { path: 'settings/fund-account', name: 'SettingsFundAccount', component: () => import('../views/settings/FundAccount.vue'), meta: { title: '资金账户' } },
      { path: 'settings/basic', name: 'SettingsBasic', component: () => import('../views/settings/Basic.vue'), meta: { title: '基础设置' } },
      { path: 'settings/user', name: 'SettingsUser', component: () => import('../views/settings/User.vue'), meta: { title: '用户管理' } },
      { path: 'settings/role', name: 'SettingsRole', component: () => import('../views/settings/Role.vue'), meta: { title: '角色管理' } },
      { path: 'settings/dept', name: 'SettingsDept', component: () => import('../views/settings/Dept.vue'), meta: { title: '部门管理' } },
      { path: 'settings/post', name: 'SettingsPost', component: () => import('../views/settings/Post.vue'), meta: { title: '岗位管理' } },
      { path: 'settings/notification', name: 'SettingsNotification', component: () => import('../views/settings/NotificationConfig.vue'), meta: { title: '通知配置' } },
      { path: 'settings/reminder-config', name: 'SettingsReminderConfig', component: () => import('../views/settings/ReminderConfig.vue'), meta: { title: '提醒配置' } },
      { path: 'settings/login-log', name: 'SettingsLoginLog', component: () => import('../views/settings/LoginLog.vue'), meta: { title: '登录日志' } },
      { path: 'settings/operation-log', name: 'SettingsOperationLog', component: () => import('../views/settings/OperationLog.vue'), meta: { title: '操作日志' } },
    ]
  },
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('../views/error/NotFound.vue') }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (!to.meta.noAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router
