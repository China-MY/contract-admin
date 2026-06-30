<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">项目提醒配置</span></template>
    <a-form :model="form" layout="vertical" @finish="handleSave" style="max-width:600px">
      <a-divider>提醒触发规则</a-divider>
      <a-form-item label="提前提醒天数" name="remind_days_before">
        <a-input-number v-model:value="form.remind_days_before" :min="1" :max="30" style="width:120px" />
        <span style="margin-left:8px;color:#666">天（计划日期前N天发送提醒）</span>
      </a-form-item>
      <a-form-item label="启用提醒类型">
        <a-checkbox v-model:checked="form.enable_before_start">阶段开始前提醒</a-checkbox>
        <a-checkbox v-model:checked="form.enable_before_end" style="margin-left:16px">阶段到期前提醒</a-checkbox>
        <br/>
        <a-checkbox v-model:checked="form.enable_on_end" style="margin-top:8px">阶段到期日提醒</a-checkbox>
        <a-checkbox v-model:checked="form.enable_overdue" style="margin-left:16px;margin-top:8px">阶段超期提醒</a-checkbox>
      </a-form-item>
      <a-divider>发送设置</a-divider>
      <a-form-item label="提醒时间">
        <span>每天 8:00 自动执行（固定时间）</span>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit" :loading="saving">保存设置</a-button>
      </a-form-item>
    </a-form>
  </a-card>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { authFetch } from '../../utils/auth'

const saving = ref(false)
const form = reactive({
  remind_days_before: 3,
  enable_before_start: true,
  enable_before_end: true,
  enable_on_end: true,
  enable_overdue: true,
})

onMounted(loadConfig)

async function loadConfig() {
  try {
    const res = await authFetch('/api/settings/config')
    const d = await res.json()
    if (d.code === 200 && d.data) {
      const items = d.data
      const map: Record<string, string> = {}
      for (const item of items) map[item.configKey] = item.configValue

      if (map.remind_days_before) form.remind_days_before = parseInt(map.remind_days_before) || 3
      if (map.enable_before_start) form.enable_before_start = map.enable_before_start === 'enabled'
      if (map.enable_before_end) form.enable_before_end = map.enable_before_end === 'enabled'
      if (map.enable_on_end) form.enable_on_end = map.enable_on_end === 'enabled'
      if (map.enable_overdue) form.enable_overdue = map.enable_overdue === 'enabled'
    }
  } catch {}
}

async function handleSave() {
  saving.value = true
  const configs = [
    { configKey: 'remind_days_before', configValue: String(form.remind_days_before), description: '提前提醒天数' },
    { configKey: 'enable_before_start', configValue: form.enable_before_start ? 'enabled' : 'disabled', description: '阶段开始前提醒' },
    { configKey: 'enable_before_end', configValue: form.enable_before_end ? 'enabled' : 'disabled', description: '阶段到期前提醒' },
    { configKey: 'enable_on_end', configValue: form.enable_on_end ? 'enabled' : 'disabled', description: '阶段到期日提醒' },
    { configKey: 'enable_overdue', configValue: form.enable_overdue ? 'enabled' : 'disabled', description: '阶段超期提醒' },
  ]
  try {
    const res = await authFetch('/api/settings/config', { method: 'PUT', body: JSON.stringify(configs) })
    const d = await res.json()
    if (d.code === 200) message.success('保存成功')
    else message.error(d.msg || '保存失败')
  } catch { message.error('保存失败') }
  saving.value = false
}
</script>
