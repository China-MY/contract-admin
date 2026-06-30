<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">通知渠道配置</span></template>
    <a-button type="primary" style="margin-bottom:12px" @click="showAdd">新增配置</a-button>
    <a-table :dataSource="configList" :columns="columns" :loading="loading" rowKey="id" size="small" bordered :pagination="{pageSize:10}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='channelType'">
          <a-tag :color="record.channelType==='feishu'?'blue':record.channelType==='dingtalk'?'orange':'green'">
            {{record.channelType==='feishu'?'飞书':record.channelType==='dingtalk'?'钉钉':'邮件'}}
          </a-tag>
        </template>
        <template v-else-if="column.key==='status'">
          <a-tag :color="record.status==='enabled'?'green':'red'">{{record.status==='enabled'?'启用':'禁用'}}</a-tag>
        </template>
        <template v-else-if="column.key==='action'">
          <a-button type="link" size="small" @click="edit(record)">编辑</a-button>
          <a-button type="link" size="small" danger @click="del(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="modalVisible" :title="modalTitle" width="55%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="配置名称" name="configName" :rules="[{required:true}]"><a-input v-model:value="form.configName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="渠道类型" name="channelType" :rules="[{required:true}]">
            <a-select v-model:value="form.channelType">
              <a-select-option value="feishu">飞书机器人</a-select-option>
              <a-select-option value="dingtalk">钉钉机器人</a-select-option>
              <a-select-option value="email">邮件</a-select-option>
            </a-select>
          </a-form-item></a-col>
          <a-col :span="24"><a-form-item label="Webhook URL"><a-input v-model:value="form.webhookUrl" placeholder="机器人Webhook地址（飞书/钉钉必填）" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="签名密钥"><a-input v-model:value="form.secret" placeholder="可选" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="收件邮箱"><a-input v-model:value="form.email" placeholder="邮件渠道时填写" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="状态"><a-select v-model:value="form.status"><a-select-option value="enabled">启用</a-select-option><a-select-option value="disabled">禁用</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="备注"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px"><a-button @click="modalVisible=false">取消</a-button><a-button type="primary" html-type="submit">保存</a-button></div>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'

const configList = ref<any[]>([]); const loading = ref(false)
const modalVisible = ref(false); const modalTitle = ref(''); const currentId = ref<number|null>(null)
const form = reactive<any>({ configName:'', channelType:'feishu', webhookUrl:'', secret:'', email:'', status:'enabled', remark:'' })
const columns = [
  { title:'名称', dataIndex:'configName', width:180 },
  { title:'渠道', key:'channelType', width:100 },
  { title:'Webhook', dataIndex:'webhookUrl', ellipsis:true },
  { title:'邮箱', dataIndex:'email', width:180 },
  { title:'状态', key:'status', width:80 },
  { title:'备注', dataIndex:'remark', width:200 },
  { title:'操作', key:'action', width:140, fixed:'right' as const },
]

onMounted(loadData)
async function loadData() {
  loading.value = true
  const res = await authFetch('/api/project-milestones/notify-configs')
  const d = await res.json()
  if (d.code === 200) configList.value = d.data || []
  loading.value = false
}
function showAdd() { currentId.value = null; modalTitle.value = '新增通知配置'; Object.assign(form, { configName:'', channelType:'feishu', webhookUrl:'', secret:'', email:'', status:'enabled', remark:'' }); modalVisible.value = true }
function edit(r: any) { currentId.value = r.id; modalTitle.value = '编辑配置'; Object.assign(form, r); modalVisible.value = true }
async function handleSave() {
  const url = currentId.value ? `/api/project-milestones/notify-configs/${currentId.value}` : '/api/project-milestones/notify-configs'
  const method = currentId.value ? 'PUT' : 'POST'
  const res = await authFetch(url, { method, body: JSON.stringify(form) })
  const d = await res.json()
  if (d.code === 200) { message.success('保存成功'); modalVisible.value = false; loadData() }
  else message.error(d.msg || '保存失败')
}
function del(r: any) {
  Modal.confirm({ title:'确认删除', content:`确定删除「${r.configName}」吗？`, onOk: async () => {
    const res = await authFetch(`/api/project-milestones/notify-configs/${r.id}`, { method:'DELETE' })
    const d = await res.json()
    if (d.code === 200) { message.success('删除成功'); loadData() } else message.error(d.msg || '删除失败')
  }})
}
</script>
