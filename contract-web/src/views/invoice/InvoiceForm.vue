<template>
  <a-form :model="form" layout="vertical" @finish="handleSave">
    <a-row :gutter="24">
      <a-col :span="8">
        <a-form-item label="发票号码">
          <a-input :value="form.invoiceNo || genInvoiceNo()" disabled />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="关联合同" name="contractNo">
          <SelectCreate v-model="form.contractNo" :options="contractOptions" placeholder="搜索合同，回车创建" @create="handleCreateContract" @change="onContractChange" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="发票类型" name="type">
          <SelectCreate v-model="form.type" :options="invoiceTypeOptions" placeholder="搜索或新建" @create="handleCreateType" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="含税金额" name="amountWithTax">
          <a-input-number v-model:value="form.amountWithTax" style="width:100%" :min="0" :precision="2" @change="calcTax" />
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item label="税率(%)" name="taxRate">
          <a-select v-model:value="form.taxRate" @change="calcTax">
            <a-select-option :value="0.13">13%</a-select-option><a-select-option :value="0.09">9%</a-select-option>
            <a-select-option :value="0.06">6%</a-select-option><a-select-option :value="0.03">3%</a-select-option>
            <a-select-option :value="0.01">1%</a-select-option><a-select-option :value="0">0%</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :span="8"><a-form-item label="不含税额"><a-input :value="(form.amountWithoutTax||0).toFixed(2)" disabled /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="税额"><a-input :value="(form.taxAmount||0).toFixed(2)" disabled /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="开票日期" name="issueDate"><a-date-picker v-model:value="form.issueDate" style="width:100%" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="开票方" name="issuer"><SelectCreate v-model="form.issuer" :options="companyOptions" placeholder="搜索或新建" @create="(n:string)=>form.issuer=n" /></a-form-item></a-col>
      <a-col :span="8"><a-form-item label="收票方" name="receiver"><SelectCreate v-model="form.receiver" :options="customerOptions" placeholder="搜索或新建" @create="(n:string)=>form.receiver=n" /></a-form-item></a-col>
      <a-col :span="24"><a-form-item label="备注" name="remark"><a-textarea v-model:value="form.remark" :rows="2" /></a-form-item></a-col>
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
const contractOptions = ref<any[]>([])
const invoiceTypeOptions = ref<any[]>([])
const companyOptions = ref<any[]>([])
const customerOptions = ref<any[]>([])

const form = reactive<any>({
  contractNo:'', contractName:'', invoiceNo:'', type:'专用发票',
  amountWithTax:0, amountWithoutTax:0, taxRate:0.01, taxAmount:0,
  issueDate:null, issuer:'', issuerTaxId:'', receiver:'', receiverTaxId:'', remark:''
})

function genInvoiceNo() {
  const now = new Date()
  const d = now.getFullYear() + String(now.getMonth()+1).padStart(2,'0') + String(now.getDate()).padStart(2,'0')
  return 'INV-' + d + '-XXX'
}

function calcTax() {
  const rate = Number(form.taxRate) || 0
  const total = Number(form.amountWithTax) || 0
  form.amountWithoutTax = total > 0 ? Math.round(total / (1 + rate) * 100) / 100 : 0
  form.taxAmount = total > 0 ? Math.round((total - form.amountWithoutTax) * 100) / 100 : 0
}

function onContractChange(val: string) {
  const found = contractOptions.value.find((c: any) => c.value === val)
  if (found) form.contractName = found.name
}

onMounted(async () => {
  try {
    const [res1, res2, res3] = await Promise.all([
      authFetch('/api/options'),
      authFetch('/api/settings/dict?type=invoice_type'),
      authFetch('/api/settings/companies')
    ])
    const d1 = await res1.json()
    if (d1.code === 200) {
      contractOptions.value = d1.data.contracts || []
      customerOptions.value = (d1.data.customers || []).map((x:any) => ({ label: x.label || x, value: x.label || x }))
    }
    const d2 = await res2.json()
    if (d2.code === 200) invoiceTypeOptions.value = (d2.data || []).map((x:any) => ({ label: x.label, value: x.label }))
    const d3 = await res3.json()
    if (d3.code === 200) companyOptions.value = (d3.data || []).map((x:any) => ({ label: x.companyName, value: x.companyName }))
  } catch {}
  if (props.record) { Object.assign(form, props.record); if (form.issueDate) form.issueDate = dayjs(form.issueDate) }
})

async function handleCreateType(name: string) {
  form.type = name
  invoiceTypeOptions.value.push({ label: name, value: name })
}

async function handleCreateContract(name: string) {
  const res = await authFetch('/api/contracts', { method:'POST', body:JSON.stringify({ contractName:name, direction:props.record?.direction || 'output' }) })
  const d = await res.json()
  if (d.code === 200) {
    const r2 = await authFetch('/api/options'); const d2 = await r2.json()
    if (d2.code === 200) contractOptions.value = d2.data.contracts || []
    message.success('已创建合同：' + name)
  } else message.error(d.msg || '创建失败')
}

async function handleSave() {
  saving.value = true; const payload = { ...form }
  if (payload.issueDate) payload.issueDate = dayjs(payload.issueDate).format('YYYY-MM-DD')
  if (!payload.invoiceNo || payload.invoiceNo.endsWith('-XXX')) delete payload.invoiceNo
  const url = props.record ? `/api/invoices/${props.record.id}` : '/api/invoices'
  const method = props.record ? 'PUT' : 'POST'
  const res = await authFetch(url, { method, headers:{'Content-Type':'application/json'}, body:JSON.stringify(payload) })
  const d = await res.json()
  if (d.code === 200) { message.success('保存成功'); emit('saved') }
  else message.error(d.msg || '操作失败')
  saving.value = false
}
</script>
