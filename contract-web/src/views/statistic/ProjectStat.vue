<template>
  <a-card><template #title><span style="font-size:18px;font-weight:bold">项目统计</span></template>
    <a-button style="margin-bottom:16px" @click="refresh"><SyncOutlined /> 刷新数据</a-button>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="projectNo" size="small" bordered @change="onChange" :scroll="{x:3000}">
      <template #bodyCell="{column,record}">
        <template v-if="column.key?.includes('Amount')||column.key?.includes('Profit')">
          ¥{{ (record[column.key as string]||0).toLocaleString() }}
        </template>
        <template v-else-if="column.key==='profitMargin'">
          <span :style="{color:(record.profitMargin||0)>=0?'#52c41a':'#ff4d4f'}">{{record.profitMargin||0}}%</span>
        </template>
        <template v-else-if="column.key==='revenueRate'||column.key==='expenseRate'">
          {{record[column.key as string]||0}}%
        </template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { SyncOutlined } from '@ant-design/icons-vue'
import { authFetch } from '../../utils/auth'
const dataList=ref<any[]>([]);const loading=ref(false)
const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const columns=[
  {title:'项目名称',dataIndex:'projectName',width:180,fixed:'left' as const},
  {title:'项目编号',dataIndex:'projectNo',width:120},{title:'项目预算',key:'budgetAmount',width:110},
  {title:'应收合同金额',key:'receivableAmount',width:120},{title:'累计结算金额',key:'settlementAmount',width:120},
  {title:'已收款',key:'receivedAmount',width:100},{title:'已开票',key:'invoicedAmount',width:100},
  {title:'未收款',key:'unreceivedAmount',width:100},{title:'未开票',key:'uninvoicedAmount',width:100},
  {title:'收款率',key:'revenueRate',width:80},{title:'开票率',key:'invoiceRate',width:80},
  {title:'应付合同金额',key:'payableAmount',width:120},{title:'已付款',key:'paidAmount',width:100},
  {title:'已收票',key:'receivedInvoice',width:100},{title:'未付款',key:'unpaidAmount',width:100},
  {title:'付款率',key:'expenseRate',width:80},{title:'利润',key:'profit',width:100},
  {title:'利润率',key:'profitMargin',width:80},{title:'已支付利润',key:'paidProfit',width:100},
]
onMounted(loadData)
async function loadData(){loading.value=true;const p=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await authFetch(`/api/statistics/projects?${p}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false}
function refresh(){loadData()}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
</script>
