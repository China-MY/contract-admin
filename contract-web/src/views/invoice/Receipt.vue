<template>
  <div class="invoice-page">
    <a-card>
      <template #title><span style="font-size:18px;font-weight:bold">进项发票</span></template>
      <a-form layout="inline" :model="searchForm" style="margin-bottom:16px">
        <a-form-item label="关键字"><a-input v-model:value="searchForm.keyword" placeholder="发票号码" allowClear /></a-form-item>
        <a-form-item label="发票类型"><a-select v-model:value="searchForm.type" placeholder="请选择" allowClear style="width:130px"><a-select-option value="专用发票">专用发票</a-select-option></a-select></a-form-item>
        <a-form-item label="开票日期"><a-date-picker v-model:value="searchForm.date" style="width:160px" /></a-form-item>
        <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="resetSearch">重置</a-button></a-form-item>
      </a-form>
      <div style="margin-bottom:12px;display:flex;justify-content:space-between">
        <div><a-button type="primary" @click="showAdd">新建进项发票</a-button><a-button style="margin-left:8px" @click="()=>message.info('导入功能开发中')">导入</a-button></div>
        <a-tag color="red">发票金额（含税）合计：¥{{ totalAmount.toLocaleString() }}</a-tag>
      </div>
      <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onTableChange">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'amountWithTax'">¥{{ (record.amountWithTax||0).toLocaleString() }}</template>
          <template v-else-if="column.key === 'action'"><a-button type="link" size="small" @click="editRecord(record)">编辑</a-button></template>
        </template>
      </a-table>
    </a-card>
    <a-modal v-model:open="modalVisible" :title="modalTitle" width="70%" :footer="null" destroyOnClose>
      <InvoiceForm v-if="modalVisible" :record="currentRecord" @saved="onSaved" @cancel="modalVisible=false" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'
import InvoiceForm from './InvoiceForm.vue'

const searchForm = reactive({ keyword: '', type: '', date: null })
const dataList = ref<any[]>([]); const loading = ref(false); const totalAmount = ref(0)
const modalVisible = ref(false); const currentRecord = ref<any>(null); const modalTitle = ref('')
const pagination = reactive({ current:1, pageSize:10, total:0, showSizeChanger:true, showTotal:(t:number)=>`共 ${t} 条` })
const columns = [
  { title:'合同编号', dataIndex:'contractNo', width:120 }, { title:'合同名称', dataIndex:'contractName', width:180 },
  { title:'发票号码', dataIndex:'invoiceNo', width:130 }, { title:'发票方向', dataIndex:'direction', width:80 },
  { title:'发票类型', dataIndex:'type', width:90 }, { title:'发票金额(含税)', key:'amountWithTax', width:120 },
  { title:'不含税额', dataIndex:'amountWithoutTax', width:110 }, { title:'税率', dataIndex:'taxRate', width:60 },
  { title:'税额', dataIndex:'taxAmount', width:100 }, { title:'开票日期', dataIndex:'issueDate', width:100 },
  { title:'凭证号', dataIndex:'voucherNo', width:100 }, { title:'开票方', dataIndex:'issuer', width:140 },
  { title:'开票方税号', dataIndex:'issuerTaxId', width:120 }, { title:'收票方', dataIndex:'receiver', width:140 },
  { title:'收票方税号', dataIndex:'receiverTaxId', width:120 }, { title:'备注', dataIndex:'remark', width:150 },
  { title:'操作', key:'action', width:80, fixed:'right' as const },
]
onMounted(loadData)
async function loadData() {
  loading.value = true
  const params = new URLSearchParams({ page:String(pagination.current), size:String(pagination.pageSize) })
  const res = await authFetch(`/api/invoices?direction=input&${params}`)
  const data = await res.json()
  if (data.code === 200) { dataList.value = data.data.records; pagination.total = data.data.total; totalAmount.value = data.data.totalAmount||0 }
  loading.value = false
}
function resetSearch() { Object.assign(searchForm,{keyword:'',type:'',date:null}); loadData() }
function onTableChange(pag:any) { pagination.current=pag.current; pagination.pageSize=pag.pageSize; loadData() }
function showAdd() { currentRecord.value=null; modalTitle.value='新建进项发票'; modalVisible.value=true }
function editRecord(record:any) { currentRecord.value={...record}; modalTitle.value='编辑进项发票'; modalVisible.value=true }
function onSaved() { modalVisible.value=false; loadData() }
</script>
