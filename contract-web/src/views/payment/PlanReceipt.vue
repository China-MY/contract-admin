<template>
  <a-card><template #title><span style="font-size:18px;font-weight:bold">收款计划</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="收款状态"><a-select v-model:value="sf.status" placeholder="请选择" allowClear style="width:130px">
        <a-select-option value="unpaid">未收</a-select-option><a-select-option value="partial">部分</a-select-option><a-select-option value="paid">已收</a-select-option>
      </a-select></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="()=>{Object.assign(sf,{status:''});loadData()}">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建收款计划</a-button>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onChange">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='plannedAmount'">¥{{(record.plannedAmount||0).toLocaleString()}}</template>
        <template v-else-if="column.key==='paidAmount'">¥{{(record.paidAmount||0).toLocaleString()}}</template>
        <template v-else-if="column.key==='status'"><a-tag :color="record.status==='paid'?'green':record.status==='partial'?'orange':'red'">{{record.status==='paid'?'已收':record.status==='partial'?'部分':'未收'}}</a-tag></template>
        <template v-else-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="55%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="计划编码"><a-input :value="'PS-'+new Date().toISOString().slice(0,10).replace(/-/g,'')+'-XXX'" disabled /></a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="关联合同" name="contractNo">
              <SelectCreate v-model="form.contractNo" :options="contractOptions" placeholder="搜索合同，回车创建" @create="handleCreateContract" @change="onContractChange" />
            </a-form-item>
          </a-col>
          <a-col :span="8"><a-form-item label="计划金额" name="plannedAmount"><a-input-number v-model:value="form.plannedAmount" style="width:100%" :min="0" :precision="2" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="计划日期" name="plannedDate"><a-date-picker v-model:value="form.plannedDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="状态" name="status"><a-select v-model:value="form.status"><a-select-option value="unpaid">未收</a-select-option><a-select-option value="partial">部分</a-select-option><a-select-option value="paid">已收</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="付款方" name="payer"><SelectCreate v-model="form.payer" :options="partyOptions" placeholder="搜索客户或供应商" @create="(n:string)=>form.payer=n" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="收款方" name="payee"><SelectCreate v-model="form.payee" :options="companyOptions" placeholder="选择我方公司" @create="(n:string)=>form.payee=n" /></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="备注" name="remark"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px">
          <a-button @click="modalVisible=false">取消</a-button>
          <a-button type="primary" html-type="submit" style="margin-left:8px" :loading="saving">保存</a-button>
        </div>
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

const sf = reactive({status:''})
const dataList=ref<any[]>([]);const loading=ref(false)
const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const modalVisible=ref(false);const modalTitle=ref('');const currentId=ref<number|null>(null);const saving=ref(false)
const form=reactive<any>({contractNo:'',contractName:'',plannedAmount:0,plannedDate:null,status:'unpaid',payer:'',payee:'',remark:''})
const contractOptions=ref<any[]>([])
const partyOptions=ref<any[]>([])
const companyOptions=ref<any[]>([])
async function loadOptions(){try{const r=await authFetch('/api/options');const d=await r.json();if(d.code===200){
  const cust=(d.data.customers||[]).map((x:any)=>({label:x.label||x,value:x.label||x}))
  const supp=(d.data.suppliers||[]).map((x:any)=>({label:x.label||x,value:x.label||x}))
  partyOptions.value=[...cust,...supp]}
  const r2=await authFetch('/api/settings/companies');const d2=await r2.json();if(d2.code===200)
  companyOptions.value=(d2.data||[]).map((c:any)=>({label:c.companyName,value:c.companyName}))
}catch{}}

function onContractChange(val:string){
  const found=contractOptions.value.find((c:any)=>c.value===val)
  if(found){
    form.contractName=found.name
    // 收款计划：付款方=对方单位，收款方=我方公司
    if(found.counterparty) form.payer=found.counterparty
    if(found.ourCompany) form.payee=found.ourCompany
  }
}
}
async function handleCreateContract(name:string){
  const res=await authFetch('/api/contracts',{method:'POST',body:JSON.stringify({contractName:name,direction:'receipt'})})
  if(res.ok){const r2=await authFetch('/api/options');const d2=await r2.json();if(d2.code===200)contractOptions.value=d2.data.contracts||[];form.contractNo='';form.contractName=name}
}

const columns=[
  {title:'合同编号',dataIndex:'contractNo',width:120},{title:'合同名称',dataIndex:'contractName',width:180},
  {title:'计划编码',dataIndex:'planCode',width:130},{title:'计划收款金额',key:'plannedAmount',width:130},
  {title:'已收金额',key:'paidAmount',width:110},{title:'计划收款日期',dataIndex:'plannedDate',width:110},
  {title:'收款状态',key:'status',width:90},{title:'付款方',dataIndex:'payer',width:140},{title:'收款方',dataIndex:'payee',width:140},
  {title:'备注',dataIndex:'remark',width:150},{title:'操作',key:'action',width:140,fixed:'right' as const},
]

onMounted(()=>{loadData();loadOptions();(async()=>{try{const r=await authFetch('/api/options');const d=await r.json();if(d.code===200)contractOptions.value=d.data.contracts||[]}catch{}})()})
async function loadData(){loading.value=true;const p=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await authFetch(`/api/payment-plans?direction=receipt&${p}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
function showAdd(){currentId.value=null;modalTitle.value='新建收款计划';Object.assign(form,{contractNo:'',contractName:'',plannedAmount:0,plannedDate:null,status:'unpaid',payer:'',payee:'',remark:''});modalVisible.value=true}
function editRecord(r:any){currentId.value=r.id;modalTitle.value='编辑收款计划';Object.assign(form,{...r,plannedDate:r.plannedDate?dayjs(r.plannedDate):null});modalVisible.value=true}
async function handleSave(){saving.value=true;const payload={...form,direction:'receipt'}
  if(payload.plannedDate)payload.plannedDate=dayjs(payload.plannedDate).format('YYYY-MM-DD')
  delete payload.planCode
  const url=currentId.value?`/api/payment-plans/${currentId.value}`:'/api/payment-plans';const method=currentId.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(payload)});const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}else message.error(d.msg||'保存失败');saving.value=false}
function deleteRecord(r:any){Modal.confirm({title:'确认删除',content:`确定删除吗？`,onOk:async()=>{const res=await authFetch(`/api/payment-plans/${r.id}`,{method:'DELETE'});const d=await res.json();if(d.code===200){message.success('删除成功');loadData()}else message.error(d.msg||'删除失败')}})}
</script>
