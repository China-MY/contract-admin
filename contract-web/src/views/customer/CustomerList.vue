<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">{{ isSupplier ? '供应商' : '客户' }}列表</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="关键字"><a-input v-model:value="sf.keyword" placeholder="名称" allowClear /></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">{{ isSupplier ? '新建供应商' : '新建客户' }}</a-button>

    <a-table :dataSource="dataList" :columns="columns" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="55%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="名称" name="name" :rules="[{required:true,message:'请输入'}]"><a-input v-model:value="form.name" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="联系人" name="contactPerson"><a-input v-model:value="form.contactPerson" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="联系电话" name="phone"><a-input v-model:value="form.phone" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="邮箱" name="email"><a-input v-model:value="form.email" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="税号" name="taxId"><a-input v-model:value="form.taxId" placeholder="统一社会信用代码" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="开户行" name="bankName"><a-input v-model:value="form.bankName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="银行账号" name="bankAccount"><a-input v-model:value="form.bankAccount" /></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="地址" name="address"><a-input v-model:value="form.address" /></a-form-item></a-col>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { useRoute } from 'vue-router'
import { authFetch } from '../../utils/auth'

const route = useRoute()
const isSupplier = computed(() => route.path.includes('supplier'))
const apiBase = computed(() => isSupplier.value ? '/api/suppliers' : '/api/customers')

const sf = reactive({keyword:''})
const dataList = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const modalTitle = ref('')
const currentId = ref<number|null>(null)
const saving = ref(false)
const form = reactive({ name:'', contactPerson:'', phone:'', email:'', address:'', remark:'', taxId:'', bankName:'', bankAccount:'' })

const columns = [
  { title:'名称', dataIndex:'name', width:200 },
  { title:'联系人', dataIndex:'contactPerson', width:120 },
  { title:'联系电话', dataIndex:'phone', width:130 },
  { title:'邮箱', dataIndex:'email', width:180 },
  { title:'税号', dataIndex:'taxId', width:150 },
  { title:'开户行', dataIndex:'bankName', width:150 },
  { title:'银行账号', dataIndex:'bankAccount', width:150 },
  { title:'地址', dataIndex:'address', width:250 },
  { title:'备注', dataIndex:'remark', width:200 },
  { title:'操作', key:'action', width:140, fixed:'right' as const },
]

onMounted(() => { loadData() })

async function loadData() {
  loading.value = true
  const p = new URLSearchParams({ page:'1', size:'20' })
  const res = await authFetch(`${apiBase.value}?${p}`)
  const d = await res.json()
  if (d.code === 200) dataList.value = d.data?.records || d.data || []
  loading.value = false
}

function reset() { Object.assign(sf,{keyword:''}); loadData() }
function showAdd() { currentId.value=null; modalTitle.value=isSupplier.value?'新建供应商':'新建客户'; Object.assign(form,{name:'',contactPerson:'',phone:'',email:'',address:'',remark:''}); modalVisible.value=true }
function editRecord(r:any) { currentId.value=r.id; modalTitle.value='编辑'; Object.assign(form,r); modalVisible.value=true }

async function handleSave() {
  saving.value=true
  const payload={...form,type:isSupplier.value?'supplier':'customer'}
  const url=currentId.value?`${apiBase.value}/${currentId.value}`:apiBase.value
  const method=currentId.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(payload)})
  const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}
  else message.error(d.msg||'保存失败')
  saving.value=false
}

function deleteRecord(r:any) {
  Modal.confirm({ title:'确认删除', content:`确定删除「${r.name}」吗？`,
    onOk:async()=>{
      const res=await authFetch(`${apiBase.value}/${r.id}`,{method:'DELETE'})
      const d=await res.json()
      if(d.code===200){message.success('删除成功');loadData()}
      else message.error(d.msg||'删除失败')
    }
  })
}
</script>
