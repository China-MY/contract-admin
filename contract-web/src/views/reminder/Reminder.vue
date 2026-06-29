<template>
  <a-card><template #title><span style="font-size:18px;font-weight:bold">提醒中心</span></template>
    <a-tabs v-model:activeKey="activeTab">
      <a-tab-pane key="all" tab="全部">
        <a-list :dataSource="reminders" :pagination="listPagination">
          <template #renderItem="{item}">
            <a-list-item>
              <a-list-item-meta>
                <template #title>
                  <a-tag :color="item.overdue ? 'red' : 'orange'">{{item.type}}</a-tag>
                  {{item.title}}
                </template>
                <template #description>
                  {{item.desc}} · {{item.overdue ? `已过期 ${item.days} 天` : `剩余 ${item.days} 天`}}
                </template>
              </a-list-item-meta>
              <template #extra><a-button size="small" type="primary" ghost @click="()=>message.info('查看详情功能开发中')">查看</a-button></template>
            </a-list-item>
          </template>
        </a-list>
      </a-tab-pane>
      <a-tab-pane key="contract" tab="合同到期">
        <a-empty v-if="!reminders.filter(r=>r.type==='合同到期').length" description="暂无合同到期提醒" />
      </a-tab-pane>
      <a-tab-pane key="plan" tab="计划到期">
        <a-empty v-if="!reminders.filter(r=>r.type==='计划到期').length" description="暂无计划到期提醒" />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
const activeTab = ref('all')
const reminders = ref<any[]>([])
const listPagination = { pageSize: 10 }
onMounted(async () => {
  const res = await fetch('/api/reminders')
  const d = await res.json()
  if (d.code === 200) reminders.value = d.data
})
</script>
