<template>
  <a-card><template #title><span style="font-size:18px;font-weight:bold">收款记录</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="收款方式"><a-select v-model:value="sf.method" placeholder="请选择" allowClear style="width:140px">
        <a-select-option value="农业银行">农业银行</a-select-option><a-select-option value="工商银行">工商银行</a-select-option>
        <a-select-option value="微信支付">微信支付</a-select-option><a-select-option value="现金">现金</a-select-option>
      </a-select></a-form-item>
      <a-form-item label="收款日期"><a-date-picker v-model:value="sf.date" style="width:160px" /></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="()=>{Object.assign(sf,{method:'',date:null});loadData()}">重置</a-button></a-form-item>
    </a-form>
    <div style="margin-bottom:12px"><a-button type="primary" @click="()=>message.info('新建收款记录功能开发中')">新建收款记录</a-button><a-button style="margin-left:8px" @click="()=>message.info('导入功能开发中')">导入</a-button></div>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onChange">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='amount'">¥{{(record.amount||0).toLocaleString()}}</template>
        <template v-else-if="column.key==='status'"><a-tag :color="record.status==='confirmed'?'green':'orange'">{{record.status==='confirmed'?'已收款':'待确认'}}</a-tag></template>
        <template v-else-if="column.key==='action'"><a-button type="link" size="small" @click="()=>message.info('编辑功能开发中')">编辑</a-button></template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
const sf = reactive({method:'',date:null})
const dataList=ref<any[]>([]);const loading=ref(false);const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const columns=[
  {title:'合同编号',dataIndex:'contractNo',width:120},{title:'合同名称',dataIndex:'contractName',width:180},
  {title:'发票号码',dataIndex:'invoiceNo',width:130},{title:'收款单号',dataIndex:'recordNo',width:130},
  {title:'收款日期',dataIndex:'recordDate',width:100},{title:'收款金额',key:'amount',width:110},
  {title:'收款状态',key:'status',width:90},{title:'收款方式',dataIndex:'method',width:100},
  {title:'收款账户',dataIndex:'account',width:120},{title:'费用类型',dataIndex:'expenseType',width:90},
  {title:'付款方',dataIndex:'payer',width:120},{title:'付款方银行',dataIndex:'payerBank',width:130},
  {title:'收款方',dataIndex:'payee',width:120},{title:'收款方银行',dataIndex:'payeeBank',width:130},
  {title:'凭证号',dataIndex:'voucherNo',width:100},{title:'备注',dataIndex:'remark',width:150},
  {title:'操作',key:'action',width:80,fixed:'right' as const},
]
onMounted(loadData)
async function loadData(){
  loading.value=true;const params=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await fetch(`/api/payment-records?direction=receipt&${params}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false
}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
</script>
