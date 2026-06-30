<template>
  <a-tag :color="colorMap[status] || 'default'">
    <slot>{{ labelMap[status] || status }}</slot>
  </a-tag>
</template>

<script setup lang="ts">
const props = withDefaults(defineProps<{
  status: string
  colorMap?: Record<string, string>
  labelMap?: Record<string, string>
}>(), {
  colorMap: () => ({
    unreceived: 'red', partial: 'orange', completed: 'green',
    unpaid: 'red', paid: 'green',
    pending: 'orange', confirmed: 'green',
    unconfirmed: 'default', archived: 'gray',
    init: 'default', in_progress: 'processing',
    enabled: 'green', disabled: 'red', normal: 'green',
  }),
  labelMap: () => ({
    unreceived: '未收款', partial: '部分收款', completed: '已完成',
    unpaid: '未付款', paid: '已付款',
    pending: '待确认', confirmed: '已确认',
    unconfirmed: '未签订', archived: '已归档',
    init: '初始', in_progress: '进行中',
    enabled: '启用', disabled: '禁用', normal: '正常',
  }),
})
</script>
