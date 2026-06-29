<template>
  <a-select
    :value="modelValue"
    :options="mergedOptions"
    :placeholder="placeholder"
    showSearch
    allowClear
    filterOption
    @change="onChange"
    @search="onSearch"
  />
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  modelValue: string
  options: any[]
  placeholder?: string
}>()
const emit = defineEmits<{
  (e: 'update:modelValue', val: string): void
  (e: 'create', name: string): void
  (e: 'change', val: string): void
}>()

const searchText = ref('')

const mergedOptions = computed(() => {
  const opts = props.options || []
  if (!searchText.value) return opts
  // 检查搜索文本是否已存在于选项中（精确匹配或模糊匹配）
  const exactMatch = opts.some(
    (o: any) => {
      const label = o.label || o.value || o
      return String(label) === searchText.value
    }
  )
  if (!exactMatch) {
    return [...opts, { label: `➕ 创建「${searchText.value}」↲`, value: '__CREATE__' }]
  }
  // 有精确匹配时自动选中
  const matched = opts.find((o: any) => {
    const label = o.label || o.value || o
    return String(label) === searchText.value
  })
  if (matched && props.modelValue !== searchText.value) {
    const val = matched.value !== undefined ? matched.value : matched.label !== undefined ? matched.label : matched
    setTimeout(() => { emit('update:modelValue', val); emit('change', val) }, 0)
  }
  return opts
})

function onSearch(val: string) {
  searchText.value = val
}

function onChange(val: string) {
  if (val === '__CREATE__') {
    const name = searchText.value || ''
    if (name && name.trim()) {
      emit('create', name.trim())
      emit('update:modelValue', name.trim())
      emit('change', name.trim())
    }
    // 清空搜索文本让组件显示新值
    searchText.value = ''
    return
  }
  emit('update:modelValue', val)
  emit('change', val)
  searchText.value = ''
}
</script>
