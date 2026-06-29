<template>
  <div class="stat-page"><a-card><template #title><span style="font-size:18px;font-weight:bold">图表分析</span></template>
    <a-button style="margin-bottom:16px" @click="refresh"><SyncOutlined /> 刷新数据</a-button>
    <a-row :gutter="16">
      <a-col :span="6" v-for="s in summaryCards" :key="s.label">
        <a-card size="small"><a-statistic :title="s.label" :value="s.value" :suffix="s.suffix" :value-style="{fontSize:'18px',color:s.color}" /></a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="12"><a-card title="年度合同趋势"><div ref="trendRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="应收合同类型分布"><div ref="receivablePieRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="应付合同类型分布"><div ref="payablePieRef" style="height:280px"></div></a-card></a-col>
    </a-row>
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="12"><a-card title="月度收支趋势"><div ref="incomeTrendRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="收款状态分析"><div ref="receiptPieRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="付款状态分析"><div ref="paymentPieRef" style="height:280px"></div></a-card></a-col>
    </a-row>
  </a-card></div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { SyncOutlined } from '@ant-design/icons-vue'
import { authFetch } from '../../utils/auth'
import * as echarts from 'echarts'

const trendRef=ref<HTMLElement>();const receivablePieRef=ref<HTMLElement>();const payablePieRef=ref<HTMLElement>()
const incomeTrendRef=ref<HTMLElement>();const receiptPieRef=ref<HTMLElement>();const paymentPieRef=ref<HTMLElement>()
const summaryCards=ref<any[]>([])

const monthLabels = ['1月','2月','3月','4月','5月','6月']
const yearLabels = ['2021','2022','2023','2024','2025','2026']

function renderCharts(data: any) {
  if (!data) return
  setTimeout(() => {
    if (trendRef.value && trendRef.value.offsetHeight > 0) {
      echarts.init(trendRef.value).setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: data.trendLabels || yearLabels },
        yAxis: { type: 'value', axisLabel: { formatter: '¥{value}' } },
        series: [{ data: data.trend || [0,0,0,0,0,0], type: 'line', smooth: true, areaStyle: { opacity: 0.3 }, lineStyle: { width: 3 }, itemStyle: { color: '#1890ff' } }]
      })
    }
    if (receivablePieRef.value && receivablePieRef.value.offsetHeight > 0) {
      echarts.init(receivablePieRef.value).setOption({
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['30%', '60%'], data: data.receivableTypes || [], label: { formatter: '{b}: {c}' } }]
      })
    }
    if (payablePieRef.value && payablePieRef.value.offsetHeight > 0) {
      echarts.init(payablePieRef.value).setOption({
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['30%', '60%'], data: data.payableTypes || [], label: { formatter: '{b}: {c}' } }]
      })
    }
    if (incomeTrendRef.value && incomeTrendRef.value.offsetHeight > 0) {
      echarts.init(incomeTrendRef.value).setOption({
        tooltip: { trigger: 'axis' }, legend: { data: ['收入', '支出'] },
        xAxis: { type: 'category', data: monthLabels },
        yAxis: { type: 'value' },
        series: [
          { name: '收入', type: 'bar', data: data.monthlyIncome || [0,0,0,0,0,0] },
          { name: '支出', type: 'bar', data: data.monthlyExpense || [0,0,0,0,0,0] }
        ]
      })
    }
    if (receiptPieRef.value && receiptPieRef.value.offsetHeight > 0) {
      echarts.init(receiptPieRef.value).setOption({
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['30%', '60%'], data: data.receiptStatus || [], label: { formatter: '{b}: {c}' } }]
      })
    }
    if (paymentPieRef.value && paymentPieRef.value.offsetHeight > 0) {
      echarts.init(paymentPieRef.value).setOption({
        tooltip: { trigger: 'item' },
        series: [{ type: 'pie', radius: ['30%', '60%'], data: data.paymentStatus || [], label: { formatter: '{b}: {c}' } }]
      })
    }
  }, 200)
}

async function loadData() {
  const res = await authFetch('/api/statistics/chart')
  const d = await res.json()
  if (d.code === 200 && d.data) {
    if (d.data.summaryCards) summaryCards.value = d.data.summaryCards
    renderCharts(d.data)
  }
}
function refresh() { loadData() }
onMounted(loadData)
</script>
