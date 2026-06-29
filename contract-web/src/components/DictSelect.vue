<template>
  <a-select
    :value="modelValue"
    :placeholder="placeholder || '请选择'"
    :allowClear="allowClear"
    :mode="mode"
    style="min-width: 160px"
    @update:value="emit('update:modelValue', $event)"
  >
    <a-select-option v-for="item in dictItems" :key="item.value" :value="item.value">
      {{ item.label }}
    </a-select-option>
  </a-select>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { getDict } from '../api/settings'

const props = defineProps<{
  modelValue?: string | string[]
  dictType: string
  placeholder?: string
  allowClear?: boolean
  mode?: 'multiple' | 'tags' | undefined
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string | string[] | undefined]
}>()

const dictItems = ref<any[]>([])

async function loadDict() {
  if (!props.dictType) return
  try {
    const res: any = await getDict(props.dictType)
    dictItems.value = res.data || []
  } catch {
    dictItems.value = []
  }
}

watch(() => props.dictType, loadDict)
onMounted(loadDict)
</script>
