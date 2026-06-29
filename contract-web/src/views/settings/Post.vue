<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">岗位管理</span></template>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新建岗位</a-button>
    <a-table :dataSource="dataList" :columns="cols" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='status'">
          <a-tag :color="record.status==='enabled'?'green':'red'">{{record.status==='enabled'?'启用':'禁用'}}</a-tag>
        </template>
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="50%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="岗位名称" name="postName" :rules="[{required:true,message:'请输入'}]">
              <a-input v-model:value="form.postName" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="岗位编码" name="postCode" :rules="[{required:true,message:'请输入'}]">
              <a-input v-model:value="form.postCode" />
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

const dataList = ref<any[]>([]); const loading = ref(false)
const modalVisible = ref(false); const modalTitle = ref('')
const currentId = ref<number|null>(null); const saving = ref(false)
const form = reactive({ postName:'', postCode:'', sortOrder:1, status:'enabled', remark:'' })
const cols = [
  { title:'岗位名称', dataIndex:'postName', width:200 },
  { title:'岗位编码', dataIndex:'postCode', width:150 },
  { title:'排序', dataIndex:'sortOrder', width:60 },
  { title:'状态', key:'status', width:70 },
  { title:'备注', dataIndex:'remark', width:350 },
  { title:'操作', key:'action', width:140, fixed:'right' as const },
]

onMounted(loadData)
async function loadData() { loading.value=true; const r=await authFetch('/api/posts'); const d=await r.json(); if(d.code===200) dataList.value=d.data||[]; loading.value=false }
function showAdd() { currentId.value=null; modalTitle.value='新建岗位'; Object.assign(form,{postName:'',postCode:'',sortOrder:1,status:'enabled',remark:''}); modalVisible.value=true }
function editRecord(r:any) { currentId.value=r.id; modalTitle.value='编辑岗位'; Object.assign(form,r); modalVisible.value=true }
async function handleSave() {
  saving.value=true; const url=currentId.value?`/api/posts/${currentId.value}`:'/api/posts'; const method=currentId.value?'PUT':'POST'
  const res=await authFetch(url,{method,body:JSON.stringify(form)}); const d=await res.json()
  if(d.code===200){message.success('保存成功');modalVisible.value=false;loadData()}else message.error(d.msg||'保存失败'); saving.value=false
}
function deleteRecord(r:any) {
  Modal.confirm({ title:'确认删除', content:`确定删除岗位「${r.postName}」吗？`,
    onOk:async()=>{const res=await authFetch(`/api/posts/${r.id}`,{method:'DELETE'});const d=await res.json();if(d.code===200){message.success('删除成功');loadData()}else message.error(d.msg||'删除失败')}
  })
}
</script>
