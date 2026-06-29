<template>
  <div class="contract-page">
    <a-card>
      <template #title><span style="font-size:18px;font-weight:bold">应付合同</span></template>
      <a-form layout="inline" :model="searchForm" style="margin-bottom:16px">
        <a-form-item label="关键字"><a-input v-model:value="searchForm.keyword" placeholder="合同编号/名称" allowClear /></a-form-item>
        <a-form-item label="项目"><a-select v-model:value="searchForm.project" placeholder="请选择" allowClear style="width:160px">
          <a-select-option v-for="p in projectOptions" :key="p" :value="p">{{p}}</a-select-option>
        </a-select></a-form-item>
        <a-form-item label="供应商"><a-select v-model:value="searchForm.supplier" placeholder="请选择" allowClear style="width:160px">
          <a-select-option v-for="s in supplierOptions" :key="s" :value="s">{{s}}</a-select-option>
        </a-select></a-form-item>
        <a-form-item label="付款状态">
          <a-select v-model:value="searchForm.paymentStatus" placeholder="请选择" allowClear style="width:140px">
            <a-select-option value="unpaid">未付款</a-select-option><a-select-option value="partial">部分付款</a-select-option><a-select-option value="completed">已完成</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="resetSearch">重置</a-button></a-form-item>
      </a-form>
      <div style="margin-bottom:12px;display:flex;justify-content:space-between">
        <div><a-button type="primary" @click="showAdd">新建应付合同</a-button><a-button style="margin-left:8px" @click="handleImport">导入</a-button></div>
        <a-tag color="red">合同金额合计：¥{{ totalAmount.toLocaleString() }}</a-tag>
      </div>
      <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered :scroll="{ x: 2800 }" @change="onTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'contractAmount'">¥{{ (record.contractAmount || 0).toLocaleString() }}</template>
          <template v-else-if="column.key === 'paidAmount'">¥{{ (record.paidAmount || 0).toLocaleString() }}</template>
          <template v-else-if="column.key === 'unpaidAmount'"><span :style="{color:(record.unpaidAmount||0)>0?'#ff4d4f':'inherit'}">¥{{ (record.unpaidAmount || 0).toLocaleString() }}</span></template>
          <template v-else-if="column.key === 'paymentStatus'">
            <a-tag :color="record.paymentStatus==='completed'?'green':record.paymentStatus==='partial'?'orange':'red'">
              {{ record.paymentStatus==='completed'?'已完成':record.paymentStatus==='partial'?'部分付款':'未付款' }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'paymentProgress'"><a-progress :percent="record.paymentProgress||0" size="small" /></template>
          <template v-else-if="column.key === 'action'"><a-button type="link" size="small" @click="editRecord(record)">编辑</a-button></template>
        </template>
      </a-table>
    </a-card>
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

const searchForm = reactive({ keyword: '', project: '', supplier: '', paymentStatus: '' })
const dataList = ref<any[]>([]); const loading = ref(false); const totalAmount = ref(0)
const modalVisible = ref(false); const currentRecord = ref<any>(null); const modalTitle = ref('')
const projectOptions = ref<string[]>([]); const supplierOptions = ref<string[]>([])
const pagination = reactive({ current:1, pageSize:10, total:0, showSizeChanger:true, showTotal:(t:number)=>`共 ${t} 条` })

const columns = [
  { title: '合同编号', dataIndex: 'contractNo', width:120, fixed:'left' as const },
  { title: '合同名称', dataIndex: 'contractName', width:200 },
  { title: '合同类型', dataIndex: 'contractType', width:100 },
  { title: '我方公司', dataIndex: 'ourCompany', width:140 },
  { title: '对方单位', dataIndex: 'counterparty', width:140 },
  { title: '项目编号', dataIndex: 'projectNo', width:120 },
  { title: '项目名称', dataIndex: 'projectName', width:160 },
  { title: '合同负责人', dataIndex: 'manager', width:100 },
  { title: '合同状态', dataIndex: 'status', width:90 },
  { title: '合同金额', key:'contractAmount', width:120 },
  { title: '结算金额', dataIndex: 'settlementAmount', width:120 },
  { title: '已付款', key:'paidAmount', width:110 },
  { title: '已收票', dataIndex: 'receivedInvoiceAmount', width:110 },
  { title: '未付款', key:'unpaidAmount', width:110 },
  { title: '未收票', dataIndex: 'unreceivedInvoiceAmount', width:110 },
  { title: '付款未收票', dataIndex: 'paidNotReceivedInvoice', width:110 },
  { title: '收票未付款', dataIndex: 'receivedInvoiceNotPaid', width:110 },
  { title: '付款状态', key:'paymentStatus', width:100 },
  { title: '付款进度', key:'paymentProgress', width:100 },
  { title: '收票进度', dataIndex: 'receivedInvoiceProgress', width:100 },
  { title: '附件', dataIndex: 'attachment', width:80 },
  { title: '备注', dataIndex: 'remark', width:150 },
  { title: '操作', key:'action', width:80, fixed:'right' as const },
]

onMounted(() => { loadData(); loadOptions() })

async function loadOptions() {
  try {
    const res = await authFetch('/api/options')
    const d = await res.json()
    if (d.code === 200) {
      projectOptions.value = (d.data.projects || []).map((p:any) => p.label || p.value || p)
      supplierOptions.value = (d.data.suppliers || []).map((s:any) => s.label || s.value || s)
    }
  } catch {}
}

async function loadData() {
  loading.value = true
  const params = new URLSearchParams({ page:String(pagination.current), size:String(pagination.pageSize), ...searchForm })
  const res = await authFetch(`/api/contracts?direction=payable&${params}`)
  const data = await res.json()
  if (data.code === 200) { dataList.value = data.data.records; pagination.total = data.data.total; totalAmount.value = data.data.totalAmount||0 }
  loading.value = false
}
function resetSearch() { Object.assign(searchForm,{keyword:'',project:'',supplier:'',paymentStatus:''}); pagination.current=1; loadData() }
function onTableChange(pag:any) { pagination.current=pag.current; pagination.pageSize=pag.pageSize; loadData() }
function showAdd() { currentRecord.value=null; modalTitle.value='新建应付合同'; modalVisible.value=true }
function editRecord(record:any) { currentRecord.value={...record}; modalTitle.value='编辑应付合同'; modalVisible.value=true }
function onSaved() { modalVisible.value=false; loadData() }
function handleImport() { message.info('导入功能开发中，即将支持 Excel 批量导入') }
</script>
