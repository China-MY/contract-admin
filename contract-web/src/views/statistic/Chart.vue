<template>
  <div class="stat-page"><a-card><template #title><span style="font-size:18px;font-weight:bold">图表分析</span></template>
    <a-button style="margin-bottom:16px" @click="refresh"><SyncOutlined /> 刷新数据</a-button>
    <a-row :gutter="16">
      <a-col :span="6" v-for="s in summaryCards" :key="s.label">
        <a-card size="small"><a-statistic :title="s.label" :value="s.value" :suffix="s.suffix" :value-style="{fontSize:'18px',color:s.color}" /></a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="12"><a-card title="年度合同趋势"><div ref="trendRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="应收合同类型分布"><div ref="receivablePieRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="应付合同类型分布"><div ref="payablePieRef" style="height:280px"></div></a-card></a-col>
    </a-row>
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="12"><a-card title="月度收支趋势"><div ref="incomeTrendRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="收款状态分析"><div ref="receiptPieRef" style="height:280px"></div></a-card></a-col>
      <a-col :span="6"><a-card title="付款状态分析"><div ref="paymentPieRef" style="height:280px"></div></a-card></a-col>
    </a-row>
  </a-card></div>
</template>
<script setup lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { SyncOutlined } from '@ant-design/icons-vue'
import * as echarts from 'echarts'
const trendRef=ref<HTMLElement>();const receivablePieRef=ref<HTMLElement>();const payablePieRef=ref<HTMLElement>()
const incomeTrendRef=ref<HTMLElement>();const receiptPieRef=ref<HTMLElement>();const paymentPieRef=ref<HTMLElement>()
const summaryCards=ref([{label:'应收合同总金额',value:'¥76,100,823',suffix:'',color:'#1890ff'},{label:'收款比例',value:'1.1%',suffix:'',color:'#52c41a'},{label:'应付合同总金额',value:'¥4,604,670',suffix:'',color:'#ff4d4f'},{label:'付款比例',value:'17.3%',suffix:'',color:'#faad14'}])

function renderCharts(data:any){
  if(!data)return
  nextTick(()=>{
    if(trendRef.value){echarts.init(trendRef.value).setOption({tooltip:{trigger:'axis'},xAxis:{type:'category',data:['2021','2022','2023','2024','2025','2026']},yAxis:{type:'value'},series:[{data:data.trend||[12,28,35,42,38,45],type:'line',smooth:true,areaStyle:{opacity:0.3}}]})}
    if(receivablePieRef.value){echarts.init(receivablePieRef.value).setOption({tooltip:{trigger:'item'},series:[{type:'pie',radius:['30%','60%'],data:data.receivableTypes||[{name:'销售合同',value:16},{name:'服务合同',value:7},{name:'采购合同',value:1},{name:'北斗续费',value:1}]}]})}
    if(payablePieRef.value){echarts.init(payablePieRef.value).setOption({tooltip:{trigger:'item'},series:[{type:'pie',radius:['30%','60%'],data:data.payableTypes||[{name:'采购合同',value:8},{name:'销售合同',value:2},{name:'服务合同',value:3}]}]})}
    if(incomeTrendRef.value){echarts.init(incomeTrendRef.value).setOption({tooltip:{trigger:'axis'},legend:{data:['收入','支出']},xAxis:{type:'category',data:['1月','2月','3月','4月','5月','6月']},yAxis:{type:'value'},series:[{name:'收入',type:'bar',data:data.income||[120,200,150,80,70,110]},{name:'支出',type:'bar',data:data.expense||[30,50,40,20,15,25]}]})}
    if(receiptPieRef.value){echarts.init(receiptPieRef.value).setOption({tooltip:{trigger:'item'},series:[{type:'pie',radius:['30%','60%'],data:[{name:'未收款',value:15},{name:'部分收款',value:4},{name:'已完成',value:6}]}]})}
    if(paymentPieRef.value){echarts.init(paymentPieRef.value).setOption({tooltip:{trigger:'item'},series:[{type:'pie',radius:['30%','60%'],data:[{name:'未付款',value:8},{name:'部分付款',value:3},{name:'已完成',value:2}]}]})}
  })
}
async function loadData(){const res=await fetch('/api/statistics/chart');const d=await res.json();if(d.code===200)renderCharts(d.data)}
function refresh(){loadData()}
onMounted(loadData)
</script>
