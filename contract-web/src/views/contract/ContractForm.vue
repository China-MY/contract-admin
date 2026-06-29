<template>
  <a-form :model="form" layout="vertical" @finish="handleSave">
    <a-row :gutter="24">
      <a-col :span="8">
        <a-form-item label="合同编号" name="contractNo" :rules="[{ required: true, message: '请输入合同编号' }]">
          <a-input v-model:value="form.contractNo" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="合同名称" name="contractName" :rules="[{ required: true, message: '请输入合同名称' }]">
          <a-input v-model:value="form.contractName" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="合同类型" name="contractType">
          <a-select v-model:value="form.contractType">
            <a-select-option value="销售合同">销售合同</a-select-option>
            <a-select-option value="采购合同">采购合同</a-select-option>
            <a-select-option value="服务合同">服务合同</a-select-option>
            <a-select-option value="北斗续费">北斗续费</a-select-option>
            <a-select-option value="AI智能体实施">AI智能体实施</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="我方公司" name="ourCompany">
          <a-input v-model:value="form.ourCompany" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="对方单位" name="counterparty">
          <a-input v-model:value="form.counterparty" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="项目编号" name="projectNo">
          <a-input v-model:value="form.projectNo" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="项目名称" name="projectName">
          <a-input v-model:value="form.projectName" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="合同负责人" name="manager">
          <a-input v-model:value="form.manager" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="计价方式" name="pricingMethod">
          <a-select v-model:value="form.pricingMethod">
            <a-select-option value="fixed">固定总价</a-select-option>
            <a-select-option value="actual">按实结算</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="签订日期" name="signDate">
          <a-date-picker v-model:value="form.signDate" style="width:100%" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="结束日期" name="endDate">
          <a-date-picker v-model:value="form.endDate" style="width:100%" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="合同状态" name="status">
          <a-select v-model:value="form.status">
            <a-select-option value="unconfirmed">未签订</a-select-option>
            <a-select-option value="confirmed">已签订</a-select-option>
            <a-select-option value="archived">已归档</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="合同金额" name="contractAmount">
          <a-input-number v-model:value="form.contractAmount" style="width:100%" :min="0" :precision="2" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="结算金额" name="settlementAmount">
          <a-input-number v-model:value="form.settlementAmount" style="width:100%" :min="0" :precision="2" />
        </a-form-item>
      </a-col>
      <a-col :span="24">
        <a-form-item label="备注" name="remark">
          <a-textarea v-model:value="form.remark" :rows="3" />
        </a-form-item>
      </a-col>
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
  contractNo: '', contractName: '', contractType: '销售合同',
  ourCompany: '', counterparty: '', projectNo: '', projectName: '',
  manager: '', pricingMethod: 'fixed', status: 'unconfirmed',
  signDate: null, endDate: null,
  contractAmount: 0, settlementAmount: 0, remark: ''
})

onMounted(() => {
  if (props.record) {
    Object.assign(form, props.record)
    if (form.signDate) form.signDate = dayjs(form.signDate)
    if (form.endDate) form.endDate = dayjs(form.endDate)
  }
})

async function handleCancel() { emit('saved') }

async function handleSave() {
  saving.value = true
  const payload = { ...form }
  if (payload.signDate) payload.signDate = dayjs(payload.signDate).format('YYYY-MM-DD')
  if (payload.endDate) payload.endDate = dayjs(payload.endDate).format('YYYY-MM-DD')

  const url = props.record ? `/api/contracts/${props.record.id}` : '/api/contracts'
  const method = props.record ? 'PUT' : 'POST'
  const res = await fetch(url, { method, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) })
  const data = await res.json()
  if (data.code === 200) {
    message.success(props.record ? '保存成功' : '新建成功')
    emit('saved')
  } else {
    message.error(data.msg || '操作失败')
  }
  saving.value = false
}
</script>
