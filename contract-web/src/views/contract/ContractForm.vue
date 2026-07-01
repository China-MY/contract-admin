<template>
  <a-form :model="form" layout="vertical" @finish="handleSave">
    <a-row :gutter="24">
      <a-col :span="8">
        <a-form-item label="合同编号">
          <a-input :value="form.contractNo || genContractNo()" disabled />
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
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="我方公司" name="ourCompany">
          <SelectCreate v-model="form.ourCompany" :options="companyOptions" placeholder="搜索公司，回车创建" @create="handleCreateCompany" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="对方单位" name="counterparty">
          <SelectCreate v-model="form.counterparty" :options="customerOptions" placeholder="搜索客户，回车创建" @create="handleCreateCustomer" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="关联项目" name="projectNo">
          <SelectCreate v-model="form.projectNo" :options="projectOptions" placeholder="搜索项目，回车创建" @create="handleCreateProject" @change="onProjectChange" />
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
        <a-form-item label="合同状态" name="status">
          <a-select v-model:value="form.status">
            <a-select-option value="unconfirmed">未签订</a-select-option>
            <a-select-option value="confirmed">已签订</a-select-option>
            <a-select-option value="archived">已归档</a-select-option>
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
          <a-textarea v-model:value="form.remark" :rows="2" />
        </a-form-item>
      </a-col>
    </a-row>
    <div style="text-align:right;margin-top:16px">
      <a-button @click="$emit('cancel')">取消</a-button>
      <a-button type="primary" html-type="submit" style="margin-left:8px" :loading="saving">保存</a-button>
    </div>
  </a-form>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'
import { authFetch } from '../../utils/auth'
import SelectCreate from '../../components/SelectCreate.vue'

const props = defineProps<{ record?: any }>()
const emit = defineEmits<{ (e: 'saved'): void, (e: 'cancel'): void }>()

const saving = ref(false)
const projectOptions = ref<any[]>([])
const customerOptions = ref<any[]>([])
const companyOptions = ref<any[]>([])

const form = reactive<any>({
  contractNo: '', contractName: '', contractType: '销售合同',
  ourCompany: '', counterparty: '', projectNo: '', projectName: '',
  manager: '', pricingMethod: 'fixed', status: 'unconfirmed',
  signDate: null, endDate: null,
  contractAmount: 0, settlementAmount: 0, remark: ''
})

function genContractNo() {
  const now = new Date()
  const d = now.getFullYear() + String(now.getMonth()+1).padStart(2,'0') + String(now.getDate()).padStart(2,'0')
  return (props.record?.direction || 'receivable') === 'receivable' ? 'CT-R-' + d + '-XXX' : 'CT-P-' + d + '-XXX'
}

function onProjectChange(val: string) {
  const found = projectOptions.value.find((p: any) => p.value === val)
  if (found) form.projectName = found.label
}

async function loadOptions() {
  try {
    const res = await authFetch('/api/options')
    const d = await res.json()
    if (d.code === 200) {
      projectOptions.value = d.data.projects || []
      customerOptions.value = d.data.customers || []
    }
  } catch {}
  try {
    const res = await authFetch('/api/settings/companies')
    const d = await res.json()
    if (d.code === 200) {
      companyOptions.value = (d.data || []).map((c: any) => ({ label: c.companyName, value: c.companyName }))
    }
  } catch {}
}

async function handleCreateCompany(name: string) {
  form.ourCompany = name
}

async function handleCreateCustomer(name: string) {
  const res = await authFetch('/api/customers', { method: 'POST', body: JSON.stringify({ name, type: 'customer' }) })
  const d = await res.json()
  if (d.code === 200) {
    customerOptions.value.push({ label: name, value: name })
    form.counterparty = name
  }
}

async function handleCreateProject(name: string) {
  const res = await authFetch('/api/projects', { method: 'POST', body: JSON.stringify({ projectName: name }) })
  if (res.ok) {
    const r2 = await authFetch('/api/options')
    const d2 = await r2.json()
    if (d2.code === 200) projectOptions.value = d2.data.projects || []
    const found = projectOptions.value.find((p: any) => p.label === name)
    if (found) { form.projectNo = found.value; form.projectName = found.label }
  }
}

onMounted(() => {
  loadOptions()
  if (props.record) {
    Object.assign(form, props.record)
    if (form.signDate) form.signDate = dayjs(form.signDate)
    if (form.endDate) form.endDate = dayjs(form.endDate)
  }
})

async function handleSave() {
  saving.value = true
  try {
    const payload = { ...form }
    if (payload.signDate) payload.signDate = dayjs(payload.signDate).format('YYYY-MM-DD')
    if (payload.endDate) payload.endDate = dayjs(payload.endDate).format('YYYY-MM-DD')
    if (!payload.contractNo || payload.contractNo.endsWith('-XXX')) delete payload.contractNo
    const url = props.record ? `/api/contracts/${props.record.id}` : '/api/contracts'
    const method = props.record ? 'PUT' : 'POST'
    const res = await authFetch(url, { method, body: JSON.stringify(payload) })
    const d = await res.json()
    if (d.code === 200) { message.success('保存成功'); emit('saved') }
    else message.error(d.msg || '操作失败')
  } catch (e) {
    if (e.message !== '未认证或token已过期') message.error('保存失败: ' + (e.message || '未知错误'))
  } finally {
    saving.value = false
  }
}
</script>
