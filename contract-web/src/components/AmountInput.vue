<template>
  <a-input-number
    :value="displayValue"
    :formatter="(value: any) => formatNumber(value)"
    :parser="(value: any) => parseNumber(value)"
    :min="min"
    :max="max"
    :precision="precision"
    :placeholder="placeholder || '请输入金额'"
    style="width: 100%"
    @update:value="handleChange"
  />
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  modelValue?: number | string
  placeholder?: string
  min?: number
  max?: number
  precision?: number
  disabled?: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: number | undefined]
}>()

const displayValue = computed(() => {
  if (props.modelValue === undefined || props.modelValue === null || props.modelValue === '') return undefined
  return Number(props.modelValue)
})

function formatNumber(value: any): string {
  if (value === undefined || value === null || value === '') return ''
  const num = Number(value)
  if (isNaN(num)) return ''
  // Add thousand separators
  const parts = num.toFixed(props.precision ?? 2).split('.')
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  return parts.join('.')
}

function parseNumber(value: any): number | undefined {
  if (!value) return undefined
  const num = Number(String(value).replace(/,/g, ''))
  return isNaN(num) ? undefined : num
}

function handleChange(value: number | null) {
  emit('update:modelValue', value === null ? undefined : value)
}
</script>
