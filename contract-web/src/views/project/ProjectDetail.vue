<template>
  <div>
    <a-button type="link" @click="$router.push('/project/list')" style="margin-bottom:8px">← 返回项目列表</a-button>

    <!-- 项目信息 -->
    <a-card v-if="project" style="margin-bottom:16px">
      <a-row :gutter="24">
        <a-col :span="6"><b>项目编号：</b>{{ project.projectNo }}</a-col>
        <a-col :span="6"><b>项目名称：</b>{{ project.projectName }}</a-col>
        <a-col :span="4"><b>项目类型：</b>{{ project.projectType || '-' }}</a-col>
        <a-col :span="4"><b>负责人：</b>{{ project.manager || '-' }}</a-col>
        <a-col :span="4"><b>项目进度：</b>
          <a-progress :percent="progress.percent" :status="progress.percent >= 100 ? 'success' : 'active'" style="width:160px;display:inline-block" />
        </a-col>
      </a-row>
      <a-row :gutter="24" style="margin-top:8px">
        <a-col :span="6"><b>客户：</b>{{ project.customerName || '-' }}</a-col>
        <a-col :span="6"><b>预算金额：</b>¥{{ (project.budgetAmount || 0).toLocaleString() }}</a-col>
        <a-col :span="4"><b>阶段数：</b>{{ progress.total }}</a-col>
        <a-col :span="4"><b>已完成：</b>{{ progress.completed }}</a-col>
        <a-col :span="4"><b>进行中：</b>{{ progress.inProgress }}</a-col>
      </a-row>
    </a-card>

    <!-- 阶段管理 -->
    <a-card>
      <template #title>
        <span style="font-size:16px;font-weight:bold">阶段管理</span>
        <a-button type="primary" size="small" style="float:right" @click="showAdd">新建阶段</a-button>
      </template>

      <!-- 时间线 -->
      <a-timeline v-if="stages.length > 0">
        <a-timeline-item v-for="s in stages" :key="s.id" :color="s.status==='completed'?'green':s.status==='in_progress'?'blue':s.status==='delayed'?'red':'gray'">
          <template #dot v-if="s.status==='completed'"><CheckCircleOutlined style="color:#52c41a" /></template>
          <template #dot v-else-if="s.status==='in_progress'"><LoadingOutlined style="color:#1890ff" /></template>
          <template #dot v-else-if="s.status==='delayed'"><CloseCircleOutlined style="color:#ff4d4f" /></template>

          <div style="display:flex;justify-content:space-between;align-items:flex-start">
            <div style="flex:1">
              <strong>阶段 {{ s.stageOrder }}：{{ s.stageName }}</strong>
              <a-tag :color="s.status==='completed'?'green':s.status==='in_progress'?'blue':s.status==='delayed'?'red':'default'" style="margin-left:8px">
                {{s.status==='pending'?'未开始':s.status==='in_progress'?'进行中':s.status==='completed'?'已完成':'已延期'}}
              </a-tag>
              <div style="color:#666;font-size:13px;margin-top:4px">
                <template v-if="s.plannedStartDate">📅 {{ s.plannedStartDate }}</template>
                <template v-if="s.plannedEndDate"> → {{ s.plannedEndDate }}</template>
                <template v-if="s.assignee"> | 👤 {{ s.assignee }}</template>
              </div>
              <div v-if="s.deliverableContent" style="margin-top:4px;padding:6px 10px;background:#fafafa;border-radius:4px;border-left:3px solid #1890ff;color:#333">
                📋 {{ s.deliverableContent }}
              </div>
              <!-- 阶段进度由状态自动计算 -->
            </div>
            <div style="white-space:nowrap;margin-left:12px">
              <a-button type="link" size="small" @click="editStage(s)">编辑</a-button>
              <a-button type="link" size="small" @click="sendNotify(s)">通知</a-button>
              <a-button type="link" size="small" danger @click="deleteStage(s)">删除</a-button>
            </div>
          </div>
        </a-timeline-item>
      </a-timeline>
      <a-empty v-else description="暂无阶段，请点击上方「新建阶段」添加" />
    </a-card>

    <!-- 文档管理 -->
    <a-card style="margin-top:16px">
      <template #title>
        <span style="font-size:16px;font-weight:bold">文档管理</span>
      </template>
      <div style="margin-bottom:12px">
        <a-upload
          :action="`/api/files/upload?projectNo=${project?.projectNo || ''}&projectName=${project?.projectName || ''}`"
          :headers="uploadHeaders"
          :showUploadList="true"
          @change="onUploadChange"
          :beforeUpload="beforeUpload"
        >
          <a-button type="primary">
            <UploadOutlined /> 上传文件
          </a-button>
        </a-upload>
      </div>
      <a-table :dataSource="documents" :columns="docColumns" rowKey="id" size="small" bordered :pagination="false" :loading="docLoading">
        <template #bodyCell="{column,record}">
          <template v-if="column.key==='fileSize'">{{ formatSize(record.fileSize) }}</template>
          <template v-else-if="column.key==='uploadTime'">{{ record.uploadTime?.substring(0,10) }}</template>
          <template v-else-if="column.key==='action'">
            <a-button type="link" size="small" @click="downloadFile(record)">下载</a-button>
            <a-popconfirm title="确定删除该文件吗？" @confirm="deleteFile(record)" okText="确定" cancelText="取消">
              <a-button type="link" size="small" danger>删除</a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 阶段编辑弹窗 -->
    <a-modal v-model:open="modalVisible" :title="modalTitle" width="60%" :footer="null" destroyOnClose>
      <a-form v-if="modalVisible" :model="form" layout="vertical" @finish="handleSave">
        <a-row :gutter="24">
          <a-col :span="12"><a-form-item label="阶段名称" :rules="[{required:true}]"><a-input v-model:value="form.stageName" /></a-form-item></a-col>
          <a-col :span="6"><a-form-item label="排序"><a-input-number v-model:value="form.stageOrder" style="width:100%" :min="1" /></a-form-item></a-col>
          <a-col :span="6"><a-form-item label="负责人"><a-input v-model:value="form.assignee" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="计划开始"><a-date-picker v-model:value="form.plannedStartDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="计划结束"><a-date-picker v-model:value="form.plannedEndDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="8"><a-form-item label="状态"><a-select v-model:value="form.status">
            <a-select-option value="pending">未开始</a-select-option>
            <a-select-option value="in_progress">进行中</a-select-option>
            <a-select-option value="completed">已完成</a-select-option>
            <a-select-option value="delayed">已延期</a-select-option>
          </a-select></a-form-item></a-col>
          <a-col :span="12"><a-form-item label="实际开始"><a-date-picker v-model:value="form.actualStartDate" style="width:100%" /></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="交付内容"><a-textarea v-model:value="form.deliverableContent" :rows="3" placeholder="描述该阶段需要交付的内容" /></a-form-item></a-col>
          <a-col :span="24"><a-form-item label="备注"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
        </a-row>
        <div style="text-align:right;margin-top:16px">
          <a-button @click="modalVisible=false">取消</a-button>
          <a-button type="primary" html-type="submit" :loading="saving">保存</a-button>
        </div>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { message, Modal } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'
import { CheckCircleOutlined, LoadingOutlined, CloseCircleOutlined, UploadOutlined } from '@ant-design/icons-vue'
import dayjs from 'dayjs'

const route = useRoute()
const project = ref<any>(null)
const stages = ref<any[]>([])
const progress = reactive({ total: 0, completed: 0, inProgress: 0, percent: 0 })

// 文档管理
const documents = ref<any[]>([])
const docLoading = ref(false)
const docColumns = [
  { title:'文件名', dataIndex:'originalName', key:'originalName', width:300 },
  { title:'文件大小', key:'fileSize', width:100 },
  { title:'文件类型', dataIndex:'fileType', key:'fileType', width:100 },
  { title:'上传时间', key:'uploadTime', width:100 },
  { title:'备注', dataIndex:'remark', key:'remark', width:150 },
  { title:'操作', key:'action', width:120, fixed:'right' as const },
]
const uploadHeaders = computed(() => ({ Authorization: 'Bearer ' + (typeof window !== 'undefined' ? localStorage.getItem('token') || '' : '') }))

const modalVisible = ref(false)
const modalTitle = ref('')
const currentId = ref<number|null>(null)
const saving = ref(false)
const form = reactive<any>({
  projectNo: '', projectName: '', stageName: '', stageOrder: 1, assignee: '',
  plannedStartDate: null, plannedEndDate: null, actualStartDate: null, actualEndDate: null,
  status: 'pending', deliverableContent: '', remark: ''
})

onMounted(async () => {
  const id = route.params.id
  if (!id) { message.error('缺少项目ID'); return }

  // 获取项目信息
  try {
    const r = await authFetch(`/api/projects?page=1&size=100`)
    const d = await r.json()
    if (d.code === 200) {
      const list = d.data?.records || d.data || []
      project.value = list.find((p: any) => String(p.id) === String(id))
    }
  } catch {}

  if (project.value) {
    loadStages()
    loadDocuments()
  }
})

async function loadStages() {
  if (!project.value?.projectNo) return
  try {
    const r = await authFetch(`/api/project-milestones/progress?projectNo=${project.value.projectNo}`)
    const d = await r.json()
    if (d.code === 200) {
      stages.value = d.data.stages || []
      progress.total = d.data.total
      progress.completed = d.data.completed
      progress.inProgress = d.data.inProgress
      progress.percent = d.data.percent
    }
  } catch {}
}

// 文档管理
async function loadDocuments() {
  if (!project.value?.projectNo) return
  docLoading.value = true
  try {
    const r = await authFetch(`/api/files/project/${project.value.projectNo}`)
    const d = await r.json()
    if (d.code === 200) documents.value = d.data || []
  } catch {} finally { docLoading.value = false }
}

function onUploadChange(info: any) {
  if (info.file.status === 'done') {
    if (info.file.response?.code === 200) {
      message.success('上传成功')
      loadDocuments()
    } else {
      message.error(info.file.response?.msg || '上传失败')
    }
  } else if (info.file.status === 'error') {
    message.error('上传失败')
  }
}

function beforeUpload(file: any) {
  const maxSize = 50 * 1024 * 1024 // 50MB
  if (file.size > maxSize) {
    message.error('文件大小不能超过 50MB')
    return false
  }
  return true
}

function formatSize(bytes: number) {
  if (!bytes) return '-'
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
}

async function downloadFile(record: any) {
  try {
    const r = await authFetch(`/api/files/${record.id}/download`)
    if (!r.ok) { message.error('下载失败'); return }
    const blob = await r.blob()
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = record.originalName || record.fileName
    document.body.appendChild(a)
    a.click()
    URL.revokeObjectURL(url)
    document.body.removeChild(a)
  } catch { message.error('下载失败') }
}

async function deleteFile(record: any) {
  try {
    const r = await authFetch(`/api/files/${record.id}`, { method: 'DELETE' })
    const d = await r.json()
    if (d.code === 200) { message.success('删除成功'); loadDocuments() }
    else message.error(d.msg || '删除失败')
  } catch {}
}

// 在项目加载后也加载文档

function showAdd() {
  currentId.value = null
  modalTitle.value = '新建阶段'
  Object.assign(form, {
    projectNo: project.value?.projectNo || '', projectName: project.value?.projectName || '',
    stageName: '', stageOrder: stages.value.length + 1, assignee: '',
    plannedStartDate: null, plannedEndDate: null, actualStartDate: null, actualEndDate: null,
    status: 'pending', deliverableContent: '', remark: ''
  })
  modalVisible.value = true
}

function editStage(s: any) {
  currentId.value = s.id
  modalTitle.value = '编辑阶段'
  Object.assign(form, {
    ...s,
    plannedStartDate: s.plannedStartDate ? dayjs(s.plannedStartDate) : null,
    plannedEndDate: s.plannedEndDate ? dayjs(s.plannedEndDate) : null,
    actualStartDate: s.actualStartDate ? dayjs(s.actualStartDate) : null,
    actualEndDate: s.actualEndDate ? dayjs(s.actualEndDate) : null,
  })
  modalVisible.value = true
}

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
  if (d.code === 200) { message.success('保存成功'); modalVisible.value = false; loadStages() }
  else message.error(d.msg || '保存失败')
  saving.value = false
}

function deleteStage(s: any) {
  Modal.confirm({
    title: '确认删除', content: `确定删除阶段「${s.stageName}」吗？`,
    onOk: async () => {
      const res = await authFetch(`/api/project-milestones/${s.id}`, { method: 'DELETE' })
      const d = await res.json()
      if (d.code === 200) { message.success('删除成功'); loadStages() }
      else message.error(d.msg || '删除失败')
    }
  })
}

function sendNotify(s: any) {
  Modal.confirm({
    title: '发送通知', content: `发送「${s.stageName}」通知到所有启用渠道？`,
    onOk: async () => {
      const res = await authFetch('/api/project-milestones/send-notify', { method: 'POST', body: JSON.stringify({ milestoneId: s.id }) })
      const d = await res.json()
      message.success(d.msg || d.data || '发送完成')
    }
  })
}
</script>
