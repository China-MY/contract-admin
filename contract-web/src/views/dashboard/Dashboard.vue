<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <a-row :gutter="[16, 16]">
      <a-col :xs="24" :sm="12" :lg="6" v-for="card in statCards" :key="card.title">
        <a-card hoverable>
          <a-statistic :title="card.title" :value="card.value" :suffix="card.suffix" :value-style="{ color: card.color }" />
          <div class="stat-sub">{{ card.sub }}</div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="[16, 16]" style="margin-top: 16px">
      <!-- 合同趋势 -->
      <a-col :span="16">
        <a-card title="合同金额趋势">
          <div ref="trendChartRef" style="height: 300px"></div>
        </a-card>
      </a-col>
      <!-- 合同状态分布 -->
      <a-col :span="8">
        <a-card title="合同状态分布">
          <div ref="pieChartRef" style="height: 300px"></div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="[16, 16]" style="margin-top: 16px">
      <!-- 最近提醒 -->
      <a-col :span="12">
        <a-card title="最近提醒">
          <a-list :data-source="recentReminders" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>
                    <span :style="{ color: item.overdue ? '#ff4d4f' : '#faad14' }">{{ item.title }}</span>
                  </template>
                  <template #description>{{ item.desc }} — {{ item.overdue ? `过期 ${item.days} 天` : `剩余 ${item.days} 天` }}</template>
                </a-list-item-meta>
                <template #extra><a-button size="small" type="link" @click="goToReminder">查看</a-button></template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
      <!-- 最近交易 -->
      <a-col :span="12">
        <a-card title="最近交易">
          <a-list :data-source="recentTransactions" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>
                    <a-tag :color="item.type === 'receipt' ? 'green' : 'red'">{{ item.type === 'receipt' ? '收款' : '付款' }}</a-tag>
                    ¥{{ item.amount.toLocaleString() }}
                    <span style="color:#999;font-size:12px;margin-left:8px">{{ item.party }}</span>
                  </template>
                  <template #description>{{ item.date }} · {{ item.status === 'confirmed' ? '已收款' : '待确认' }}</template>
                </a-list-item-meta>
                <template #extra><a-button size="small" type="link" @click="goToTransaction(item)">查看</a-button></template>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { authFetch } from '../../utils/auth'
import * as echarts from 'echarts'

const router = useRouter()

const trendChartRef = ref<HTMLElement>()
const pieChartRef = ref<HTMLElement>()

onMounted(async () => {
  try {
    const res = await authFetch('/api/dashboard/summary')
    const data = await res.json()
    if (data.code === 200 && data.data) {
      if (data.data.cards) statCards.value = data.data.cards
      if (data.data.reminders) recentReminders.value = data.data.reminders
      if (data.data.transactions) recentTransactions.value = data.data.transactions
      // 等待 DOM 渲染完成后初始化 ECharts
      await nextTick()
      renderChart(data.data)
    }
  } catch (e) {
    console.error('Dashboard load failed:', e)
  }
})

function renderChart(data: any) {
  setTimeout(() => {
  if (trendChartRef.value) {
    const chart = echarts.init(trendChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: data.trendMonths || [] },
      yAxis: { type: 'value', axisLabel: { formatter: '¥{value}' } },
      series: [{
        data: data.trendValues || [],
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.3 },
        lineStyle: { width: 3 },
        itemStyle: { color: '#1890ff' }
      }]
    })
  }
  if (pieChartRef.value) {
    const chart = echarts.init(pieChartRef.value)
    chart.setOption({
      tooltip: { trigger: 'item' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        data: data.statusDistribution || [],
        label: { formatter: '{b}: {c}' }
      }]
    })
  }
  }, 100)
}

function goToReminder() {
  router.push('/reminder')
}

function goToTransaction(item: any) {
  if (item.type === 'receipt') {
    router.push('/payment/record/receipt')
  } else {
    router.push('/payment/record/pay')
  }
}

const statCards = ref([
  { title: '合同总数', value: 38, suffix: '份', sub: '本月新增 4 份', color: '#1890ff' },
  { title: '已完成合同', value: 5, suffix: '份', sub: '完成率 13.16%', color: '#52c41a' },
  { title: '已收款', value: '¥929,863', suffix: '', sub: '本月收款 ¥0', color: '#1890ff' },
  { title: '已付款', value: '¥957,100', suffix: '', sub: '本月付款 ¥0', color: '#ff4d4f' },
])

const recentReminders = ref<any[]>([])
const recentTransactions = ref<any[]>([])
</script>
