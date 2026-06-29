<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">登录日志</span></template>
    <a-form layout="inline" style="margin-bottom:12px">
      <a-form-item label="用户名"><a-input v-model:value="sf.username" placeholder="请输入" allowClear /></a-form-item>
      <a-form-item label="状态">
        <a-select v-model:value="sf.status" placeholder="全部" allowClear style="width:120px">
          <a-select-option value="success">成功</a-select-option>
          <a-select-option value="fail">失败</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-table :dataSource="dataList" :columns="cols" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10, showTotal:(t:number)=>`共 ${t} 条`}">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'status'">
          <a-tag :color="record.status === 'success' ? 'green' : 'red'">{{ record.status === 'success' ? '成功' : '失败' }}</a-tag>
        </template>
        <template v-else-if="column.key === 'loginTime'">
          {{ formatTime(record.loginTime) }}
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { authFetch } from '../../utils/auth'

const sf = reactive({username:'',status:''})
const dataList = ref<any[]>([])
const loading = ref(false)
const cols = [
  {title:'用户名', dataIndex:'username', width:120},
  {title:'IP地址', dataIndex:'ip', width:140},
  {title:'状态', key:'status', width:70},
  {title:'消息', dataIndex:'message', width:400},
  {title:'登录时间', key:'loginTime', width:180},
]

function formatTime(t: string) {
  if (!t) return '-'
  return t.replace('T', ' ').substring(0, 19)
}

onMounted(loadData)
async function loadData() {
  loading.value = true
  const r = await authFetch('/api/logs/login')
  const d = await r.json()
  if (d.code === 200) dataList.value = d.data || []
  loading.value = false
}
function reset() { Object.assign(sf,{username:'',status:''}); loadData() }
</script>
