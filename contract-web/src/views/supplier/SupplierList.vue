<template>
  <a-card>
    <template #title><span style="font-size:18px;font-weight:bold">供应商列表</span></template>
    <a-form layout="inline" :model="sf" style="margin-bottom:16px">
      <a-form-item label="关键字"><a-input v-model:value="sf.keyword" placeholder="名称" allowClear /></a-form-item>
      <a-form-item><a-button type="primary" @click="loadData">查询</a-button><a-button style="margin-left:8px" @click="()=>{Object.assign(sf,{keyword:''});loadData()}">重置</a-button></a-form-item>
    </a-form>
    <a-button type="primary" style="margin-bottom:12px" @click="()=>message.info('新建供应商功能开发中')">新建供应商</a-button>
    <a-table :dataSource="dataList" :columns="columns" :loading="loading" :pagination="pagination" rowKey="id" size="small" bordered @change="onChange">
      <template #bodyCell="{column}">
        <template v-if="column.key==='action'"><a-button type="link" size="small" @click="()=>message.info('编辑功能开发中')">编辑</a-button></template>
      </template>
    </a-table>
  </a-card>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
const sf=reactive({keyword:''})
const dataList=ref<any[]>([]);const loading=ref(false)
const pagination=reactive({current:1,pageSize:10,total:0,showSizeChanger:true,showTotal:(t:number)=>`共 ${t} 条`})
const columns=[{title:'名称',dataIndex:'name',width:200},{title:'联系人',dataIndex:'contactPerson',width:120},{title:'联系电话',dataIndex:'phone',width:130},{title:'邮箱',dataIndex:'email',width:180},{title:'地址',dataIndex:'address',width:250},{title:'备注',dataIndex:'remark',width:200},{title:'操作',key:'action',width:80,fixed:'right' as const}]
onMounted(loadData)
async function loadData(){loading.value=true;const p=new URLSearchParams({page:String(pagination.current),size:String(pagination.pageSize)})
  const res=await fetch(`/api/suppliers?${p}`);const d=await res.json()
  if(d.code===200){dataList.value=d.data.records;pagination.total=d.data.total};loading.value=false}
function onChange(pag:any){pagination.current=pag.current;pagination.pageSize=pag.pageSize;loadData()}
</script>
