<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">操作日志</span></template>
    <a-form layout="inline" style="margin-bottom:12px">
      <a-form-item label="用户名"><a-input v-model:value="sf.username" placeholder="请输入" allowClear /></a-form-item>
      <a-form-item label="模块">
        <a-select v-model:value="sf.module" placeholder="全部" allowClear style="width:130px">
          <a-select-option value="合同管理">合同管理</a-select-option>
          <a-select-option value="发票管理">发票管理</a-select-option>
          <a-select-option value="系统设置">系统设置</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-table :dataSource="dataList" :columns="cols" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10, showTotal:(t:number)=>`共 ${t} 条`}">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'operationTime'">
          {{ formatTime(record.operationTime) }}
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { authFetch } from '../../utils/auth'

const sf = reactive({username:'',module:''})
const dataList = ref<any[]>([])
const loading = ref(false)
const cols = [
  {title:'用户名', dataIndex:'username', width:100},
  {title:'模块', dataIndex:'module', width:100},
  {title:'操作', dataIndex:'action', width:100},
  {title:'操作对象', dataIndex:'target', width:200},
  {title:'详情', dataIndex:'detail', width:400},
  {title:'IP', dataIndex:'ip', width:130},
  {title:'操作时间', key:'operationTime', width:180},
]

function formatTime(t: string) {
  if (!t) return '-'
  return t.replace('T', ' ').substring(0, 19)
}

onMounted(loadData)
async function loadData() {
  loading.value = true
  const r = await authFetch('/api/logs/operation')
  const d = await r.json()
  if (d.code === 200) dataList.value = d.data || []
  loading.value = false
}
function reset() { Object.assign(sf,{username:'',module:''}); loadData() }
</script>
