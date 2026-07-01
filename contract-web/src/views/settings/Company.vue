<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">公司管理</span></template>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建公司</a-button>

    <a-table :dataSource="dataList" :columns="columns" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
          <a-button v-if="!record.isDefault" type="link" size="small" @click="setDefault(record)">设为默认</a-button>
          <a-tag v-else color="blue">默认</a-tag>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="60%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="公司名称" name="companyName" :rules="[{required:true,message:'请输入公司名称'}]">
            <a-input v-model:value="form.companyName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="统一社会信用代码" name="taxId">
            <a-input v-model:value="form.taxId" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="联系电话" name="phone">
            <a-input v-model:value="form.phone" /></a-form-item></a-col>
          <a-col :span="16"><a-form-item label="公司地址" name="address">
            <a-input v-model:value="form.address" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="发票抬头" name="invoiceTitle">
            <a-input v-model:value="form.invoiceTitle" /></a-form-item></a-col>
          <a-col :span="6"><a-form-item label="开户银行" name="bankName">
            <a-input v-model:value="form.bankName" /></a-form-item></a-col>
          <a-col :span="6"><a-form-item label="银行账号" name="bankAccount">
            <a-input v-model:value="form.bankAccount" /></a-form-item></a-col>
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
import { message, Modal } from 'ant-design-vue'

import { authFetch } from '../../utils/auth'

const dataList = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('')
const currentId = ref<number|null>(null)
const form = reactive({ companyName:'', taxId:'', phone:'', address:'', invoiceTitle:'', bankName:'', bankAccount:'' })

const columns = [
  { title:'公司名称', dataIndex:'companyName', width:180 },
  { title:'统一社会信用代码', dataIndex:'taxId', width:150 },
  { title:'联系电话', dataIndex:'phone', width:120 },
  { title:'公司地址', dataIndex:'address', width:250 },
  { title:'发票抬头', dataIndex:'invoiceTitle', width:180 },
  { title:'开户银行', dataIndex:'bankName', width:150 },
  { title:'银行账号', dataIndex:'bankAccount', width:150 },
  { title:'操作', key:'action', width:200, fixed:'right' as const },
]

onMounted(loadData)

async function loadData() {
  loading.value = true
  const res = await authFetch('/api/settings/companies')
  const d = await res.json()
  if (d.code === 200) dataList.value = d.data || []
  loading.value = false
}

function showAdd() {
  currentId.value = null
  modalTitle.value = '新建公司'
  Object.assign(form, { companyName:'', taxId:'', phone:'', address:'', invoiceTitle:'', bankName:'', bankAccount:'', isDefault: false })
  modalVisible.value = true
}

function editRecord(record: any) {
  currentId.value = record.id
  modalTitle.value = '编辑公司'
  Object.assign(form, record)
  modalVisible.value = true
}

async function handleSave() {
  const url = currentId.value ? `/api/settings/companies/${currentId.value}` : '/api/settings/companies'
  const method = currentId.value ? 'PUT' : 'POST'
  const res = await authFetch(url, { method, body: JSON.stringify(form) })
  const d = await res.json()
  if (d.code === 200) {
    message.success('保存成功')
    modalVisible.value = false
    loadData()
  } else {
    message.error(d.msg || '保存失败')
  }
}

function deleteRecord(record: any) {
  if (record.isDefault) {
    message.warning('默认公司不能删除，请先设置其他公司为默认')
    return
  }
  Modal.confirm({
    title: '确认删除',
    content: `确定删除公司「${record.companyName}」吗？`,
    onOk: async () => {
      const res = await authFetch(`/api/settings/companies/${record.id}`, { method:'DELETE' })
      const d = await res.json()
      if (d.code === 200) { message.success('删除成功'); loadData() }
      else message.error(d.msg || '删除失败')
    }
  })
}

async function setDefault(record: any) {
  const res = await authFetch(`/api/settings/companies/${record.id}/default`, { method:'PUT' })
  const d = await res.json()
  if (d.code === 200) { message.success('已设为默认公司'); loadData() }
}
</script>
