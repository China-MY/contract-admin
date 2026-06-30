<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">项目进度追踪</span></template>

    <!-- 项目筛选 -->
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="项目">
        <a-select v-model:value="sf.projectNo" placeholder="选择项目" allowClear style="width:260px" @change="loadData">
          <a-select-option v-for="p in projects" :key="p.projectNo" :value="p.projectNo">{{ p.projectName }}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="阶段状态">
        <a-select v-model:value="sf.status" placeholder="全部" allowClear style="width:130px" @change="loadData">
          <a-select-option value="pending">未开始</a-select-option>
          <a-select-option value="in_progress">进行中</a-select-option>
          <a-select-option value="completed">已完成</a-select-option>
          <a-select-option value="delayed">已延期</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="showAdd">新建阶段</a-button>
        <a-button style="margin-left:8px" @click="showNotifyConfig">通知配置</a-button>
      </a-form-item>
    </a-form>

    <!-- 里程碑列表 -->
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" rowKey="id" size="small" bordered>
      <template #bodyCell="{column,record}">
        <template v-if="column.key==='stageOrder'">{{ record.stageOrder }}</template>
        <template v-if="column.key==='status'">
          <a-tag :color="record.status==='completed'?'green':record.status==='in_progress'?'blue':record.status==='delayed'?'red':'default'">
            {{record.status==='pending'?'未开始':record.status==='in_progress'?'进行中':record.status==='completed'?'已完成':'已延期'}}
          </a-tag>
        </template>
        <template v-if="column.key==='progress'">
          <a-progress :percent="record.progress||0" size="small" style="width:120px" />
        </template>
        <template v-if="column.key==='action'">
          <a-button type="link" size="small" @click="editRecord(record)">编辑</a-button>
          <a-button type="link" size="small" @click="sendNotify(record)">通知</a-button>
          <a-button type="link" size="small" danger @click="deleteRecord(record)">删除</a-button>
        </template>
      </template>
    </a-table>

    <!-- 阶段编辑弹窗 -->
    <a-modal v-model:open="modalVisible" :title="modalTitle" width="65%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="关联项目" name="projectNo" :rules="[{required:true,message:'请选择项目'}]">
              <a-select v-model:value="form.projectNo" placeholder="选择项目" @change="val=>{const p=projects.find(x=>x.projectNo===val);if(p)form.projectName=p.projectName}">
                <a-select-option v-for="p in projects" :key="p.projectNo" :value="p.projectNo">{{ p.projectName }}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="阶段名称" name="stageName" :rules="[{required:true,message:'请输入阶段名称'}]">
              <a-input v-model:value="form.stageName" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="排序号" name="stageOrder">
              <a-input-number v-model:value="form.stageOrder" style="width:100%" :min="1" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="负责人" name="assignee">
              <a-input v-model:value="form.assignee" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="计划开始" name="plannedStartDate">
              <a-date-picker v-model:value="form.plannedStartDate" style="width:100%" />
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="计划结束" name="plannedEndDate">
              <a-date-picker v-model:value="form.plannedEndDate" style="width:100%" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="状态" name="status">
              <a-select v-model:value="form.status">
                <a-select-option value="pending">未开始</a-select-option>
                <a-select-option value="in_progress">进行中</a-select-option>
                <a-select-option value="completed">已完成</a-select-option>
                <a-select-option value="delayed">已延期</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="进度 %" name="progress">
              <a-slider v-model:value="form.progress" :min="0" :max="100" :step="5" />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="实际开始" name="actualStartDate">
              <a-date-picker v-model:value="form.actualStartDate" style="width:100%" />
            </a-form-item>
          </a-col>
          <a-col :span="24">
            <a-form-item label="交付内容" name="deliverableContent">
              <a-textarea v-model:value="form.deliverableContent" :rows="3" placeholder="描述该阶段需要交付的内容" />
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

    <!-- 通知配置弹窗 -->
    <a-modal v-model:open="configModalVisible" title="通知渠道配置" width="55%" :footer="null" destroyOnClose>
      <div style="margin-bottom:12px"><a-button type="primary" @click="showAddConfig">新增配置</a-button></div>
      <a-table :dataSource="configList" rowKey="id" size="small" bordered :pagination="{pageSize:5}">
        <a-column title="名称" dataIndex="configName" />
        <a-column title="渠道" dataIndex="channelType">
          <template #default="{text}">{{text==='feishu'?'飞书':text==='dingtalk'?'钉钉':text==='email'?'邮件':text}}</template>
        </a-column>
        <a-column title="Webhook" dataIndex="webhookUrl" ellipsis />
        <a-column title="状态" dataIndex="status">
          <template #default="{text}"><a-tag :color="text==='enabled'?'green':'red'">{{text==='enabled'?'启用':'禁用'}}</a-tag></template>
        </a-column>
        <a-column title="操作" key="action">
          <template #default="{record}">
            <a-button type="link" size="small" @click="editConfig(record)">编辑</a-button>
            <a-button type="link" size="small" danger @click="deleteConfig(record)">删除</a-button>
          </template>
        </a-column>
      </a-table>
    </a-modal>
    <!-- 通知配置编辑弹窗 -->
    <a-modal v-model:open="configEditVisible" :title="configEditTitle" width="50%" :footer="null" destroyOnClose>
      <a-form v-if="configEditVisible" :model="configForm" layout="vertical" @finish="handleSaveConfig">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="配置名称" :rules="[{required:true}]"><a-input v-model:value="configForm.configName" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="渠道类型" :rules="[{required:true}]"><a-select v-model:value="configForm.channelType"><a-select-option value="feishu">飞书</a-select-option><a-select-option value="dingtalk">钉钉</a-select-option><a-select-option value="email">邮件</a-select-option></a-select></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="Webhook URL"><a-input v-model:value="configForm.webhookUrl" placeholder="机器人Webhook地址" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="密钥"><a-input v-model:value="configForm.secret" placeholder="签名密钥（可选）" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="邮箱"><a-input v-model:value="configForm.email" placeholder="收件邮箱" /></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="状态"><a-select v-model:value="configForm.status"><a-select-option value="enabled">启用</a-select-option><a-select-option value="disabled">禁用</a-select-option></a-select></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px"><a-button @click="configEditVisible=false">取消</a-button><a-button type="primary" html-type="submit" :loading="savingConfig">保存</a-button></div>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'
import dayjs from 'dayjs'

const sf = reactive({ projectNo: undefined, status: undefined })
const dataList = ref<any[]>([])
const projects = ref<any[]>([])
const loading = ref(false)
const modalVisible = ref(false); const modalTitle = ref(''); const currentId = ref<number|null>(null); const saving = ref(false)
const form = reactive<any>({ projectNo:'', projectName:'', stageName:'', stageOrder:1, deliverableContent:'', plannedStartDate:null, plannedEndDate:null, actualStartDate:null, actualEndDate:null, status:'pending', progress:0, assignee:'', remark:'' })

const columns = [
  { title:'序号', key:'stageOrder', width:60 }, { title:'项目', dataIndex:'projectName', width:180 },
  { title:'阶段名称', dataIndex:'stageName', width:180 }, { title:'负责人', dataIndex:'assignee', width:100 },
  { title:'计划开始', dataIndex:'plannedStartDate', width:100 }, { title:'计划结束', dataIndex:'plannedEndDate', width:100 },
  { title:'状态', key:'status', width:80 }, { title:'进度', key:'progress', width:150 },
  { title:'交付内容', dataIndex:'deliverableContent', ellipsis:true },
  { title:'操作', key:'action', width:180, fixed:'right' as const },
]

// 通知配置
const configModalVisible = ref(false); const configList = ref<any[]>([])
const configEditVisible = ref(false); const configEditTitle = ref(''); const configCurrentId = ref<number|null>(null); const savingConfig = ref(false)
const configForm = reactive<any>({ configName:'', channelType:'feishu', webhookUrl:'', secret:'', email:'', status:'enabled' })

onMounted(async () => {
  try {
    const r = await authFetch('/api/options')
    const d = await r.json()
    if (d.code === 200) projects.value = (d.data.projects || []).map((p: any) => ({ projectNo: p.value || p.projectNo || p.label, projectName: p.label || p.projectName || p }))
    // Also try projects API
    const r2 = await authFetch('/api/projects?page=1&size=100')
    const d2 = await r2.json()
    if (d2.code === 200) {
      const list = d2.data?.records || d2.data || []
      if (list.length > 0) projects.value = list.map((p: any) => ({ projectNo: p.projectNo, projectName: p.projectName }))
    }
  } catch {}
  loadData()
})

async function loadData() {
  loading.value = true
  let url = '/api/project-milestones'
  const params = new URLSearchParams()
  if (sf.projectNo) params.set('projectNo', sf.projectNo)
  const qs = params.toString()
  if (qs) url += '?' + qs
  const res = await authFetch(url)
  const d = await res.json()
  if (d.code === 200) dataList.value = d.data || []
  else dataList.value = []
  loading.value = false
}

function showAdd() { currentId.value = null; modalTitle.value = '新建阶段'; Object.assign(form, { projectNo:'', projectName:'', stageName:'', stageOrder: dataList.value.length + 1, deliverableContent:'', plannedStartDate:null, plannedEndDate:null, actualStartDate:null, actualEndDate:null, status:'pending', progress:0, assignee:'', remark:'' }); modalVisible.value = true }

function editRecord(r: any) { currentId.value = r.id; modalTitle.value = '编辑阶段'; Object.assign(form, { ...r, plannedStartDate: r.plannedStartDate ? dayjs(r.plannedStartDate) : null, plannedEndDate: r.plannedEndDate ? dayjs(r.plannedEndDate) : null, actualStartDate: r.actualStartDate ? dayjs(r.actualStartDate) : null, actualEndDate: r.actualEndDate ? dayjs(r.actualEndDate) : null }); modalVisible.value = true }

async function handleSave() {
  saving.value = true
  const payload = { ...form }
  for (const k of ['plannedStartDate','plannedEndDate','actualStartDate','actualEndDate']) {
    if (payload[k]) payload[k] = dayjs(payload[k]).format('YYYY-MM-DD')
  }
  const url = currentId.value ? `/api/project-milestones/${currentId.value}` : '/api/project-milestones'
  const method = currentId.value ? 'PUT' : 'POST'
  const res = await authFetch(url, { method, body: JSON.stringify(payload) })
  const d = await res.json()
  if (d.code === 200) { message.success('保存成功'); modalVisible.value = false; loadData() }
  else message.error(d.msg || '保存失败')
  saving.value = false
}

function deleteRecord(r: any) {
  Modal.confirm({ title: '确认删除', content: `确定删除阶段「${r.stageName}」吗？`, onOk: async () => {
    const res = await authFetch(`/api/project-milestones/${r.id}`, { method: 'DELETE' })
    const d = await res.json()
    if (d.code === 200) { message.success('删除成功'); loadData() } else message.error(d.msg || '删除失败')
  }})
}

function sendNotify(r: any) {
  Modal.confirm({ title: '发送通知', content: `确定发送「${r.stageName}」阶段通知到所有启用渠道？`, onOk: async () => {
    const res = await authFetch('/api/project-milestones/send-notify', { method: 'POST', body: JSON.stringify({ milestoneId: r.id }) })
    const d = await res.json()
    message.success(d.msg || d.data || '发送完成')
  }})
}

// 通知配置
async function showNotifyConfig() {
  const res = await authFetch('/api/project-milestones/notify-configs')
  const d = await res.json()
  if (d.code === 200) configList.value = d.data || []
  configModalVisible.value = true
}

function showAddConfig() { configCurrentId.value = null; configEditTitle.value = '新增配置'; Object.assign(configForm, { configName:'', channelType:'feishu', webhookUrl:'', secret:'', email:'', status:'enabled' }); configEditVisible.value = true }

function editConfig(r: any) { configCurrentId.value = r.id; configEditTitle.value = '编辑配置'; Object.assign(configForm, r); configEditVisible.value = true }

async function handleSaveConfig() {
  savingConfig.value = true
  const url = configCurrentId.value ? `/api/project-milestones/notify-configs/${configCurrentId.value}` : '/api/project-milestones/notify-configs'
  const method = configCurrentId.value ? 'PUT' : 'POST'
  const res = await authFetch(url, { method, body: JSON.stringify(configForm) })
  const d = await res.json()
  if (d.code === 200) { message.success('保存成功'); configEditVisible.value = false; showNotifyConfig() }
  else message.error(d.msg || '保存失败')
  savingConfig.value = false
}

function deleteConfig(r: any) {
  Modal.confirm({ title: '确认删除', content: `确定删除配置「${r.configName}」吗？`, onOk: async () => {
    const res = await authFetch(`/api/project-milestones/notify-configs/${r.id}`, { method: 'DELETE' })
    const d = await res.json()
    if (d.code === 200) { message.success('删除成功'); showNotifyConfig() } else message.error(d.msg || '删除失败')
  }})
}
</script>
