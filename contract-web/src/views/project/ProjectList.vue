<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">项目列表</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="关键字"><a-input v-model:value="sf.keyword" placeholder="项目编号/名称" allowClear /></a-form-item>
      <a-form-item label="项目类型"><a-select v-model:value="sf.type" placeholder="请选择" allowClear style="width:140px">
        <a-select-option v-for="t in types" :key="t" :value="t">{{t}}</a-select-option>
      </a-select></a-form-item>
      <a-form-item label="状态"><a-select v-model:value="sf.status" placeholder="请选择" allowClear style="width:120px">
        <a-select-option value="init">立项</a-select-option><a-select-option value="in_progress">进行中</a-select-option><a-select-option value="completed">已完成</a-select-option>
      </a-select></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建项目</a-button>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onChange">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='status'"><a-tag :color="record.status==='completed'?'green':record.status==='in_progress'?'blue':'orange'">{{record.status==='completed'?'已完成':record.status==='in_progress'?'进行中':'立项'}}</a-tag></template>
        <template v-else-if="column.key==='budgetAmount'">¥{{(record.budgetAmount||0).toLocaleString()}}</template>
        <template v-else-if="column.key==='action'"><a-button type="link" size="small" @click="edit(record)">编辑</a-button></template>
      </template>
    </a-table>

    <!-- 新建/编辑项目弹窗 -->
    <a-modal v-model:open="modalVisible" :title="modalTitle" width="60%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="formModel" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="项目编号"><a-input v-model:value="formModel.projectNo" :disabled="!currentRecord" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="项目名称" name="projectName"><a-input v-model:value="formModel.projectName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="项目类型"><a-select v-model:value="formModel.projectType"><a-select-option v-for="t in types" :key="t" :value="t">{{t}}</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="项目状态"><a-select v-model:value="formModel.status"><a-select-option value="init">立项</a-select-option><a-select-option value="in_progress">进行中</a-select-option><a-select-option value="completed">已完成</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="项目年度" name="year"><a-input v-model:value="formModel.year" placeholder="如 2026" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="项目来源" name="source"><a-input v-model:value="formModel.source" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="客户名称" name="customerName"><a-input v-model:value="formModel.customerName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="项目经理" name="manager"><a-input v-model:value="formModel.manager" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="开始日期" name="startDate"><a-date-picker v-model:value="formModel.startDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="计划结束" name="endDate"><a-date-picker v-model:value="formModel.endDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="预算金额"><a-input-number v-model:value="formModel.budgetAmount" style="width:100%" :min="0" /></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="备注" name="remark"><a-textarea v-model:value="formModel.remark" :rows="2" /></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px">
          <a-button @click="modalVisible=false">取消</a-button>
          <a-button type="primary" html-type="submit" style="margin-left:8px">保存</a-button>
        </div>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'
import dayjs from 'dayjs'
const sf = reactive({keyword:'',type:'',status:''})
const dataList=ref<any[]>([]);const loading=ref(false);const types=ref(['研发项目','实施项目','维护项目','咨询项目','其他'])
const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const columns=[
  {title:'项目编号',dataIndex:'projectNo',width:130},{title:'项目名称',dataIndex:'projectName',width:200},
  {title:'项目类型',dataIndex:'projectType',width:100},{title:'项目状态',key:'status',width:90},
  {title:'项目年度',dataIndex:'year',width:80},{title:'项目来源',dataIndex:'source',width:100},
  {title:'项目经理',dataIndex:'manager',width:100},{title:'开始日期',dataIndex:'startDate',width:100},
  {title:'计划结束',dataIndex:'endDate',width:100},{title:'预算金额',key:'budgetAmount',width:120},
  {title:'客户名称',dataIndex:'customerName',width:140},{title:'备注',dataIndex:'remark',width:200},
  {title:'操作',key:'action',width:80,fixed:'right' as const},
]
const modalVisible=ref(false);const currentRecord=ref<any>(null);const modalTitle=ref('')
const formModel=ref<any>({projectNo:'',projectName:'',projectType:'',status:'init',year:'',source:'',manager:'',customerName:'',startDate:null,endDate:null,budgetAmount:0,remark:''})

onMounted(loadData)
async function loadData(){loading.value=true;const p=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await authFetch(`/api/projects?${p}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false}
function reset(){Object.assign(sf,{keyword:'',type:'',status:''});loadData()}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
function genProjectNo() {
  const now = new Date()
  const d = now.getFullYear() + String(now.getMonth()+1).padStart(2,'0') + String(now.getDate()).padStart(2,'0')
  return 'PRJ-' + d + '-' + String(dataList.value.length + 1).padStart(3,'0')
}
function showAdd(){currentRecord.value=null;formModel.value={projectNo:genProjectNo(),projectName:'',projectType:'',status:'init',year:'',source:'',manager:'',customerName:'',startDate:null,endDate:null,budgetAmount:0,remark:''};modalTitle.value='新建项目';modalVisible.value=true}
function edit(r:any){currentRecord.value={...r};formModel.value={...r,startDate:r.startDate?dayjs(r.startDate):null,endDate:r.endDate?dayjs(r.endDate):null};modalTitle.value='编辑项目';modalVisible.value=true}
async function handleSave(){
  const payload={...formModel.value}
  if(payload.startDate)payload.startDate=dayjs(payload.startDate).format('YYYY-MM-DD')
  if(payload.endDate)payload.endDate=dayjs(payload.endDate).format('YYYY-MM-DD')
  const url=currentRecord.value?`/api/projects/${currentRecord.value.id}`:'/api/projects'
  const method=currentRecord.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(payload)})
  const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}
  else message.error(d.msg||'保存失败')
}
</script>
