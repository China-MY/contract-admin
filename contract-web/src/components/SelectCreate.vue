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
    @keydown.enter.prevent="onEnter"
    ref="selectRef"
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
  if (!searchText.value) return props.options || []
  const exists = (props.options || []).some(
    (o: any) => (o.label || o.value || o).toLowerCase().includes(searchText.value.toLowerCase())
  )
  if (!exists) {
    return [...(props.options || []), { label: `➕ 创建「${searchText.value}」↲`, value: '__CREATE__', searchText: searchText.value }]
  }
  return props.options || []
})

function onSearch(val: string) {
  searchText.value = val
}

function onChange(val: string) {
  if (val === '__CREATE__') {
    const name = searchText.value
    if (name && name.trim()) {
      emit('create', name.trim())
      emit('update:modelValue', name.trim())
      emit('change', name.trim())
    }
    // Reset the select value to keep showing the typed text
    searchText.value = name || ''
    return
  }
  emit('update:modelValue', val)
  emit('change', val)
  searchText.value = ''
}

function onEnter() {
  const text = searchText.value
  if (!text || !text.trim()) return
  const exists = (props.options || []).some(
    (o: any) => (o.label || o.value || o) === text
  )
  if (!exists) {
    emit('create', text.trim())
    emit('update:modelValue', text.trim())
    emit('change', text.trim())
    searchText.value = ''
  }
}
</script>
