<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">提醒中心</span></template>
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="all" tab="全部">
        <a-list :dataSource="filteredReminders" :pagination="listPagination" v-if="filteredReminders.length > 0">
          <template #renderItem="{item}">
            <a-list-item>
              <a-list-item-meta>
                <template #title>
                  <a-tag :color="item.overdue ? 'red' : 'orange'">{{item.type}}</a-tag>
                  {{item.title}}
                </template>
                <template #description>
                  {{item.desc}} · <span :style="{color: item.overdue ? '#ff4d4f' : '#faad14', fontWeight:'bold'}">{{ item.overdue ? `已过期 ${item.days} 天` : `剩余 ${item.days} 天` }}</span>
                </template>
              </a-list-item-meta>
              <template #extra>
                <a-button size="small" type="primary" ghost @click="goToTarget(item)">查看</a-button>
              </template>
            </a-list-item>
          </template>
        </a-list>
        <a-empty v-else description="暂无提醒" />
      </a-tab-pane>
      <a-tab-pane key="contract" tab="合同到期">
        <a-list :dataSource="contractReminders" :pagination="listPagination" v-if="contractReminders.length > 0">
          <template #renderItem="{item}">
            <a-list-item>
              <a-list-item-meta>
                <template #title>
                  <a-tag :color="item.overdue ? 'red' : 'orange'">{{item.type}}</a-tag>
                  {{item.title}}
                </template>
                <template #description>
                  {{item.desc}} · <span :style="{color: item.overdue ? '#ff4d4f' : '#faad14', fontWeight:'bold'}">{{ item.overdue ? `已过期 ${item.days} 天` : `剩余 ${item.days} 天` }}</span>
                </template>
              </a-list-item-meta>
              <template #extra>
                <a-button size="small" type="primary" ghost @click="goToTarget(item)">查看</a-button>
              </template>
            </a-list-item>
          </template>
        </a-list>
        <a-empty v-else description="暂无合同到期提醒" />
      </a-tab-pane>
      <a-tab-pane key="plan" tab="计划到期">
        <a-list :dataSource="planReminders" :pagination="listPagination" v-if="planReminders.length > 0">
          <template #renderItem="{item}">
            <a-list-item>
              <a-list-item-meta>
                <template #title>
                  <a-tag :color="item.overdue ? 'red' : 'orange'">{{item.type}}</a-tag>
                  {{item.title}}
                </template>
                <template #description>
                  {{item.desc}} · <span :style="{color: item.overdue ? '#ff4d4f' : '#faad14', fontWeight:'bold'}">{{ item.overdue ? `已过期 ${item.days} 天` : `剩余 ${item.days} 天` }}</span>
                </template>
              </a-list-item-meta>
              <template #extra>
                <a-button size="small" type="primary" ghost @click="goToTarget(item)">查看</a-button>
              </template>
            </a-list-item>
          </template>
        </a-list>
        <a-empty v-else description="暂无计划到期提醒" />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { authFetch } from '../../utils/auth'
const router = useRouter()
const activeTab = ref('all')
const reminders = ref<any[]>([])
const listPagination = { pageSize: 10 }

const filteredReminders = computed(() => {
  if (activeTab.value === 'all') return reminders.value
  if (activeTab.value === 'contract') return contractReminders.value
  if (activeTab.value === 'plan') return planReminders.value
  return reminders.value
})

const contractReminders = computed(() =>
  reminders.value.filter(r => r.type && (r.type.includes('合同到期') || r.type.includes('contract')))
)

const planReminders = computed(() =>
  reminders.value.filter(r => r.type && (r.type.includes('计划到期') || r.type.includes('plan')))
)

function goToTarget(item: any) {
  const type = item.type || ''
  if (type.includes('应收') || type.includes('receivable')) {
    router.push('/contract/receivable')
  } else if (type.includes('应付') || type.includes('payable')) {
    router.push('/contract/payable')
  } else if (type.includes('收款') || type.includes('receipt')) {
    router.push('/payment/plan/receipt')
  } else if (type.includes('付款') || type.includes('payment')) {
    router.push('/payment/plan/pay')
  } else {
    router.push('/contract/receivable')
  }
}

onMounted(async () => {
  try {
    const res = await authFetch('/api/reminders')
    const d = await res.json()
    if (d.code === 200) reminders.value = d.data
  } catch {}
})
</script>
