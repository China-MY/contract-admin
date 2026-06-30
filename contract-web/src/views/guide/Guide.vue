<template>
  <a-card>
    <template #title><span style="font-size:20px;font-weight:bold">🚀 新手指南</span></template>
    <a-typography-paragraph style="font-size:15px;color:#666">
      合同管理系统覆盖合同全生命周期管理，以下是推荐的操作流程，点击按钮可自动跳转。
    </a-typography-paragraph>

    <a-timeline>
      <!-- Step 1 -->
      <a-timeline-item color="blue">
        <template #dot><SettingOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第一步：配置基础信息</strong>
          <div style="margin:4px 0 8px;color:#666">
            在「系统设置」中配置公司信息、字典项（合同类型、税率等）和通知渠道（钉钉/飞书机器人）。建议先添加好我方公司和基础字典。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/settings/company')">公司管理</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/settings/basic')">基础设置</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/settings/notification')">通知配置</a-button>
          </div>
        </a-card>
      </a-timeline-item>

      <!-- Step 2 -->
      <a-timeline-item color="blue">
        <template #dot><TeamOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第二步：添加客户与供应商</strong>
          <div style="margin:4px 0 8px;color:#666">
            录入合作伙伴信息，包括名称、联系人、税号、开户行等。后续创建合同时可直接从列表中选择对方单位。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/customer/list')">客户管理</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/supplier/list')">供应商管理</a-button>
          </div>
        </a-card>
      </a-timeline-item>

      <!-- Step 3 -->
      <a-timeline-item color="blue">
        <template #dot><ProjectOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第三步：创建项目</strong>
          <div style="margin:4px 0 8px;color:#666">
            创建项目后可在项目详情中添加项目阶段，阶段进度自动计算项目整体完成度。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/project/list')">项目管理</a-button>
          </div>
        </a-card>
      </a-timeline-item>

      <!-- Step 4 -->
      <a-timeline-item color="blue">
        <template #dot><FileTextOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第四步：签订合同（应收/应付）</strong>
          <div style="margin:4px 0 8px;color:#666">
            应收合同向客户收款，应付合同向供应商付款。填写我方公司、对方单位、关联项目后，后续发票和资金单自动填充。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/contract/receivable')">应收合同</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/contract/payable')">应付合同</a-button>
          </div>
        </a-card>
      </a-timeline-item>

      <!-- Step 5 -->
      <a-timeline-item color="blue">
        <template #dot><FileProtectOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第五步：开具/接收发票</strong>
          <div style="margin:4px 0 8px;color:#666">
            销项发票开给客户，进项发票登记供应商来票。关联合同后自动填充开票方/收票方，选择税率自动计算税额。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/invoice/issue')">销项发票</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/invoice/receipt')">进项发票</a-button>
          </div>
        </a-card>
      </a-timeline-item>

      <!-- Step 6 -->
      <a-timeline-item color="blue">
        <template #dot><DollarOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第六步：资金管理（计划与记录）</strong>
          <div style="margin:4px 0 8px;color:#666">
            先创建收款/付款计划进行分期管理，实际收付款后在记录中登记。确认后自动更新合同已收/已付金额。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/payment/plan/receipt')">收款计划</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/payment/plan/pay')">付款计划</a-button>
          </div>
          <div style="margin-top:6px">
            <a-button type="primary" ghost size="small" @click="go('/payment/record/receipt')">收款记录</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/payment/record/pay')">付款记录</a-button>
          </div>
        </a-card>
      </a-timeline-item>

      <!-- Step 7 -->
      <a-timeline-item color="green">
        <template #dot><BarChartOutlined style="font-size:20px;color:#52c41a" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <strong style="font-size:16px">第七步：查看统计与提醒</strong>
          <div style="margin:4px 0 8px;color:#666">
            查看经营明细、图表分析和项目统计。提醒中心展示合同和计划到期提醒。配置好通知渠道后系统每天自动推送。
          </div>
          <div>
            <a-button type="primary" ghost size="small" @click="go('/statistic')">经营统计</a-button>
            <a-button style="margin-left:6px" size="small" @click="go('/reminder')">提醒中心</a-button>
          </div>
        </a-card>
      </a-timeline-item>
    </a-timeline>

    <a-divider />
    <a-alert type="info" showIcon>
      <template #message>
        <strong>💡 项目阶段提醒配置：</strong>
        在「<a @click="go('/settings/reminder-config')" style="cursor:pointer">系统设置 → 提醒配置</a>」中设置提前提醒天数和启用提醒类型，
        在「<a @click="go('/settings/notification')" style="cursor:pointer">系统设置 → 通知配置</a>」中配置钉钉/飞书机器人Webhook。
      </template>
    </a-alert>
    <a-alert type="warning" showIcon style="margin-top:12px">
      <template #message>
        <strong>📌 核心关联逻辑：</strong>
        合同 → 发票/资金计划/资金记录 自动关联填充。选择合同后系统自动填写我方公司、对方单位、开票方/收票方、付款方/收款方。
      </template>
    </a-alert>
  </a-card>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import {
  SettingOutlined, TeamOutlined, ProjectOutlined, FileTextOutlined,
  FileProtectOutlined, DollarOutlined, BarChartOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
function go(path: string) { router.push(path) }
</script>
