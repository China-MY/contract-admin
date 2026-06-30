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
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第一步：配置基础信息</strong>
              <div style="margin-top:4px;color:#666">
                在「系统设置」中配置<span style="color:#1890ff;font-weight:bold">公司信息</span>、<span style="color:#1890ff;font-weight:bold">字典项</span>（合同类型、税率等）和<span style="color:#1890ff;font-weight:bold">通知渠道</span>（钉钉/飞书机器人）。
                <br/>建议先添加好我方公司和基础字典，后续创建合同时可直接选用。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button type="primary" ghost @click="go('/settings/company')">公司管理</a-button>
              <a-button type="default" ghost style="margin-left:4px" @click="go('/settings/basic')">字典</a-button>
              <a-button type="default" ghost style="margin-left:4px;margin-top:4px" @click="go('/settings/notification')">通知</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>

      <!-- Step 2 -->
      <a-timeline-item color="blue">
        <template #dot><TeamOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第二步：添加客户与供应商</strong>
              <div style="margin-top:4px;color:#666">
                在「<span style="color:#1890ff;font-weight:bold">客户</span>」和「<span style="color:#1890ff;font-weight:bold">供应商</span>」管理中录入合作伙伴信息，包括名称、联系人、税号、开户行等。
                <br/>后续创建合同时可直接从列表中选择对方单位。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button type="primary" ghost @click="go('/customer/list')">客户</a-button>
              <a-button type="default" ghost style="margin-left:4px" @click="go('/supplier/list')">供应商</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>

      <!-- Step 3 -->
      <a-timeline-item color="blue">
        <template #dot><ProjectOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第三步：创建项目</strong>
              <div style="margin-top:4px;color:#666">
                在「<span style="color:#1890ff;font-weight:bold">项目</span>」中创建项目，填写项目类型、年度、预算等信息。
                <br/>项目创建后可在「<span style="color:#1890ff;font-weight:bold">项目详情</span>」中添加<span style="color:#52c41a;font-weight:bold">项目阶段</span>，阶段进度自动计算项目整体进度。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button type="primary" ghost @click="go('/project/list')">项目管理</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>

      <!-- Step 4 -->
      <a-timeline-item color="blue">
        <template #dot><FileTextOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第四步：签订合同（应收/应付）</strong>
              <div style="margin-top:4px;color:#666">
                在「<span style="color:#1890ff;font-weight:bold">应收合同</span>」中创建向客户收款的合同，在「<span style="color:#1890ff;font-weight:bold">应付合同</span>」中创建向供应商付款的合同。
                <br/>填写合同金额、结算金额、我方公司、对方单位、关联项目等，系统自动计算收款/付款进度。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button type="primary" ghost @click="go('/contract/receivable')">应收</a-button>
              <a-button type="default" ghost style="margin-left:4px" @click="go('/contract/payable')">应付</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>

      <!-- Step 5 -->
      <a-timeline-item color="blue">
        <template #dot><FileProtectOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第五步：开具/接收发票</strong>
              <div style="margin-top:4px;color:#666">
                在「<span style="color:#1890ff;font-weight:bold">销项发票</span>」中开票给客户，在「<span style="color:#1890ff;font-weight:bold">进项发票</span>」中登记供应商的发票。
                <br/>选择关联合同后自动填充开票方/收票方，选择税率后自动计算不含税金额和税额。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button type="primary" ghost @click="go('/invoice/issue')">销项</a-button>
              <a-button type="default" ghost style="margin-left:4px" @click="go('/invoice/receipt')">进项</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>

      <!-- Step 6 -->
      <a-timeline-item color="blue">
        <template #dot><DollarOutlined style="font-size:20px;color:#1890ff" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第六步：资金管理（计划与记录）</strong>
              <div style="margin-top:4px;color:#666">
                创建<span style="color:#1890ff;font-weight:bold">收款/付款计划</span>进行分期管理，实际收到/支付款项后在<span style="color:#1890ff;font-weight:bold">收款/付款记录</span>中登记。
                <br/>选择关联合同后自动填充付款方/收款方，确认后自动更新合同的已收/已付金额。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-space direction="vertical" size="small">
                <a-button type="primary" ghost size="small" @click="go('/payment/plan/receipt')">收款计划</a-button>
                <a-button type="default" ghost size="small" @click="go('/payment/plan/pay')">付款计划</a-button>
                <a-button type="primary" ghost size="small" @click="go('/payment/record/receipt')">收款记录</a-button>
                <a-button type="default" ghost size="small" @click="go('/payment/record/pay')">付款记录</a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>

      <!-- Step 7 -->
      <a-timeline-item color="green">
        <template #dot><BarChartOutlined style="font-size:20px;color:#52c41a" /></template>
        <a-card size="small" style="max-width:700px;margin-bottom:8px">
          <a-row :gutter="16" align="middle">
            <a-col :span="18">
              <strong style="font-size:16px">第七步：查看统计与提醒</strong>
              <div style="margin-top:4px;color:#666">
                在「<span style="color:#1890ff;font-weight:bold">统计</span>」中查看经营明细、图表分析和项目统计。
                在「<span style="color:#1890ff;font-weight:bold">提醒中心</span>」查看合同到期和计划到期提醒。
                <br/>配置好通知渠道后，系统会在每天指定时间自动推送到期提醒到钉钉/飞书。
              </div>
            </a-col>
            <a-col :span="6" style="text-align:right">
              <a-button type="primary" ghost @click="go('/statistic')">统计</a-button>
              <a-button type="default" ghost style="margin-left:4px" @click="go('/reminder')">提醒</a-button>
            </a-col>
          </a-row>
        </a-card>
      </a-timeline-item>
    </a-timeline>

    <a-divider />
    <a-alert type="info" showIcon>
      <template #message>
        <strong>💡 项目阶段提醒配置：</strong>
        在「<a @click="go('/settings/reminder-config')" style="cursor:pointer">系统设置 → 提醒配置</a>」中设置提前提醒天数和启用提醒类型，
        在「<a @click="go('/settings/notification')" style="cursor:pointer">系统设置 → 通知配置</a>」中配置钉钉/飞书机器人Webhook，
        系统每天自动检查项目阶段的到期情况并推送通知。
      </template>
    </a-alert>
    <a-alert type="warning" showIcon style="margin-top:12px">
      <template #message>
        <strong>📌 核心关联逻辑：</strong>
        合同 → 发票/资金计划/资金记录 自动关联填充。<br/>
        选择合同后，系统自动填写我方公司、对方单位、开票方/收票方、付款方/收款方，无需重复录入。
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
