<template>
  <a-card><template #title><span style="font-size:18px;font-weight:bold">收款记录</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="收款方式"><a-select v-model:value="sf.method" placeholder="请选择" allowClear style="width:140px">
        <a-select-option value="银行转账">银行转账</a-select-option><a-select-option value="现金">现金</a-select-option>
      </a-select></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <div style="margin-bottom:12px"><a-button type="primary" @click="showAdd">新建收款记录</a-button><a-button style="margin-left:8px" @click="()=>message.info('导入功能开发中')">导入</a-button></div>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onChange">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='amount'">¥{{(record.amount||0).toLocaleString()}}</template>
        <template v-else-if="column.key==='status'"><a-tag :color="record.status==='confirmed'?'green':'orange'">{{record.status==='confirmed'?'已收款':'待确认'}}</a-tag></template>
        <template v-else-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>
    <a-modal v-model:open="modalVisible" :title="modalTitle" width="65%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="8"><a-form-item label="合同编号"><a-input v-model:value="form.contractNo" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="合同名称"><a-input v-model:value="form.contractName" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="发票号码"><a-input v-model:value="form.invoiceNo" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="收款金额"><a-input-number v-model:value="form.amount" style="width:100%" :min="0" :precision="2" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="收款日期"><a-date-picker v-model:value="form.recordDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="收款方式"><a-select v-model:value="form.method"><a-select-option value="银行转账">银行转账</a-select-option><a-select-option value="现金">现金</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="收款状态"><a-select v-model:value="form.status"><a-select-option value="pending">待确认</a-select-option><a-select-option value="confirmed">已收款</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="付款方"><SelectCreate v-model="form.payer" :options="allPartyOptions" placeholder="搜索或选择" @create="(n:string)=>form.payer=n" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="收款方"><SelectCreate v-model="form.payee" :options="allPartyOptions" placeholder="搜索或选择" @create="(n:string)=>form.payee=n" /></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="备注"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px"><a-button @click="modalVisible=false">取消</a-button><a-button type="primary" html-type="submit" style="margin-left:8px" :loading="saving">保存</a-button></div>
      </a-form>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'
import SelectCreate from '../../components/SelectCreate.vue'
import dayjs from 'dayjs'

const sf=reactive({method:''});const dataList=ref<any[]>([]);const loading=ref(false)
const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const modalVisible=ref(false);const modalTitle=ref('');const currentId=ref<number|null>(null);const saving=ref(false)
const form=reactive<any>({contractNo:'',contractName:'',invoiceNo:'',amount:0,recordDate:null,method:'银行转账',status:'pending',payer:'',payee:'',remark:''})
const allPartyOptions=ref<any[]>([])
async function loadOptions() {
  try {
    const r=await authFetch('/api/options');const d=await r.json()
    if(d.code===200){
      const cust=(d.data.customers||[]).map((x:any)=>({label:x.label||x,value:x.label||x}))
      const supp=(d.data.suppliers||[]).map((x:any)=>({label:x.label||x,value:x.label||x}))
      const all=[...cust,...supp]
      const r2=await authFetch('/api/settings/companies');const d2=await r2.json()
      if(d2.code===200){
        const comp=(d2.data||[]).map((c:any)=>({label:c.companyName,value:c.companyName}))
        allPartyOptions.value=[...all,...comp]
      }else{
        allPartyOptions.value=all
      }
    }
  }catch(e){}
}

const columns=[
  {title:'合同编号',dataIndex:'contractNo',width:120},{title:'合同名称',dataIndex:'contractName',width:180},
  {title:'收款单号',dataIndex:'recordNo',width:130},{title:'收款日期',dataIndex:'recordDate',width:100},
  {title:'收款金额',key:'amount',width:110},{title:'收款状态',key:'status',width:90},
  {title:'收款方式',dataIndex:'method',width:100},{title:'费用类型',dataIndex:'expenseType',width:90},
  {title:'付款方',dataIndex:'payer',width:120},{title:'收款方',dataIndex:'payee',width:120},
  {title:'备注',dataIndex:'remark',width:150},{title:'操作',key:'action',width:140,fixed:'right' as const},
]
onMounted(()=>{loadData();loadOptions()})
async function loadData(){loading.value=true;const p=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await authFetch(`/api/payment-records?direction=receipt&${p}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
function reset(){Object.assign(sf,{method:''});loadData()}
function showAdd(){currentId.value=null;modalTitle.value='新建收款记录';Object.assign(form,{contractNo:'',contractName:'',invoiceNo:'',amount:0,recordDate:null,method:'银行转账',status:'pending',payer:'',payee:'',remark:''});modalVisible.value=true}
function editRecord(r:any){currentId.value=r.id;modalTitle.value='编辑收款记录';Object.assign(form,{...r,recordDate:r.recordDate?dayjs(r.recordDate):null});modalVisible.value=true}
async function handleSave(){saving.value=true;const payload={...form,direction:'receipt',recordNo:'RC-' + Date.now()}
  if(payload.recordDate)payload.recordDate=dayjs(payload.recordDate).format('YYYY-MM-DD')
  const url=currentId.value?`/api/payment-records/${currentId.value}`:'/api/payment-records';const method=currentId.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(payload)});const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}else message.error(d.msg||'保存失败');saving.value=false}
function deleteRecord(r:any){Modal.confirm({title:'确认删除',content:`确定删除吗？`,onOk:async()=>{const res=await authFetch(`/api/payment-records/${r.id}`,{method:'DELETE'});const d=await res.json();if(d.code===200){message.success('删除成功');loadData()}else message.error(d.msg||'删除失败')}})}
</script>
