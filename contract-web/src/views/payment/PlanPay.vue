<template>
  <a-card><template #title><span style="font-size:18px;font-weight:bold">付款计划</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="付款状态"><a-select v-model:value="sf.status" placeholder="请选择" allowClear style="width:130px">
        <a-select-option value="unpaid">未付</a-select-option><a-select-option value="partial">部分</a-select-option><a-select-option value="paid">已付</a-select-option>
      </a-select></a-form-item>
      <a-form-item label="计划日期"><a-date-picker v-model:value="sf.date" style="width:160px" /></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="()=>{Object.assign(sf,{status:'',date:null});loadData()}">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建付款计划</a-button>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onChange">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='plannedAmount'">¥{{(record.plannedAmount||0).toLocaleString()}}</template>
        <template v-else-if="column.key==='status'"><a-tag :color="record.status==='paid'?'green':record.status==='partial'?'orange':'red'">{{record.status==='paid'?'已付':record.status==='partial'?'部分':'未付'}}</a-tag></template>
        <template v-else-if="column.key==='action'"><a-button type="link" size="small" @click="()=>message.info('付款功能开发中，即将支持一键付款')">付款</a-button></template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
const sf = reactive({status:'',date:null})
const dataList=ref<any[]>([]);const loading=ref(false);const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const columns=[
  {title:'合同编号',dataIndex:'contractNo',width:120},{title:'合同名称',dataIndex:'contractName',width:180},
  {title:'发票号码',dataIndex:'invoiceNo',width:130},{title:'计划编码',dataIndex:'planCode',width:130},
  {title:'计划付款金额',key:'plannedAmount',width:130},{title:'已付金额',dataIndex:'paidAmount',width:110},
  {title:'未付金额',dataIndex:'unpaidAmount',width:110},{title:'计划付款日期',dataIndex:'plannedDate',width:110},
  {title:'付款状态',key:'status',width:90},{title:'付款进度',dataIndex:'progress',width:100},
  {title:'付款方',dataIndex:'payer',width:140},{title:'收款方',dataIndex:'payee',width:140},
  {title:'备注',dataIndex:'remark',width:150},{title:'操作',key:'action',width:80,fixed:'right' as const},
]
onMounted(loadData)
async function loadData(){
  loading.value=true;const params=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await fetch(`/api/payment-plans?direction=pay&${params}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false
}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
function showAdd(){message.info('新建付款计划功能开发中')}
</script>
