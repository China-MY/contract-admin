<template>
  <div>
    <a-card>
      <template #title><span style="font-size:18px;font-weight:bold">经营统计</span></template>
      <a-tabs v-model:activeKey="activeTab">
        <!-- Tab 1: 经营明细 -->
        <a-tab-pane key="overview" tab="经营明细">
          <a-button style="margin-bottom:16px" @click="refreshAll"><SyncOutlined /> 刷新数据</a-button>
          <a-row :gutter="16">
            <a-col :span="6" v-for="s in summary" :key="s.label">
              <a-card size="small"><a-statistic :title="s.label" :value="s.value" :suffix="s.suffix" :value-style="{fontSize:'20px',color:s.color}" /></a-card>
            </a-col>
          </a-row>
          <a-row :gutter="16" style="margin-top:16px">
            <a-col :span="12"><a-card title="应收（销项）"><a-table :dataSource="receivableStats" :columns="statCols" :pagination="false" size="small" bordered /></a-card></a-col>
            <a-col :span="12"><a-card title="应付（进项）"><a-table :dataSource="payableStats" :columns="statCols" :pagination="false" size="small" bordered /></a-card></a-col>
          </a-row>
        </a-tab-pane>

        <!-- Tab 2: 图表分析 -->
        <a-tab-pane key="chart" tab="图表分析">
          <a-button style="margin-bottom:16px" @click="refreshAll"><SyncOutlined /> 刷新数据</a-button>
          <a-row :gutter="16">
            <a-col :span="6" v-for="s in chartCards" :key="s.label">
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
        </a-tab-pane>

        <!-- Tab 3: 项目统计 -->
        <a-tab-pane key="projects" tab="项目统计">
          <a-button style="margin-bottom:16px" @click="refreshAll"><SyncOutlined /> 刷新数据</a-button>
          <a-table :dataSource="projectList" :columns="projectCols" :loading="projectLoading" :pagination="projectPagination" rowKey="projectNo" size="small" bordered @change="onProjectChange" :scroll="{x:3000}">
            <template #bodyCell="{column,record}">
              <template v-if="column.key?.includes('Amount')||column.key?.includes('Profit')">
                ¥{{ (record[column.key as string]||0).toLocaleString() }}
              </template>
              <template v-else-if="column.key==='profitMargin'">
                <span :style="{color:(record.profitMargin||0)>=0?'#52c41a':'#ff4d4f'}">{{record.profitMargin||0}}%</span>
              </template>
              <template v-else-if="column.key==='revenueRate'||column.key==='expenseRate'">
                {{record[column.key as string]||0}}%
              </template>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { SyncOutlined } from '@ant-design/icons-vue'
import { authFetch } from '../../utils/auth'
import * as echarts from 'echarts'

const activeTab = ref('overview')

// === 经营明细 ===
const summary = ref<any[]>([])
const receivableStats = ref<any[]>([])
const payableStats = ref<any[]>([])
const statCols = [{title:'指标',dataIndex:'indicator',width:120},{title:'金额',dataIndex:'amount',width:160},{title:'比例',dataIndex:'rate',width:80}]

// === 图表分析 ===
const chartCards = ref<any[]>([])
let chartData: any = null  // 缓存图表数据，Tab切换时重绘
const trendRef=ref<HTMLElement>();const receivablePieRef=ref<HTMLElement>();const payablePieRef=ref<HTMLElement>()
const incomeTrendRef=ref<HTMLElement>();const receiptPieRef=ref<HTMLElement>();const paymentPieRef=ref<HTMLElement>()
const monthLabels = ['1月','2月','3月','4月','5月','6月']
const yearLabels = ['2021','2022','2023','2024','2025','2026']

// === 项目统计 ===
const projectList=ref<any[]>([]);const projectLoading=ref(false)
const projectPagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const projectCols=[
  {title:'项目名称',dataIndex:'projectName',width:180,fixed:'left' as const},
  {title:'项目编号',dataIndex:'projectNo',width:120},{title:'项目预算',key:'budgetAmount',width:110},
  {title:'应收合同金额',key:'receivableAmount',width:120},{title:'累计结算金额',key:'settlementAmount',width:120},
  {title:'已收款',key:'receivedAmount',width:100},{title:'已开票',key:'invoicedAmount',width:100},
  {title:'未收款',key:'unreceivedAmount',width:100},{title:'未开票',key:'uninvoicedAmount',width:100},
  {title:'收款率',key:'revenueRate',width:80},{title:'开票率',key:'invoiceRate',width:80},
  {title:'应付合同金额',key:'payableAmount',width:120},{title:'已付款',key:'paidAmount',width:100},
  {title:'已收票',key:'receivedInvoice',width:100},{title:'未付款',key:'unpaidAmount',width:100},
  {title:'付款率',key:'expenseRate',width:80},{title:'利润',key:'profit',width:100},
  {title:'利润率',key:'profitMargin',width:80},{title:'已支付利润',key:'paidProfit',width:100},
]

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

async function loadOverview() {
  const res = await authFetch('/api/statistics/overview'); const d = await res.json()
  if (d.code === 200) { summary.value = d.data.summary; receivableStats.value = d.data.receivable; payableStats.value = d.data.payable }
}

async function loadChart() {
  const res = await authFetch('/api/statistics/chart'); const d = await res.json()
  if (d.code === 200 && d.data) {
    chartData = d.data
    if (d.data.summaryCards) chartCards.value = d.data.summaryCards
    if (activeTab.value === 'chart') {
      await nextTick(); renderCharts(d.data)
    }
  }
}

async function loadProjects() {
  projectLoading.value = true
  const p = new URLSearchParams({page:String(projectPagination.current),size:String(projectPagination.pageSize)})
  const res = await authFetch(`/api/statistics/projects?${p}`); const d = await res.json()
  if (d.code === 200) { projectList.value = d.data.records; projectPagination.total = d.data.total }
  projectLoading.value = false
}

function onProjectChange(pag: any) { projectPagination.current = pag.current; projectPagination.pageSize = pag.pageSize; loadProjects() }

function refreshAll() {
  loadOverview()
  loadChart()
  loadProjects()
}

onMounted(() => {
  loadOverview()
  loadChart()
  loadProjects()
})

// 切换到图表 Tab 时重绘 ECharts
watch(activeTab, async (tab) => {
  if (tab === 'chart' && chartData) {
    // 先销毁旧实例避免重复
    [trendRef, receivablePieRef, payablePieRef, incomeTrendRef, receiptPieRef, paymentPieRef].forEach(ref => {
      if (ref.value) {
        const instance = echarts.getInstanceByDom(ref.value)
        if (instance) instance.dispose()
      }
    })
    await nextTick()
    renderCharts(chartData)
  }
})
</script>
