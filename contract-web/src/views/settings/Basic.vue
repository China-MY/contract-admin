<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">基础设置</span></template>
    <a-layout style="background:transparent">
      <a-layout-sider theme="light" width="200" style="background:#fff;border-right:1px solid #f0f0f0">
        <a-menu :selectedKeys="[activeKey]" mode="inline" @click="onMenuClick" :items="menuItems" />
      </a-layout-sider>
      <a-layout-content style="padding-left:16px;min-height:400px">
        <a-form layout="inline" style="margin-bottom:12px">
          <a-form-item label="关键字"><a-input v-model:value="sf.keyword" placeholder="名称" allowClear /></a-form-item>
          <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="()=>{Object.assign(sf,{keyword:''});loadData()}">重置</a-button></a-form-item>
        </a-form>

        <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建</a-button>

        <a-table :dataSource="dataList" :columns="dynamicCols" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
          <template #bodyCell="{column,record}">
            <template v-if="column.key==='status'">
              <a-tag :color="record.status==='enabled'?'green':'red'">{{record.status==='enabled'?'启用':'禁用'}}</a-tag>
            </template>
            <template v-else-if="column.key==='isDefault'||column.key==='receivableDefault'||column.key==='payableDefault'">
              <a-tag v-if="record[column.key]" color="blue">是</a-tag>
            </template>
            <template v-else-if="column.key==='action'">
              <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
              <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
            </template>
          </template>
        </a-table>
      </a-layout-content>
    </a-layout>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="50%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="选项标签" name="label" :rules="[{required:true,message:'请输入'}]">
              <a-input v-model:value="form.label" />
            </a-form-item>
          </a-col>
          <a-col :span="12" v-if="activeKey==='tax_rate'">
            <a-form-item label="选项键值" name="value">
              <a-input v-model:value="form.value" placeholder="如 0.13" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="排序" name="sortOrder">
              <a-input-number v-model:value="form.sortOrder" style="width:100%" :min="0" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="状态" name="status">
              <a-select v-model:value="form.status">
                <a-select-option value="enabled">启用</a-select-option>
                <a-select-option value="disabled">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8" v-if="activeKey==='contract_type'">
            <a-form-item label="应收默认" name="receivableDefault">
              <a-switch v-model:checked="form.receivableDefault" />
            </a-form-item>
          </a-col>
          <a-col :span="8" v-if="activeKey==='contract_type'">
            <a-form-item label="应付默认" name="payableDefault">
              <a-switch v-model:checked="form.payableDefault" />
            </a-form-item>
          </a-col>
          <a-col :span="16" v-if="activeKey!=='contract_type'">
            <a-form-item label="默认项" name="isDefault">
              <a-switch v-model:checked="form.isDefault" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="备注" name="remark">
              <a-textarea v-model:value="form.remark" :rows="2" />
            </a-form-item>
          </a-col>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'

import { authFetch } from '../../utils/auth'

const activeKey = ref('contract_type')
const sf = reactive({keyword:''})
const dataList=ref<any[]>([]);const loading=ref(false)
const modalVisible=ref(false);const modalTitle=ref('');const currentId=ref<number|null>(null);const saving=ref(false)
const form=reactive({label:'',value:'',sortOrder:1,status:'enabled',isDefault:false,receivableDefault:false,payableDefault:false,remark:''})

const menuItems = [
  { key:'contract_type', label:'合同类型' }, { key:'project_type', label:'项目类型' },
  { key:'project_source', label:'项目来源' }, { key:'region', label:'区域' },
  { key:'payment_method', label:'支付方式' }, { key:'expense_type', label:'费用类型' },
  { key:'bank', label:'银行' }, { key:'tax_rate', label:'税率' },
  { key:'reminder_settings', label:'提醒设置' }, { key:'payment_settings', label:'其他设置' },
]

function onMenuClick(info:{key:string}){activeKey.value=info.key;loadData()}

const dynamicCols = computed(() => {
  const isTax = activeKey.value === 'tax_rate'
  const isContractType = activeKey.value === 'contract_type'
  return [
    { title: isTax ? '选项标签' : '类型名称', dataIndex:'label', width:150 },
    ...(isTax ? [{title:'选项键值',dataIndex:'value',width:100}] : []),
    { title:'排序', dataIndex:'sortOrder', width:60 },
    { title:'状态', key:'status', width:70 },
    ...(isContractType ? [
      { title:'应收默认', key:'receivableDefault', width:80 },
      { title:'应付默认', key:'payableDefault', width:80 },
    ] : [{ title:'默认项', key:'isDefault', width:70 }]),
    { title:'备注', dataIndex:'remark', width:200 },
    { title:'操作', key:'action', width:120, fixed:'right' as const },
  ]
})

async function loadData(){
  loading.value=true
  const res=await authFetch(`/api/settings/dict?type=${activeKey.value}`);const d=await res.json()
  if(d.code===200)dataList.value=d.data||[]
  loading.value=false
}

function showAdd(){
  currentId.value=null;modalTitle.value='新建字典项'
  Object.assign(form,{label:'',value:'',sortOrder:1,status:'enabled',isDefault:false,receivableDefault:false,payableDefault:false,remark:''})
  modalVisible.value=true
}

function editRecord(record:any){
  currentId.value=record.id;modalTitle.value='编辑字典项'
  Object.assign(form,record)
  modalVisible.value=true
}

async function handleSave(){
  saving.value=true
  const payload={...form,dictType:activeKey.value}
  const url=currentId.value?`/api/settings/dict/${currentId.value}`:'/api/settings/dict'
  const method=currentId.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(payload)})
  const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}
  else message.error(d.msg||'保存失败')
  saving.value=false
}

function deleteRecord(record:any){
  Modal.confirm({
    title:'确认删除',content:`确定删除「${record.label}」吗？`,
    onOk:async()=>{
      const res=await authFetch(`/api/settings/dict/${record.id}`,{method:'DELETE'})
      const d=await res.json()
      if(d.code===200){message.success('删除成功');loadData()}
      else message.error(d.msg||'删除失败')
    }
  })
}

onMounted(loadData)
</script>
