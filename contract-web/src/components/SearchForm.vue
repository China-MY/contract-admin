<template>
  <a-form :layout="layout" :model="formModel">
    <a-row :gutter="16">
      <a-col v-for="field in fields" :key="field.key" :span="field.span || 6">
        <a-form-item :label="field.label">
          <!-- Input -->
          <a-input
            v-if="field.type === 'input'"
            :placeholder="field.placeholder || '请输入'"
            v-model:value="formModel[field.key]"
            :allow-clear="true"
            @press-enter="emit('search', { ...formModel })"
          />
          <!-- Select -->
          <a-select
            v-else-if="field.type === 'select'"
            :placeholder="field.placeholder || '请选择'"
            v-model:value="formModel[field.key]"
            :allow-clear="true"
            style="width: 100%"
          >
            <a-select-option v-for="opt in field.options || []" :key="opt.value" :value="opt.value">
              {{ opt.label }}
            </a-select-option>
          </a-select>
          <!-- DatePicker -->
          <a-date-picker
            v-else-if="field.type === 'date'"
            v-model:value="formModel[field.key]"
            :placeholder="field.placeholder || '选择日期'"
            style="width: 100%"
          />
          <!-- DateRange -->
          <a-range-picker
            v-else-if="field.type === 'dateRange'"
            v-model:value="formModel[field.key]"
            :placeholder="[field.startPlaceholder || '开始日期', field.endPlaceholder || '结束日期']"
            style="width: 100%"
          />
        </a-form-item>
      </a-col>
      <a-col :span="actionSpan">
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="emit('search', { ...formModel })">查询</a-button>
            <a-button @click="handleReset">重置</a-button>
          </a-space>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>

<script setup lang="ts">
import { reactive } from 'vue'

interface SearchField {
  key: string
  label: string
  type: 'input' | 'select' | 'date' | 'dateRange'
  placeholder?: string
  startPlaceholder?: string
  endPlaceholder?: string
  span?: number
  options?: { label: string; value: any }[]
}

const props = defineProps<{
  fields: SearchField[]
  layout?: 'horizontal' | 'vertical' | 'inline'
}>()

const emit = defineEmits<{
  search: [values: Record<string, any>]
}>()

const initialValues: Record<string, any> = {}
props.fields.forEach((f) => {
  initialValues[f.key] = f.type === 'dateRange' ? [] : undefined
})

const formModel = reactive({ ...initialValues })
const actionSpan = 24 - props.fields.reduce((s, f) => s + (f.span || 6), 0)

function handleReset() {
  Object.keys(initialValues).forEach((k) => {
    formModel[k] = Array.isArray(initialValues[k]) ? [] : undefined
  })
  emit('search', {})
}
</script>
