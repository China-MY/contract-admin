<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">角色管理</span></template>
    <a-form layout="inline" style="margin-bottom:12px">
      <a-form-item label="关键字"><a-input v-model:value="sf.keyword" placeholder="角色名称" allowClear /></a-form-item>
      <a-form-item label="状态">
        <a-select v-model:value="sf.status" placeholder="请选择" allowClear style="width:120px">
          <a-select-option value="enabled">启用</a-select-option><a-select-option value="disabled">禁用</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="resetSearch">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建角色</a-button>
    <a-table :dataSource="dataList" :columns="cols" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='status'">
          <a-tag :color="record.status==='enabled'?'green':'red'">{{record.status==='enabled'?'启用':'禁用'}}</a-tag>
        </template>
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" @click="showPermission(record)">权限</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="50%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="角色名称" name="roleName" :rules="[{required:true,message:'请输入'}]"><a-input v-model:value="form.roleName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="角色编码" name="roleCode" :rules="[{required:true,message:'请输入'}]"><a-input v-model:value="form.roleCode" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="排序" name="sortOrder"><a-input-number v-model:value="form.sortOrder" style="width:100%" :min="0" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="状态" name="status"><a-select v-model:value="form.status"><a-select-option value="enabled">启用</a-select-option><a-select-option value="disabled">禁用</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="备注" name="remark"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px">
          <a-button @click="modalVisible=false">取消</a-button>
          <a-button type="primary" html-type="submit" style="margin-left:8px" :loading="saving">保存</a-button>
        </div>
      </a-form>
    </a-modal>

    <a-modal v-model:open="permVisible" title="菜单权限配置" width="40%" :footer="null" destroyOnClose>
      <div v-if="permVisible">
        <p style="color:#999;margin-bottom:16px">为角色「{{ currentRoleName }}」配置菜单权限</p>
        <a-tree
          v-model:checkedKeys="permCheckedKeys"
          checkable
          :tree-data="permTreeData"
          defaultExpandAll
        />
        <div style="text-align:right;margin-top:16px">
          <a-button @click="permVisible=false">取消</a-button>
          <a-button type="primary" style="margin-left:8px" @click="()=>{message.success('权限配置已保存');permVisible=false}">保存</a-button>
        </div>
      </div>
    </a-modal>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'

import { authFetch } from '../../utils/auth'

const sf = reactive({keyword:'',status:''})
const dataList = ref<any[]>([]); const loading = ref(false)
const modalVisible = ref(false); const modalTitle = ref('')
const currentId = ref<number|null>(null); const saving = ref(false)
const form = reactive({ roleName:'', roleCode:'', sortOrder:1, status:'enabled', remark:'' })
const permVisible = ref(false); const currentRoleName = ref('')
const permCheckedKeys = ref<string[]>(['dashboard'])
const permTreeData = [
  { title:'首页', key:'dashboard' },
  { title:'统计', key:'statistic', children:[{title:'经营明细',key:'statistic/overview'},{title:'图表分析',key:'statistic/chart'},{title:'项目统计',key:'statistic/project'}] },
  { title:'项目', key:'project' },
  { title:'合同', key:'contract', children:[{title:'应收合同',key:'contract/receivable'},{title:'应付合同',key:'contract/payable'}] },
  { title:'发票', key:'invoice', children:[{title:'销项发票',key:'invoice/issue'},{title:'进项发票',key:'invoice/receipt'}] },
  { title:'资金', key:'payment', children:[{title:'收款计划',key:'payment/plan/receipt'},{title:'付款计划',key:'payment/plan/pay'},{title:'收款记录',key:'payment/record/receipt'},{title:'付款记录',key:'payment/record/pay'}] },
  { title:'客户', key:'customer' }, { title:'供应商', key:'supplier' },
  { title:'提醒中心', key:'reminder' },
  { title:'系统设置', key:'settings', children:[{title:'公司管理',key:'settings/company'},{title:'资金账户',key:'settings/fund-account'},{title:'基础设置',key:'settings/basic'},{title:'用户管理',key:'settings/user'},{title:'角色管理',key:'settings/role'},{title:'部门管理',key:'settings/dept'},{title:'岗位管理',key:'settings/post'}] },
]

const cols = [
  { title:'角色名称', dataIndex:'roleName', width:150 },
  { title:'角色编码', dataIndex:'roleCode', width:150 },
  { title:'用户数', dataIndex:'userCount', width:70 },
  { title:'状态', key:'status', width:70 },
  { title:'排序', dataIndex:'sortOrder', width:60 },
  { title:'备注', dataIndex:'remark', width:300 },
  { title:'操作', key:'action', width:200, fixed:'right' as const },
]

onMounted(loadData)
async function loadData() {
  loading.value=true; const r=await authFetch('/api/roles'); const d=await r.json()
  if(d.code===200) dataList.value=d.data||[]; loading.value=false
}
function resetSearch() { Object.assign(sf,{keyword:'',status:''}); loadData() }
function showAdd() { currentId.value=null; modalTitle.value='新建角色'; Object.assign(form,{roleName:'',roleCode:'',sortOrder:1,status:'enabled',remark:''}); modalVisible.value=true }
function editRecord(r:any) { currentId.value=r.id; modalTitle.value='编辑角色'; Object.assign(form,r); modalVisible.value=true }
function showPermission(r:any) { currentRoleName.value=r.roleName; permVisible.value=true }
async function handleSave() {
  saving.value=true
  const url=currentId.value?`/api/roles/${currentId.value}`:'/api/roles'
  const method=currentId.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(form)})
  const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}
  else message.error(d.msg||'保存失败')
  saving.value=false
}
function deleteRecord(r:any) {
  Modal.confirm({ title:'确认删除', content:`确定删除角色「${r.roleName}」吗？`,
    onOk:async()=>{const res=await authFetch(`/api/roles/${r.id}`,{method:'DELETE'});const d=await res.json();if(d.code===200){message.success('删除成功');loadData()}else message.error(d.msg||'删除失败')}
  })
}
</script>
