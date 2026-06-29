<template>
  <div class="contract-page">
    <a-card>
      <template #title>
        <span style="font-size:18px;font-weight:bold">应收合同</span>
      </template>
      <!-- 搜索栏 -->
      <a-form layout="inline" :model="searchForm" style="margin-bottom: 16px">
        <a-form-item label="关键字">
          <a-input v-model:value="searchForm.keyword" placeholder="合同编号/名称" allowClear />
        </a-form-item>
        <a-form-item label="项目">
          <a-select v-model:value="searchForm.project" placeholder="请选择" allowClear style="width:160px">
            <a-select-option v-for="p in projects" :key="p" :value="p">{{ p }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="客户">
          <a-select v-model:value="searchForm.customer" placeholder="请选择" allowClear style="width:160px">
            <a-select-option v-for="c in customers" :key="c" :value="c">{{ c }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="收款状态">
          <a-select v-model:value="searchForm.receiptStatus" placeholder="请选择" allowClear style="width:140px">
            <a-select-option value="unreceived">未收款</a-select-option>
            <a-select-option value="partial">部分收款</a-select-option>
            <a-select-option value="completed">已完成</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="loadData">查询</a-button>
          <a-button style="margin-left:8px" @click="resetSearch">重置</a-button>
        </a-form-item>
      </a-form>

      <!-- 操作栏 -->
      <div style="margin-bottom:12px;display:flex;justify-content:space-between">
        <div>
          <a-button type="primary" @click="showAdd">新建应收合同</a-button>
          <a-button style="margin-left:8px" @click="handleImport">导入</a-button>
        </div>
        <a-tag color="blue">合同金额合计：¥{{ totalAmount.toLocaleString() }}</a-tag>
      </div>

      <!-- 表格 -->
      <a-table
        :dataSource="dataList"
        :columns="columns"
        :loading="loading"
        :pagination="pagination"
        rowKey="id"
        size="small"
        bordered
        :scroll="{ x: 3200 }"
        @change="onTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'contractAmount'">
            ¥{{ record.contractAmount?.toLocaleString() || 0 }}
          </template>
          <template v-else-if="column.key === 'settlementAmount'">
            ¥{{ record.settlementAmount?.toLocaleString() || 0 }}
          </template>
          <template v-else-if="column.key === 'receivedAmount'">
            ¥{{ record.receivedAmount?.toLocaleString() || 0 }}
          </template>
          <template v-else-if="column.key === 'invoicedAmount'">
            ¥{{ record.invoicedAmount?.toLocaleString() || 0 }}
          </template>
          <template v-else-if="column.key === 'unreceivedAmount'">
            <span v-if="(record.unreceivedAmount || 0) > 0" style="color:#ff4d4f">¥{{ record.unreceivedAmount.toLocaleString() }}</span>
            <span v-else>¥0</span>
          </template>
          <template v-else-if="column.key === 'receiptStatus'">
            <a-tag :color="record.receiptStatus === 'completed' ? 'green' : record.receiptStatus === 'partial' ? 'orange' : 'red'">
              {{ record.receiptStatus === 'completed' ? '已完成' : record.receiptStatus === 'partial' ? '部分收款' : '未收款' }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'receiptProgress'">
            <a-progress :percent="record.receiptProgress || 0" size="small" />
          </template>
          <template v-else-if="column.key === 'invoiceProgress'">
            <a-progress :percent="record.invoiceProgress || 0" size="small" />
          </template>
          <template v-else-if="column.key === 'profitMargin'">
            <span :style="{ color: (record.profitMargin || 0) >= 0 ? '#52c41a' : '#ff4d4f' }">
              {{ record.profitMargin || 0 }}%
            </span>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 新建/编辑弹窗 -->
    <a-modal v-model:open="modalVisible" :title="modalTitle" width="80%" :footer="null" destroyOnClose>
      <ContractForm v-if="modalVisible" :record="currentRecord" @saved="onSaved" @cancel="modalVisible=false" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'
import ContractForm from './ContractForm.vue'

const searchForm = reactive({ keyword: '', project: '', customer: '', receiptStatus: '' })
const dataList = ref<any[]>([])
const loading = ref(false)
const totalAmount = ref(0)
const modalVisible = ref(false)
const currentRecord = ref<any>(null)
const modalTitle = ref('')
const projects = ref<string[]>([])
const customers = ref<string[]>([])

const pagination = reactive({ current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: (t: number) => `共 ${t} 条` })

const columns = [
  { title: '合同编号', dataIndex: 'contractNo', key: 'contractNo', width: 120, fixed: 'left' as const },
  { title: '合同名称', dataIndex: 'contractName', key: 'contractName', width: 200 },
  { title: '合同类型', dataIndex: 'contractType', key: 'contractType', width: 100 },
  { title: '我方公司', dataIndex: 'ourCompany', key: 'ourCompany', width: 140 },
  { title: '对方单位', dataIndex: 'counterparty', key: 'counterparty', width: 140 },
  { title: '项目编号', dataIndex: 'projectNo', key: 'projectNo', width: 120 },
  { title: '项目名称', dataIndex: 'projectName', key: 'projectName', width: 160 },
  { title: '合同地址', dataIndex: 'contractAddress', key: 'contractAddress', width: 160 },
  { title: '合同负责人', dataIndex: 'manager', key: 'manager', width: 100 },
  { title: '合同状态', dataIndex: 'status', key: 'status', width: 90 },
  { title: '签订日期', dataIndex: 'signDate', key: 'signDate', width: 100 },
  { title: '结束日期', dataIndex: 'endDate', key: 'endDate', width: 100 },
  { title: '计价方式', dataIndex: 'pricingMethod', key: 'pricingMethod', width: 90 },
  { title: '合同金额', key: 'contractAmount', width: 120 },
  { title: '结算金额', key: 'settlementAmount', width: 120 },
  { title: '已收款', key: 'receivedAmount', width: 110 },
  { title: '已开票', key: 'invoicedAmount', width: 110 },
  { title: '未收款', key: 'unreceivedAmount', width: 110 },
  { title: '未开票', dataIndex: 'uninvoicedAmount', key: 'uninvoicedAmount', width: 110 },
  { title: '收款未开票', dataIndex: 'receivedNotInvoiced', key: 'receivedNotInvoiced', width: 110 },
  { title: '开票未收款', dataIndex: 'invoicedNotReceived', key: 'invoicedNotReceived', width: 110 },
  { title: '收款状态', key: 'receiptStatus', width: 100 },
  { title: '收款进度', key: 'receiptProgress', width: 100 },
  { title: '开票进度', key: 'invoiceProgress', width: 100 },
  { title: '采购合同金额', dataIndex: 'procurementContractAmount', key: 'procurementContractAmount', width: 120 },
  { title: '采购结算金额', dataIndex: 'procurementSettlementAmount', key: 'procurementSettlementAmount', width: 120 },
  { title: '采购已付款', dataIndex: 'procurementPaidAmount', key: 'procurementPaidAmount', width: 110 },
  { title: '采购已收票', dataIndex: 'procurementReceivedInvoice', key: 'procurementReceivedInvoice', width: 110 },
  { title: '利润', dataIndex: 'profit', key: 'profit', width: 100 },
  { title: '利润率', key: 'profitMargin', width: 80 },
  { title: '已支付利润', dataIndex: 'paidProfit', key: 'paidProfit', width: 110 },
  { title: '附件', dataIndex: 'attachment', key: 'attachment', width: 80 },
  { title: '备注', dataIndex: 'remark', key: 'remark', width: 150 },
  { title: '操作', key: 'action', width: 80, fixed: 'right' as const },
]

onMounted(() => { loadData(); loadOptions() })

async function loadData() {
  loading.value = true
  const params = new URLSearchParams({
    page: String(pagination.current), size: String(pagination.pageSize),
    ...searchForm
  })
  const res = await authFetch(`/api/contracts?direction=receivable&${params}`)
  const data = await res.json()
  if (data.code === 200) {
    dataList.value = data.data.records
    pagination.total = data.data.total
    totalAmount.value = data.data.totalAmount || 0
  }
  loading.value = false
}

async function loadOptions() {
  try {
    const res = await authFetch('/api/options')
    const data = await res.json()
    if (data.code === 200) {
      projects.value = (data.data.projects || []).map((p:any) => p.label || p.value || p)
      customers.value = (data.data.customers || []).map((c:any) => c.label || c.value || c)
    }
  } catch {}
}

function resetSearch() {
  Object.assign(searchForm, { keyword: '', project: '', customer: '', receiptStatus: '' })
  pagination.current = 1
  loadData()
}

function onTableChange(pag: any) {
  pagination.current = pag.current
  pagination.pageSize = pag.pageSize
  loadData()
}

function showAdd() {
  currentRecord.value = null
  modalTitle.value = '新建应收合同'
  modalVisible.value = true
}

function editRecord(record: any) {
  currentRecord.value = { ...record }
  modalTitle.value = '编辑应收合同'
  modalVisible.value = true
}

function onSaved() {
  modalVisible.value = false
  loadData()
}

function handleImport() {
  message.info('导入功能开发中，即将支持 Excel 批量导入')
}
</script>
