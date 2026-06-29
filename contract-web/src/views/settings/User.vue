<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">用户管理</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:12px">
      <a-form-item label="关键字"><a-input v-model:value="sf.keyword" placeholder="姓名/账号" allowClear /></a-form-item>
      <a-form-item label="角色">
        <a-select v-model:value="sf.role" placeholder="请选择" allowClear style="width:140px">
          <a-select-option value="超级管理员">超级管理员</a-select-option>
          <a-select-option value="普通用户">普通用户</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="状态">
        <a-select v-model:value="sf.status" placeholder="请选择" allowClear style="width:120px">
          <a-select-option value="normal">正常</a-select-option>
          <a-select-option value="disabled">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建用户</a-button>

    <a-table :dataSource="dataList" :columns="columns" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='status'">
          <a-tag :color="record.status==='normal'?'green':'red'">{{record.status==='normal'?'正常':'禁用'}}</a-tag>
        </template>
        <template v-else-if="column.key==='lastLoginAt'">
          {{ record.lastLoginAt ? record.lastLoginAt.replace('T',' ').substring(0,19) : '-' }}
        </template>
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="60%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="真实姓名" name="realName" :rules="[{required:true,message:'请输入姓名'}]">
              <a-input v-model:value="form.realName" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="登录账号" name="username" :rules="[{required:true,message:'请输入账号'}]">
              <a-input v-model:value="form.username" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="密码" name="password" :rules="currentId?[]:[{required:true,message:'请输入密码'}]">
              <a-input-password v-model:value="form.password" :placeholder="currentId?'留空不修改':''" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="性别" name="gender">
              <a-select v-model:value="form.gender"><a-select-option value="男">男</a-select-option><a-select-option value="女">女</a-select-option></a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12"><a-form-item label="手机号" name="phone"><a-input v-model:value="form.phone" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="邮箱" name="email"><a-input v-model:value="form.email" /></a-form-item></a-col>
          <a-col :span="8">
            <a-form-item label="角色" name="roleNames">
              <a-select v-model:value="form.roleNames" placeholder="选择角色" :options="roleOptions" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="部门" name="deptName">
              <a-select v-model:value="form.deptName" placeholder="选择部门" allowClear :options="deptOptions" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="岗位" name="postName">
              <a-select v-model:value="form.postName" placeholder="选择岗位" allowClear :options="postOptions" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="关联公司" name="companyIds">
              <a-select v-model:value="form.companyIds" mode="multiple" placeholder="选择公司" :options="companyOptions" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="状态" name="status">
              <a-select v-model:value="form.status">
                <a-select-option value="normal">正常</a-select-option>
                <a-select-option value="disabled">禁用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
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

const sf = reactive({keyword:'',role:'',status:''})
const dataList = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('')
const currentId = ref<number|null>(null)
const saving = ref(false)
const roleOptions = ref<any[]>([])
const deptOptions = ref<any[]>([])
const postOptions = ref<any[]>([])
const companyOptions = ref<any[]>([])

const form = reactive({
  realName:'', username:'', password:'', gender:'', phone:'', email:'',
  roleNames:'普通用户', deptName:undefined as string|undefined, postName:undefined as string|undefined,
  companyIds:[] as string[], status:'normal', remark:''
})

const columns = [
  { title:'真实姓名', dataIndex:'realName', width:100 },
  { title:'登录账号', dataIndex:'username', width:120 },
  { title:'手机号', dataIndex:'phone', width:120 },
  { title:'角色', dataIndex:'roleNames', width:100 },
  { title:'部门', dataIndex:'deptName', width:100 },
  { title:'岗位', dataIndex:'postName', width:100 },
  { title:'所属公司', dataIndex:'companyNames', width:200 },
  { title:'状态', key:'status', width:70 },
  { title:'最后登录', dataIndex:'lastLoginAt', width:160, ellipsis:true },
  { title:'操作', key:'action', width:120, fixed:'right' as const },
]

onMounted(() => { loadData(); loadCompanies(); loadRoles(); loadDepts(); loadPosts() })

async function loadCompanies() {
  const res = await fetch('/api/settings/companies')
  const d = await res.json()
  if (d.code === 200) {
    companyOptions.value = (d.data || []).map((c: any) => ({ label: c.companyName, value: String(c.id) }))
  }
}
async function loadRoles() {
  const res = await authFetch('/api/roles'); const d = await res.json()
  if (d.code === 200) roleOptions.value = (d.data || []).map((r: any) => ({ label: r.roleName, value: r.roleName }))
}
async function loadDepts() {
  const res = await authFetch('/api/depts'); const d = await res.json()
  if (d.code === 200) deptOptions.value = (d.data || []).map((r: any) => ({ label: r.deptName, value: r.deptName }))
}
async function loadPosts() {
  const res = await authFetch('/api/posts'); const d = await res.json()
  if (d.code === 200) postOptions.value = (d.data || []).map((r: any) => ({ label: r.postName, value: r.postName }))
}

async function loadData() {
  loading.value = true
  const p = new URLSearchParams({ page:'1', size:'10' })
  const res = await authFetch(`/api/users?${p}`)
  const d = await res.json()
  if (d.code === 200) dataList.value = d.data || []
  loading.value = false
}

async function reset() { Object.assign(sf,{keyword:'',role:'',status:''}); await loadData() }

function showAdd() {
  currentId.value = null
  modalTitle.value = '新建用户'
  Object.assign(form, { realName:'', username:'', password:'', gender:'', phone:'', email:'', roleNames:'普通用户', deptName:undefined, postName:undefined, companyIds:[], status:'normal', remark:'' })
  modalVisible.value = true
}

function editRecord(record: any) {
  currentId.value = record.id
  modalTitle.value = '编辑用户'
  const companyIds = record.companyIds ? String(record.companyIds).split(',').filter(Boolean) : []
  Object.assign(form, { ...record, password:'', companyIds, deptName: record.deptName || undefined, postName: record.postName || undefined })
  modalVisible.value = true
}

async function handleSave() {
  saving.value = true
  // 从公司ID数组获取公司名称
  const names = form.companyIds
    .map((id: string) => companyOptions.value.find((o: any) => o.value === id)?.label)
    .filter(Boolean)
    .join(',')
  const payload = {
    ...form,
    companyIds: form.companyIds.join(','),
    companyNames: names,
    password: form.password || undefined
  }
  if (!currentId.value) delete payload.password
  else if (!payload.password) delete payload.password

  const url = currentId.value ? `/api/users/${currentId.value}` : '/api/users'
  const method = currentId.value ? 'PUT' : 'POST'
  const res = await authFetch(url, { method, body: JSON.stringify(payload) })
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
    title:'确认删除', content:`确定删除用户「${record.realName}」吗？`,
    onOk: async () => {
      const res = await authFetch(`/api/users/${record.id}`, { method:'DELETE' })
      const d = await res.json()
      if (d.code === 200) { message.success('删除成功'); loadData() }
      else message.error(d.msg || '删除失败')
    }
  })
}
</script>
