<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">资金账户</span></template>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建账户</a-button>

    <a-table :dataSource="dataList" :columns="columns" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='status'">
          <a-tag :color="record.status==='enabled'?'green':'red'">{{record.status==='enabled'?'启用':'禁用'}}</a-tag>
        </template>
        <template v-if="column.key==='type'">
          {{ record.type === 'company' ? '公司账户' : '个人账户' }}
        </template>
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="55%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="账户名称" name="accountName" :rules="[{required:true,message:'请输入账户名称'}]">
              <a-input v-model:value="form.accountName" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="账号" name="accountNo">
              <a-input v-model:value="form.accountNo" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="开户银行" name="bankName">
              <a-input v-model:value="form.bankName" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="类型" name="type">
              <a-select v-model:value="form.type">
                <a-select-option value="company">公司账户</a-select-option>
                <a-select-option value="personal">个人账户</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="状态" name="status">
              <a-select v-model:value="form.status">
                <a-select-option value="enabled">启用</a-select-option>
                <a-select-option value="disabled">禁用</a-select-option>
              </a-select>
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
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'

import { authFetch } from '../../utils/auth'

const dataList = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('')
const currentId = ref<number|null>(null)
const saving = ref(false)
const form = reactive({ accountName:'', accountNo:'', bankName:'', type:'company', status:'enabled', remark:'' })

const columns = [
  { title:'账户名称', dataIndex:'accountName', width:180 },
  { title:'账号', dataIndex:'accountNo', width:180 },
  { title:'开户银行', dataIndex:'bankName', width:200 },
  { title:'类型', key:'type', width:100 },
  { title:'状态', key:'status', width:80 },
  { title:'备注', dataIndex:'remark', width:250 },
  { title:'操作', key:'action', width:140, fixed:'right' as const },
]

onMounted(loadData)

async function loadData() {
  loading.value = true
  const res = await authFetch('/api/settings/fund-accounts')
  const d = await res.json()
  if (d.code === 200) dataList.value = d.data || []
  loading.value = false
}

function showAdd() {
  currentId.value = null
  modalTitle.value = '新建资金账户'
  Object.assign(form, { accountName:'', accountNo:'', bankName:'', type:'company', status:'enabled', remark:'' })
  modalVisible.value = true
}

function editRecord(record: any) {
  currentId.value = record.id
  modalTitle.value = '编辑资金账户'
  Object.assign(form, record)
  modalVisible.value = true
}

async function handleSave() {
  saving.value = true
  const url = currentId.value ? `/api/settings/fund-accounts/${currentId.value}` : '/api/settings/fund-accounts'
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
  saving.value = false
}

function deleteRecord(record: any) {
  Modal.confirm({
    title: '确认删除',
    content: `确定删除资金账户「${record.accountName}」吗？`,
    onOk: async () => {
      const res = await authFetch(`/api/settings/fund-accounts/${record.id}`, { method:'DELETE' })
      const d = await res.json()
      if (d.code === 200) { message.success('删除成功'); loadData() }
      else message.error(d.msg || '删除失败')
    }
  })
}
</script>
