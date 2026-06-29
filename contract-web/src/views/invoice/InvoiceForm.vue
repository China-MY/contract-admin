<template>
  <a-form :model="form" layout="vertical" @finish="handleSave">
    <a-row :gutter="24">
      <a-col :span="8"><a-form-item label="合同编号" name="contractNo"><a-input v-model:value="form.contractNo" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="合同名称" name="contractName"><a-input v-model:value="form.contractName" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="发票号码" name="invoiceNo" :rules="[{ required: true, message:'请输入发票号码' }]"><a-input v-model:value="form.invoiceNo" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="发票类型" name="type"><a-select v-model:value="form.type"><a-select-option value="专用发票">专用发票</a-select-option></a-select></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="含税金额" name="amountWithTax"><a-input-number v-model:value="form.amountWithTax" style="width:100%" :min="0" :precision="2" @change="calcTax" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="税率(%)" name="taxRate"><a-select v-model:value="form.taxRate" @change="calcTax"><a-select-option :value="0.13">13%</a-select-option><a-select-option :value="0.09">9%</a-select-option><a-select-option :value="0.06">6%</a-select-option><a-select-option :value="0.03">3%</a-select-option><a-select-option :value="0.01">1%</a-select-option><a-select-option :value="0">0%</a-select-option></a-select></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="不含税额"><a-input :value="(form.amountWithoutTax||0).toFixed(2)" disabled /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="税额"><a-input :value="(form.taxAmount||0).toFixed(2)" disabled /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="开票日期" name="issueDate"><a-date-picker v-model:value="form.issueDate" style="width:100%" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="开票方" name="issuer"><a-input v-model:value="form.issuer" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="开票方税号" name="issuerTaxId"><a-input v-model:value="form.issuerTaxId" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="收票方" name="receiver"><a-input v-model:value="form.receiver" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="收票方税号" name="receiverTaxId"><a-input v-model:value="form.receiverTaxId" /></a-form-item></a-col>
      <a-col :span="24"><a-form-item label="备注" name="remark"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
    </a-row>
    <div style="text-align:right;margin-top:16px">
      <a-button @click="handleCancel">取消</a-button>
      <a-button type="primary" html-type="submit" style="margin-left:8px" :loading="saving">保存</a-button>
    </div>
  </a-form>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'

const props = defineProps<{ record?: any }>()
const emit = defineEmits<{ (e: 'saved'): void }>()
const saving = ref(false)

const form = reactive<any>({
  contractNo:'', contractName:'', invoiceNo:'', type:'专用发票',
  amountWithTax:0, amountWithoutTax:0, taxRate:0.01, taxAmount:0,
  issueDate:null, issuer:'', issuerTaxId:'', receiver:'', receiverTaxId:'', remark:''
})

function handleCancel() { emit('saved') }

function calcTax() {
  const rate = Number(form.taxRate) || 0
  const total = Number(form.amountWithTax) || 0
  form.amountWithoutTax = Math.round(total / (1 + rate) * 100) / 100
  form.taxAmount = Math.round((total - form.amountWithoutTax) * 100) / 100
}

onMounted(() => {
  if (props.record) {
    Object.assign(form, props.record)
    if (form.issueDate) form.issueDate = dayjs(form.issueDate)
  }
})

async function handleSave() {
  saving.value = true
  const payload = { ...form }
  if (payload.issueDate) payload.issueDate = dayjs(payload.issueDate).format('YYYY-MM-DD')
  const url = props.record ? `/api/invoices/${props.record.id}` : '/api/invoices'
  const method = props.record ? 'PUT' : 'POST'
  const res = await fetch(url, { method, headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload) })
  const data = await res.json()
  if (data.code === 200) { message.success('保存成功'); emit('saved') }
  else message.error(data.msg || '操作失败')
  saving.value = false
}
</script>
