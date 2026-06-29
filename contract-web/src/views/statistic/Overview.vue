<template>
  <div class="stat-page"><a-card><template #title><span style="font-size:18px;font-weight:bold">经营明细</span></template>
    <a-button style="margin-bottom:16px" @click="refresh"><template #icon><SyncOutlined /></template>刷新数据</a-button>
    <a-row :gutter="16">
      <a-col :span="6" v-for="s in summary" :key="s.label">
        <a-card size="small"><a-statistic :title="s.label" :value="s.value" :suffix="s.suffix" :value-style="{fontSize:'20px',color:s.color}" /></a-card>
      </a-col>
    </a-row>
    <a-row :gutter="16" style="margin-top:16px">
      <a-col :span="12"><a-card title="应收（销项）"><a-table :dataSource="receivableStats" :columns="statCols" :pagination="false" size="small" bordered /></a-card></a-col>
      <a-col :span="12"><a-card title="应付（进项）"><a-table :dataSource="payableStats" :columns="statCols" :pagination="false" size="small" bordered /></a-card></a-col>
    </a-row>
  </a-card></div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { SyncOutlined } from '@ant-design/icons-vue'
const summary = ref<any[]>([])
const receivableStats = ref<any[]>([])
const payableStats = ref<any[]>([])
const statCols = [{title:'指标',dataIndex:'indicator',width:120},{title:'金额',dataIndex:'amount',width:160},{title:'比例',dataIndex:'rate',width:80}]
async function loadData(){
  const res=await fetch('/api/statistics/overview');const d=await res.json()
  if(d.code===200){summary.value=d.data.summary;receivableStats.value=d.data.receivable;payableStats.value=d.data.payable}
}
function refresh(){loadData()}
onMounted(loadData)
</script>
