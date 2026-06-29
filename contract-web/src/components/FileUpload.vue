<template>
  <a-upload
    :file-list="fileList"
    :action="action"
    :headers="headers"
    :before-upload="beforeUpload"
    @change="handleChange"
    @remove="handleRemove"
  >
    <a-button>
      <upload-outlined />
      上传附件
    </a-button>
    <template #itemRender="{ file, actions }">
      <span class="file-item">
        <paper-clip-outlined class="file-icon" />
        <a :href="file.url" target="_blank" v-if="file.url">{{ file.name }}</a>
        <span v-else>{{ file.name }}</span>
        <delete-outlined class="delete-btn" @click="actions.remove" />
      </span>
    </template>
  </a-upload>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { message } from 'ant-design-vue'
import { UploadOutlined, PaperClipOutlined, DeleteOutlined } from '@ant-design/icons-vue'

const props = defineProps<{
  action?: string
  maxSize?: number // in MB
  accept?: string
}>()

const emit = defineEmits<{
  change: [fileList: any[]]
}>()

const fileList = ref<any[]>([])

const token = localStorage.getItem('token') || ''
const headers = { Authorization: `Bearer ${token}` }

function beforeUpload(file: File) {
  const maxSize = props.maxSize || 10
  if (file.size > maxSize * 1024 * 1024) {
    message.error(`文件大小不能超过 ${maxSize}MB`)
    return false
  }
  return true
}

function handleChange(info: any) {
  fileList.value = info.fileList
  emit('change', fileList.value)
}

function handleRemove() {
  // handled by template's itemRender
}
</script>

<style scoped>
.file-item { display: inline-flex; align-items: center; gap: 6px; }
.file-icon { color: #1890ff; }
.delete-btn { color: #ff4d4f; cursor: pointer; }
</style>
