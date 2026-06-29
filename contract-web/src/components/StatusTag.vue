<template>
  <a-tag :color="colorMap[status] || 'default'">
    <slot>{{ labelMap[status] || status }}</slot>
  </a-tag>
</template>

<script setup lang="ts">
defineProps<{
  status: string
  colorMap?: Record<string, string>
  labelMap?: Record<string, string>
}>()

// Default color mappings for common statuses
const defaultColors: Record<string, string> = {
  // Receipt status
  unreceived: 'red',
  partial: 'orange',
  completed: 'green',
  // Payment status
  unpaid: 'red',
  paid: 'green',
  // Record status
  pending: 'orange',
  confirmed: 'green',
  // Contract status
  unconfirmed: 'default',
  confirmed: 'blue',
  archived: 'gray',
  // Project status
  init: 'default',
  in_progress: 'processing',
  // Record direction
  receipt: 'green',
  pay: 'red',
  // Enable/disable
  enabled: 'green',
  disabled: 'red',
  normal: 'green',
}

const defaultLabels: Record<string, string> = {
  unreceived: '未收款',
  partial: '部分收款',
  completed: '已完成',
  unpaid: '未付款',
  paid: '已付款',
  pending: '待确认',
  confirmed: '已确认',
  unconfirmed: '未签订',
  confirmed: '已签订',
  archived: '已归档',
  init: '初始',
  in_progress: '进行中',
  receipt: '收款',
  pay: '付款',
  enabled: '启用',
  disabled: '禁用',
  normal: '正常',
}
</script>
