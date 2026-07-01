// 合同管理系统 — Mock API 层
// 模拟所有后端接口，前端可独立运行开发

// 模拟 fetch 拦截 — 所有 /api/ 请求由本地处理

// ---------- 辅助函数 ----------
function paginate(list: any[], page: number, size: number) {
  const start = (page - 1) * size
  const records = list.slice(start, start + size)
  return { records, total: list.length, page, size }
}

function delay(ms = 200) { return new Promise(r => setTimeout(r, ms)) }

// ---------- Mock 数据 ----------

// 客户
const customers = [
  { id: 1, name: '南京科技公司', type: 'customer', contactPerson: '张经理', phone: '13800001111', email: '', address: '南京市', remark: '' },
  { id: 2, name: '某某科技有限公司', type: 'customer', contactPerson: '李总', phone: '13800002222', email: '', address: '上海市', remark: '' },
  { id: 3, name: '中铁建二十局', type: 'customer', contactPerson: '王工', phone: '13800003333', email: '', address: '西安市', remark: '' },
  { id: 4, name: 'FUJIAN', type: 'customer', contactPerson: '陈经理', phone: '13800004444', email: '', address: '福州市', remark: '' },
  { id: 5, name: '苏州爱华', type: 'customer', contactPerson: '赵总', phone: '13800005555', email: '', address: '苏州市', remark: '' },
  { id: 6, name: '天津三环电力', type: 'customer', contactPerson: '刘工', phone: '13800006666', email: '', address: '天津市', remark: '' },
  { id: 7, name: '莆田市方人也网络科技', type: 'customer', contactPerson: '林总', phone: '13800007777', email: '', address: '莆田市', remark: '' },
]
const suppliers = [
  { id: 8, name: '联想集团', type: 'supplier', contactPerson: '杨工', phone: '13900001111', email: '', address: '北京市', remark: '' },
  { id: 9, name: '福州赢雅电子信息科技', type: 'supplier', contactPerson: '陈总', phone: '13900002222', email: '', address: '福州市', remark: '' },
  { id: 10, name: '甲方联系人 甲方联系电话 乙方', type: 'supplier', contactPerson: '联系人', phone: '13900003333', email: '', address: '', remark: '' },
]

// 项目
const projects = [
  { id: 1, projectNo: 'NM260101', projectName: '苏州爱华MES项目', projectType: '实施项目', status: 'init', year: '2026', source: '', manager: '张总', startDate: '2026-02-01', endDate: '2026-02-28', budgetAmount: 1000000, customerName: '苏州爱华', attachment: '', remark: '' },
  { id: 2, projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', projectType: '东大洋-1', status: 'in_progress', year: '2026', source: '', manager: '李经理', startDate: '2026-03-01', endDate: '2026-05-31', budgetAmount: 1000000, customerName: '中铁建二十局', attachment: '', remark: '' },
  { id: 3, projectNo: '202603001', projectName: '智慧校园', projectType: '', status: 'in_progress', year: '2026', source: '', manager: '', startDate: '2026-05-01', endDate: '2026-05-31', budgetAmount: 0, customerName: '', attachment: '', remark: '' },
  { id: 4, projectNo: '588', projectName: '北斗续费', projectType: '北斗续费', status: 'init', year: '', source: '', manager: '', startDate: '', endDate: '', budgetAmount: 0, customerName: '', attachment: '', remark: '' },
  { id: 5, projectNo: 'TEST-2026-001', projectName: '测试项目A', projectType: '', status: 'init', year: '', source: '', manager: '', startDate: '', endDate: '', budgetAmount: 0, customerName: '', attachment: '', remark: '' },
]

// 应收合同
const receivableContracts = [
  { id: 1, contractNo: 'HT202512002', contractName: '汉庭酒店2025维保合同', contractType: '服务合同', direction: 'receivable', ourCompany: '筱凡网络科技', counterparty: '南京科技公司', projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', contractAddress: '闵行区芦恒路60号', manager: '-', status: 'confirmed', signDate: '2026-05-01', endDate: '2026-05-31', pricingMethod: 'fixed', contractAmount: 9999, settlementAmount: 9999, receivedAmount: 2000, invoicedAmount: 3000, unreceivedAmount: 7999, uninvoicedAmount: 6999, receivedNotInvoiced: 0, invoicedNotReceived: 1000, receiptStatus: 'partial', receiptProgress: 20, invoiceProgress: 30, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 9969.30, profitMargin: 99.70, paidProfit: 1970.30, attachment: '', remark: '' },
  { id: 2, contractNo: '2601', contractName: 'FJ2233005', contractType: '销售合同', direction: 'receivable', ourCompany: '成者为译', counterparty: 'FUJIAN', projectNo: '-', projectName: '-', contractAddress: '-', manager: '-', status: 'unconfirmed', signDate: '2026-06-26', endDate: '-', pricingMethod: 'fixed', contractAmount: 10000, settlementAmount: 10000, receivedAmount: 0, invoicedAmount: 5000, unreceivedAmount: 10000, uninvoicedAmount: 5000, receivedNotInvoiced: 0, invoicedNotReceived: 5000, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 50, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 9424.78, profitMargin: 94.25, paidProfit: -575.22, attachment: '', remark: '' },
  { id: 3, contractNo: '123456', contractName: '合肥', contractType: '销售合同', direction: 'receivable', ourCompany: '筱凡网络科技', counterparty: '某某科技有限公司', projectNo: '-', projectName: '-', contractAddress: '-', manager: '-', status: 'archived', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 10000, settlementAmount: 10000, receivedAmount: 0, invoicedAmount: 110000, unreceivedAmount: 10000, uninvoicedAmount: -100000, receivedNotInvoiced: 0, invoicedNotReceived: 110000, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 8910.89, profitMargin: 89.11, paidProfit: -1089.11, attachment: '', remark: '' },
  { id: 4, contractNo: '苏C1Q22222', contractName: '苏C1Q22222', contractType: '北斗续费', direction: 'receivable', ourCompany: '苏C1Q22222', counterparty: '212', projectNo: '588', projectName: '北斗续费', contractAddress: '-', manager: '-', status: 'archived', signDate: '2026-05-18', endDate: '2026-05-20', pricingMethod: 'fixed', contractAmount: 300, settlementAmount: 300, receivedAmount: 1100, invoicedAmount: 0, unreceivedAmount: -800, uninvoicedAmount: 300, receivedNotInvoiced: 1100, invoicedNotReceived: 0, receiptStatus: 'completed', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 300, profitMargin: 100, paidProfit: 1100, attachment: '', remark: '' },
  { id: 5, contractNo: '1111111', contractName: '11', contractType: '销售合同', direction: 'receivable', ourCompany: '筱凡网络科技', counterparty: '南京科技公司', projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '2026-05-18', endDate: '2027-05-18', pricingMethod: 'actual', contractAmount: 2400, settlementAmount: 2400, receivedAmount: 200, invoicedAmount: 0, unreceivedAmount: 2200, uninvoicedAmount: 2400, receivedNotInvoiced: 200, invoicedNotReceived: 0, receiptStatus: 'partial', receiptProgress: 8.33, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 2400, profitMargin: 100, paidProfit: 200, attachment: '', remark: '' },
  { id: 6, contractNo: '2026051501', contractName: '采购合同', contractType: '采购合同', direction: 'receivable', ourCompany: '筱凡网络科技', counterparty: '南京科技公司', projectNo: 'SZ21-3505-2024-100097-2', projectName: '供应链管理系统项目销售合同', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '2026-05-15', endDate: '2027-05-14', pricingMethod: 'fixed', contractAmount: 6000, settlementAmount: 6000, receivedAmount: 0, invoicedAmount: 0, unreceivedAmount: 6000, uninvoicedAmount: 6000, receivedNotInvoiced: 0, invoicedNotReceived: 0, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 6000, profitMargin: 100, paidProfit: 0, attachment: '', remark: '' },
  { id: 7, contractNo: 'HT202512001', contractName: '汉庭酒店2025维保合同', contractType: '服务合同', direction: 'receivable', ourCompany: '筱凡网络科技', counterparty: '南京科技公司', projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', contractAddress: '闵行区芦恒路60号', manager: '-', status: 'confirmed', signDate: '2026-05-01', endDate: '2026-05-31', pricingMethod: 'fixed', contractAmount: 8888, settlementAmount: 8888, receivedAmount: 0, invoicedAmount: 0, unreceivedAmount: 8888, uninvoicedAmount: 8888, receivedNotInvoiced: 0, invoicedNotReceived: 0, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 8888, profitMargin: 100, paidProfit: 0, attachment: '', remark: '' },
  { id: 8, contractNo: 'xf-100', contractName: '小程序开发', contractType: '服务合同', direction: 'receivable', ourCompany: '爱绝', counterparty: '落白', projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', contractAddress: '闵行区芦恒路61号', manager: '-', status: 'confirmed', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 3800, settlementAmount: 3800, receivedAmount: 0, invoicedAmount: 0, unreceivedAmount: 3800, uninvoicedAmount: 3800, receivedNotInvoiced: 0, invoicedNotReceived: 0, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 3800, profitMargin: 100, paidProfit: 0, attachment: '', remark: '' },
  { id: 9, contractNo: '20', contractName: '枯', contractType: '销售合同', direction: 'receivable', ourCompany: '缀', counterparty: '在', projectNo: '-', projectName: '-', contractAddress: '-', manager: '-', status: 'unconfirmed', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 10, settlementAmount: 10, receivedAmount: 0, invoicedAmount: 0, unreceivedAmount: 10, uninvoicedAmount: 10, receivedNotInvoiced: 0, invoicedNotReceived: 0, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 10, profitMargin: 100, paidProfit: 0, attachment: '', remark: '' },
  { id: 10, contractNo: '1', contractName: '123', contractType: '销售合同', direction: 'receivable', ourCompany: '1', counterparty: '1', projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', contractAddress: '闵行区芦恒路62号', manager: '-', status: 'unconfirmed', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 10, settlementAmount: 10, receivedAmount: 0, invoicedAmount: 0, unreceivedAmount: 10, uninvoicedAmount: 10, receivedNotInvoiced: 0, invoicedNotReceived: 0, receiptStatus: 'unreceived', receiptProgress: 0, invoiceProgress: 0, procurementContractAmount: 0, procurementSettlementAmount: 0, procurementPaidAmount: 0, procurementReceivedInvoice: 0, profit: 10, profitMargin: 100, paidProfit: 0, attachment: '', remark: '' },
]

// 应付合同
const payableContracts = [
  { id: 101, contractNo: 'LY-2026-01', contractName: '白炭黑设备采购', contractType: '采购合同', direction: 'payable', ourCompany: '绿源', counterparty: '甲方联系人 甲方联系电话 乙方', projectNo: 'LY-BTH-01', projectName: '白炭黑绿化工程', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '-', endDate: '2026-03-20', pricingMethod: 'fixed', contractAmount: 450000, settlementAmount: 450000, paidAmount: 350000, receivedInvoiceAmount: 350000, unpaidAmount: 100000, unreceivedInvoiceAmount: 100000, paidNotReceivedInvoice: 0, receivedInvoiceNotPaid: 0, paymentStatus: 'partial', paymentProgress: 77.78, receivedInvoiceProgress: 77.78, attachment: '', remark: '' },
  { id: 102, contractNo: 'JCY002', contractName: '测试', contractType: '采购合同', direction: 'payable', ourCompany: '测试', counterparty: '测试', projectNo: '-', projectName: '-', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 100, settlementAmount: 100, paidAmount: 100, receivedInvoiceAmount: 0, unpaidAmount: 0, unreceivedInvoiceAmount: 100, paidNotReceivedInvoice: 100, receivedInvoiceNotPaid: 0, paymentStatus: 'completed', paymentProgress: 100, receivedInvoiceProgress: 0, attachment: '', remark: '' },
  { id: 103, contractNo: 'NM260101-01', contractName: '苏州爱华MES项目采购合同', contractType: '采购合同', direction: 'payable', ourCompany: '苏州诺码智能', counterparty: '联想', projectNo: 'NM260101', projectName: '苏州爱华MES项目', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 20000, settlementAmount: 20000, paidAmount: 0, receivedInvoiceAmount: 0, unpaidAmount: 20000, unreceivedInvoiceAmount: 20000, paidNotReceivedInvoice: 0, receivedInvoiceNotPaid: 0, paymentStatus: 'unpaid', paymentProgress: 0, receivedInvoiceProgress: 0, attachment: '', remark: '' },
  { id: 104, contractNo: 'SZ21-3505-2024-100097-2', contractName: '供应链管理系统项目销售合同', contractType: '采购合同', direction: 'payable', ourCompany: '福州赢雅电子信息科技有限公司', counterparty: '联通（福建）', projectNo: 'SZ21-3505-2024-100097-2', projectName: '供应链管理系统项目销售合同', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '-', endDate: '2026-03-12', pricingMethod: 'fixed', contractAmount: 2880000, settlementAmount: 2880000, paidAmount: 104600, receivedInvoiceAmount: 53000, unpaidAmount: 2775400, unreceivedInvoiceAmount: 2827000, paidNotReceivedInvoice: 51600, receivedInvoiceNotPaid: 0, paymentStatus: 'partial', paymentProgress: 3.63, receivedInvoiceProgress: 1.84, attachment: '', remark: '' },
  { id: 105, contractNo: '112212', contractName: '测绘', contractType: '采购合同', direction: 'payable', ourCompany: '1', counterparty: '1', projectNo: '123', projectName: '打印', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '-', endDate: '2026-02-25', pricingMethod: 'fixed', contractAmount: 300000, settlementAmount: 300000, paidAmount: 302500, receivedInvoiceAmount: 302000, unpaidAmount: -2500, unreceivedInvoiceAmount: -2000, paidNotReceivedInvoice: 500, receivedInvoiceNotPaid: 0, paymentStatus: 'completed', paymentProgress: 100, receivedInvoiceProgress: 100, attachment: '', remark: '' },
  { id: 106, contractNo: '1213', contractName: '采购物资', contractType: '采购合同', direction: 'payable', ourCompany: '张三', counterparty: '张三', projectNo: 'NM260101', projectName: '苏州爱华MES项目', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '2026-03-29', endDate: '2026-02-28', pricingMethod: 'fixed', contractAmount: 100, settlementAmount: 100, paidAmount: 1000, receivedInvoiceAmount: 100, unpaidAmount: -900, unreceivedInvoiceAmount: 0, paidNotReceivedInvoice: 900, receivedInvoiceNotPaid: 0, paymentStatus: 'completed', paymentProgress: 100, receivedInvoiceProgress: 100, attachment: '', remark: '' },
  { id: 107, contractNo: '65665', contractName: 'DASD', contractType: '采购合同', direction: 'payable', ourCompany: '问问', counterparty: '问问', projectNo: '123', projectName: '打印', contractAddress: '-', manager: '-', status: 'confirmed', signDate: '-', endDate: '-', pricingMethod: 'fixed', contractAmount: 6000, settlementAmount: 60000, paidAmount: 213, receivedInvoiceAmount: 0, unpaidAmount: 59787, unreceivedInvoiceAmount: 60000, paidNotReceivedInvoice: 213, receivedInvoiceNotPaid: 0, paymentStatus: 'partial', paymentProgress: 0.36, receivedInvoiceProgress: 0, attachment: '', remark: '' },
]

// 销项发票
const outputInvoices = [
  { id: 1, contractNo: '2601', contractName: 'FJ2233005', invoiceNo: '111117', direction: 'output', type: '专用发票', amountWithTax: 5000, amountWithoutTax: 4424.78, taxRate: 13, taxAmount: 575.22, issueDate: '2026-06-26', voucherNo: '-', issuer: '成者为译', issuerTaxId: '-', receiver: 'FUJIAN', receiverTaxId: '-', attachment: '', remark: '' },
  { id: 2, contractNo: '123456', contractName: '合肥', invoiceNo: '223', direction: 'output', type: '专用发票', amountWithTax: 100000, amountWithoutTax: 99009.90, taxRate: 1, taxAmount: 990.10, issueDate: '2026-06-25', voucherNo: '-', issuer: '筱凡网络科技', issuerTaxId: '-', receiver: '某某科技有限公司', receiverTaxId: '-', attachment: '', remark: '' },
  { id: 3, contractNo: '123456', contractName: '合肥', invoiceNo: '111111111111111', direction: 'output', type: '专用发票', amountWithTax: 10000, amountWithoutTax: 9900.99, taxRate: 1, taxAmount: 99.01, issueDate: '2026-06-03', voucherNo: '-', issuer: '筱凡网络科技', issuerTaxId: '-', receiver: '某某科技有限公司', receiverTaxId: '-', attachment: '', remark: '' },
  { id: 4, contractNo: 'HT202512002', contractName: '汉庭酒店2025维保合同', invoiceNo: '25312000000113282363', direction: 'output', type: '专用发票', amountWithTax: 2000, amountWithoutTax: 1980.20, taxRate: 1, taxAmount: 19.80, issueDate: '2026-05-11', voucherNo: '-', issuer: '筱凡网络科技', issuerTaxId: '-', receiver: '南京科技公司', receiverTaxId: '-', attachment: '', remark: '' },
  { id: 5, contractNo: '202603001', contractName: '智慧校园', invoiceNo: '22222', direction: 'output', type: '专用发票', amountWithTax: 100000, amountWithoutTax: 88495.58, taxRate: 13, taxAmount: 11504.42, issueDate: '2026-03-25', voucherNo: '-', issuer: '公司', issuerTaxId: '-', receiver: '中铁建二十局', receiverTaxId: '-', attachment: '', remark: '' },
]

// 进项发票
const inputInvoices = [
  { id: 1, contractNo: 'LY-2026-01', contractName: '白炭黑设备采购', invoiceNo: '65389', direction: 'input', type: '专用发票', amountWithTax: 350000, amountWithoutTax: 309734.51, taxRate: 13, taxAmount: 40265.49, issueDate: '2026-03-08', voucherNo: '-', issuer: '甲方联系人 甲方联系电话 乙方', issuerTaxId: '-', receiver: '绿源', receiverTaxId: '-', attachment: '', remark: '' },
  { id: 2, contractNo: 'SZ21-3505-2024-100097-2', contractName: '供应链管理系统项目销售合同', invoiceNo: '26352000000226957456', direction: 'input', type: '专用发票', amountWithTax: 53000, amountWithoutTax: 50000, taxRate: 0.06, taxAmount: 3000, issueDate: '2026-02-02', voucherNo: '-', issuer: '莆田市方人也网络科技有限公司', issuerTaxId: '91350304MA32J0EX7Y', receiver: '福州赢雅电子信息科技有限公司', receiverTaxId: '-', attachment: '', remark: '发票备注信息' },
]

// 收款计划
const receiptPlans = [
  { id: 1, contractNo: '苏C1Q22222', contractName: '苏C1Q22222', invoiceNo: '', planCode: 'PS-202605-0003', direction: 'receipt', plannedAmount: 300, paidAmount: 500, unpaidAmount: -200, plannedDate: '2026-05-20', status: 'paid', progress: 100, payer: '苏C1Q22222', payee: '212', remark: '' },
  { id: 2, contractNo: 'HT202512002', contractName: '汉庭酒店2025维保合同', invoiceNo: '25312000000113282363', planCode: 'PS-202605-0002', direction: 'receipt', plannedAmount: 2000, paidAmount: 1000, unpaidAmount: 1000, plannedDate: '2026-05-31', status: 'partial', progress: 50, payer: '南京科技公司', payee: '筱凡网络科技', remark: '' },
  { id: 3, contractNo: '2026-03-18', contractName: '工业办公室', invoiceNo: '', planCode: 'PS-202603-0009', direction: 'receipt', plannedAmount: 18000, paidAmount: 11111, unpaidAmount: 6889, plannedDate: '2026-03-31', status: 'partial', progress: 61.73, payer: '不知道是谁', payee: '天津三环电力', remark: '' },
  { id: 4, contractNo: '202603001', contractName: '智慧校园', invoiceNo: '22222', planCode: 'PS-202604-0001', direction: 'receipt', plannedAmount: 100000, paidAmount: 0, unpaidAmount: 100000, plannedDate: '2026-04-30', status: 'unpaid', progress: 0, payer: '中铁建二十局', payee: '公司', remark: '' },
]

// 付款计划
const payPlans = [
  { id: 1, contractNo: 'LY-2026-01', contractName: '白炭黑设备采购', invoiceNo: '65389', planCode: 'PF-202603-0005', direction: 'pay', plannedAmount: 350000, paidAmount: 350000, unpaidAmount: 0, plannedDate: '2026-03-08', status: 'paid', progress: 100, payer: '绿源', payee: '甲方联系人 甲方联系电话 乙方', remark: '' },
  { id: 2, contractNo: '112212', contractName: '测绘', invoiceNo: '11', planCode: 'PF-202602-0001', direction: 'pay', plannedAmount: 300000, paidAmount: 300000, unpaidAmount: 0, plannedDate: '2026-02-28', status: 'paid', progress: 100, payer: '1', payee: '1', remark: '' },
  { id: 3, contractNo: 'SZ21-3505-2024-100097-2', contractName: '供应链管理系统项目销售合同', invoiceNo: '26352000000226957456', planCode: 'PF-202501-0001', direction: 'pay', plannedAmount: 46000, paidAmount: 4600, unpaidAmount: 41400, plannedDate: '2026-03-12', status: 'partial', progress: 10, payer: '福州赢雅电子信息科技有限公司', payee: '联通（福建）产业互联网有限公司莆田分公司', remark: '收付款计划备注信息' },
]

// 收款记录
const receiptRecords = [
  { id: 1, contractNo: '苏C1Q22222', contractName: '苏C1Q22222', invoiceNo: '', recordNo: '202605180002', direction: 'receipt', recordDate: '2026-05-18', amount: 300, status: 'confirmed', method: '农业银行', account: '-', expenseType: '-', payer: '苏C1Q22222', payerBank: '-', payee: '212', payeeBank: '-', voucherNo: '-', attachment: '', remark: '' },
  { id: 2, contractNo: 'HT202512002', contractName: '汉庭酒店2025维保合同', invoiceNo: '26312000000545932066', recordNo: '202605140002', direction: 'receipt', recordDate: '2026-05-14', amount: 1000, status: 'confirmed', method: '农业银行', account: '-', expenseType: '-', payer: '南京科技公司', payerBank: '-', payee: '筱凡网络科技', payeeBank: '-', voucherNo: '-', attachment: '', remark: '' },
  { id: 3, contractNo: '123456', contractName: '合肥', invoiceNo: '111111111111111', recordNo: '202606030001', direction: 'receipt', recordDate: '2026-06-03', amount: 10000, status: 'pending', method: '农业银行', account: '-', expenseType: '-', payer: '筱凡网络科技', payerBank: '-', payee: '某某科技有限公司', payeeBank: '-', voucherNo: '-', attachment: '', remark: '' },
]

// 用户/角色/部门
const users = [
  { id: 1, realName: '落白', gender: '男', username: 'admin', phone: '13800138000', email: 'admin@example.com', roleNames: '超级管理员', deptName: '-', postName: '-', status: 'normal', lastLoginAt: '2026-06-26 15:33:28', remark: '系统初始管理员账户' },
  { id: 2, realName: '李小雯', gender: '女', username: 'LXW', phone: '-', email: '-', roleNames: '操作', deptName: '销售部', postName: '-', status: 'normal', lastLoginAt: '-', remark: '' },
  { id: 3, realName: '张san', gender: '男', username: 'zhangsan', phone: '-', email: '-', roleNames: '普通用户', deptName: '销售部', postName: '-', status: 'normal', lastLoginAt: '2026-06-03 22:02:29', remark: '' },
]
const roles = [
  { id: 1, roleName: '超级管理员', roleCode: 'SUPER_ADMIN', status: 'enabled', sortOrder: 0, remark: '系统最高权限角色' },
  { id: 2, roleName: '普通用户', roleCode: 'USER', status: 'enabled', sortOrder: 4, remark: '基础用户角色' },
  { id: 3, roleName: '操作', roleCode: 'ROLE_IDS0YEYO', status: 'enabled', sortOrder: 0, remark: '' },
]

// ---------- Mock Request Handler ----------
async function mockHandler(url: string, options?: RequestInit): Promise<Response> {
  const method = (options?.method || 'GET').toUpperCase()
  const body = options?.body ? JSON.parse(options.body as string) : {}

  await delay(100 + Math.random() * 200)

  // 登录
  if (url === '/api/auth/login' && method === 'POST') {
    if (body.username === 'admin' && body.password === '123456') {
      return jsonResponse({
        code: 200, msg: 'success',
        data: {
          token: 'mock-jwt-token-admin-2026',
          user: { id: 1, username: 'admin', realName: '落白', roleNames: '超级管理员' }
        }
      })
    }
    return jsonResponse({ code: 401, msg: '用户名或密码错误' })
  }

  // 仪表盘
  if (url === '/api/dashboard/summary' && method === 'GET') {
    const totalContracts = receivableContracts.length + payableContracts.length
    const totalReceived = receivableContracts.reduce((s, c) => s + c.receivedAmount, 0)
    const totalPaid = payableContracts.reduce((s, c) => s + c.paidAmount, 0)
    return jsonResponse({ code: 200, data: {
      cards: [
        { title: '合同总数', value: totalContracts, suffix: '份', sub: '本月新增 4 份', color: '#1890ff' },
        { title: '已完成合同', value: 5, suffix: '份', sub: '完成率 13.16%', color: '#52c41a' },
        { title: '已收款', value: '¥' + totalReceived.toLocaleString(), suffix: '', sub: '本月收款 ¥3,000', color: '#1890ff' },
        { title: '已付款', value: '¥' + totalPaid.toLocaleString(), suffix: '', sub: '本月付款 ¥0', color: '#ff4d4f' },
      ],
      reminders: [
        { title: '应付合同到期', desc: '供应链管理系统项目销售合同', overdue: true, days: 597 },
        { title: '付款计划到期', desc: '测绘', overdue: true, days: 120 },
        { title: '应收合同到期', desc: '苏州爱华MES项目', overdue: true, days: 117 },
        { title: '付款计划到期', desc: '白炭黑设备采购', overdue: true, days: 105 },
      ],
      transactions: [
        { type: 'receipt', amount: 10000, party: '筱凡网络科技', date: '2026-06-03', status: 'pending' },
        { type: 'receipt', amount: 300, party: '苏C1Q22222', date: '2026-05-18', status: 'confirmed' },
        { type: 'receipt', amount: 200, party: '1111111', date: '2026-05-18', status: 'confirmed' },
        { type: 'receipt', amount: 1000, party: '汉庭酒店', date: '2026-05-14', status: 'confirmed' },
      ],
      trendMonths: ['1月','2月','3月','4月','5月','6月'],
      trendValues: [120000, 280000, 350000, 420000, 380000, 450000],
      statusDistribution: [
        { name: '未签订', value: 6 }, { name: '已签订', value: 22 }, { name: '已归档', value: 10 },
      ],
    }})
  }

  // 最近提醒
  if (url === '/api/reminders' && method === 'GET') {
    const reminders = [
      { id: 1, type: 'contract', title: '应付合同到期', contractNo: 'SZ21-3505-2024-100097-2', name: '供应链管理系统项目销售合同', dueDate: '2024-11-05', overdueDays: 597 },
      { id: 2, type: 'payment_plan', title: '付款计划到期', contractNo: '112212', name: '测绘', dueDate: '2026-02-25', overdueDays: 120 },
      { id: 3, type: 'contract', title: '应收合同到期', contractNo: 'NM260101', name: '苏州爱华MES项目', dueDate: '2026-02-28', overdueDays: 117 },
      { id: 4, type: 'payment_plan', title: '付款计划到期', contractNo: 'SZ21-3505-2024-100097-2', name: '供应链管理系统项目销售合同', dueDate: '2026-03-12', overdueDays: 105 },
      { id: 5, type: 'contract', title: '应付合同到期', contractNo: 'LY-2026-01', name: '白炭黑设备采购', dueDate: '2026-03-20', overdueDays: 97 },
    ]
    return jsonResponse({ code: 200, data: reminders })
  }

  // 合同列表
  if (url.startsWith('/api/contracts') && method === 'GET') {
    const urlObj = new URL(url, 'http://localhost')
    const direction = urlObj.searchParams.get('direction') || 'receivable'
    const page = parseInt(urlObj.searchParams.get('page') || '1')
    const size = parseInt(urlObj.searchParams.get('size') || '10')
    const keyword = urlObj.searchParams.get('keyword') || ''
    const status = urlObj.searchParams.get('receiptStatus') || ''
    const list = (direction === 'receivable' ? receivableContracts : payableContracts)
      .filter((c: any) => !keyword || c.contractName.includes(keyword) || c.contractNo.includes(keyword))
      .filter((c: any) => !status || status === 'all' || c.receiptStatus === status || c.paymentStatus === status)
    const totalAmount = list.reduce((s, c) => s + (c.contractAmount || 0), 0)
    return jsonResponse({ code: 200, data: { ...paginate(list, page, size), totalAmount } })
  }

  // 新建合同
  if (url === '/api/contracts' && method === 'POST') {
    const c = { ...body }
    c.id = Math.max(...receivableContracts.map(x => x.id), ...payableContracts.map(x => x.id), 0) + 1
    c.direction = c.direction || 'receivable'
    if (!c.contractNo) {
      const d = new Date()
      const ds = d.getFullYear() + String(d.getMonth()+1).padStart(2,'0') + String(d.getDate()).padStart(2,'0')
      c.contractNo = (c.direction === 'receivable' ? 'CT-R-' : 'CT-P-') + ds + '-' + String(c.id).padStart(3,'0')
    }
    // 确保结算/已收/未收字段初始化
    c.settlementAmount = c.settlementAmount ?? c.contractAmount ?? 0
    c.receivedAmount = c.receivedAmount ?? 0
    c.invoicedAmount = c.invoicedAmount ?? 0
    c.unreceivedAmount = c.contractAmount ?? 0
    c.uninvoicedAmount = c.contractAmount ?? 0
    c.receiptStatus = 'unreceived'
    c.receiptProgress = 0
    c.invoiceProgress = 0
    c.paidAmount = c.paidAmount ?? 0
    c.receivedInvoiceAmount = c.receivedInvoiceAmount ?? 0
    c.unpaidAmount = c.contractAmount ?? 0
    c.unreceivedInvoiceAmount = c.contractAmount ?? 0
    c.paymentStatus = 'unpaid'
    c.paymentProgress = 0
    c.receivedInvoiceProgress = 0
    c.profit = (c.contractAmount ?? 0) - (c.procurementContractAmount ?? 0)
    if (c.direction === 'receivable') {
      receivableContracts.unshift(c)
    } else {
      payableContracts.unshift(c)
    }
    return jsonResponse({ code: 200, msg: '保存成功', data: c })
  }

  // 更新合同
  if (method === 'PUT' && url.startsWith('/api/contracts/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    let idx = receivableContracts.findIndex(c => c.id === id)
    let list: any[] = receivableContracts
    if (idx < 0) { idx = payableContracts.findIndex(c => c.id === id); list = payableContracts as any }
    if (idx >= 0) {
      const item: any = list[idx]
      Object.assign(item, body, { id })
      // 重新计算衍生字段
      item.unreceivedAmount = (item.contractAmount ?? 0) - (item.receivedAmount ?? 0)
      item.uninvoicedAmount = (item.settlementAmount ?? 0) - (item.invoicedAmount ?? 0)
      item.receiptStatus = (item.receivedAmount ?? 0) >= (item.contractAmount ?? 0) ? 'completed'
        : (item.receivedAmount ?? 0) > 0 ? 'partial' : 'unreceived'
      item.receiptProgress = (item.contractAmount ?? 0) > 0 ? ((item.receivedAmount ?? 0) / (item.contractAmount ?? 0)) * 100 : 0
      item.invoiceProgress = (item.settlementAmount ?? 0) > 0 ? ((item.invoicedAmount ?? 0) / (item.settlementAmount ?? 0)) * 100 : 0
      item.unpaidAmount = (item.contractAmount ?? 0) - (item.paidAmount ?? 0)
      item.unreceivedInvoiceAmount = (item.settlementAmount ?? 0) - (item.receivedInvoiceAmount ?? 0)
      item.paymentStatus = (item.paidAmount ?? 0) >= (item.contractAmount ?? 0) ? 'completed'
        : (item.paidAmount ?? 0) > 0 ? 'partial' : 'unpaid'
      item.paymentProgress = (item.contractAmount ?? 0) > 0 ? ((item.paidAmount ?? 0) / (item.contractAmount ?? 0)) * 100 : 0
      item.receivedInvoiceProgress = (item.settlementAmount ?? 0) > 0 ? ((item.receivedInvoiceAmount ?? 0) / (item.settlementAmount ?? 0)) * 100 : 0
      item.profit = (item.contractAmount ?? 0) - (item.procurementContractAmount ?? 0)
    }
    return jsonResponse({ code: 200, msg: '保存成功' })
  }

  // 删除合同
  if (method === 'DELETE' && url.startsWith('/api/contracts/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    let idx = receivableContracts.findIndex(c => c.id === id)
    if (idx >= 0) {
      receivableContracts.splice(idx, 1)
    } else {
      idx = payableContracts.findIndex(c => c.id === id)
      if (idx >= 0) payableContracts.splice(idx, 1)
    }
    return jsonResponse({ code: 200, msg: '删除成功' })
  }

  // 发票列表
  if (url.startsWith('/api/invoices') && method === 'GET') {
    const urlObj = new URL(url, 'http://localhost')
    const direction = urlObj.searchParams.get('direction') || 'output'
    const page = parseInt(urlObj.searchParams.get('page') || '1')
    const size = parseInt(urlObj.searchParams.get('size') || '10')
    const list = direction === 'output' ? outputInvoices : inputInvoices
    const totalAmount = list.reduce((s, i) => s + (i.amountWithTax || 0), 0)
    return jsonResponse({ code: 200, data: { ...paginate(list, page, size), totalAmount } })
  }

  // 新建发票
  if (url === '/api/invoices' && method === 'POST') {
    const inv = { ...body }
    inv.id = Math.max(...outputInvoices.map(x => x.id), ...inputInvoices.map(x => x.id), 0) + 1
    const list = (inv.direction || 'output') === 'output' ? outputInvoices : inputInvoices
    list.unshift(inv)
    return jsonResponse({ code: 200, msg: '保存成功', data: inv })
  }

  // 更新发票
  if (method === 'PUT' && url.startsWith('/api/invoices/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    let idx = outputInvoices.findIndex(c => c.id === id)
    let list = outputInvoices
    if (idx < 0) { idx = inputInvoices.findIndex(c => c.id === id); list = inputInvoices }
    if (idx >= 0) list[idx] = { ...list[idx], ...body, id }
    return jsonResponse({ code: 200, msg: '保存成功' })
  }

  // 删除发票
  if (method === 'DELETE' && url.startsWith('/api/invoices/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    let idx = outputInvoices.findIndex(c => c.id === id)
    if (idx >= 0) outputInvoices.splice(idx, 1)
    else { idx = inputInvoices.findIndex(c => c.id === id); if (idx >= 0) inputInvoices.splice(idx, 1) }
    return jsonResponse({ code: 200, msg: '删除成功' })
  }

  // 资金计划
  if (url.startsWith('/api/payment-plans') && method === 'GET') {
    const urlObj = new URL(url, 'http://localhost')
    const direction = urlObj.searchParams.get('direction') || 'receipt'
    const page = parseInt(urlObj.searchParams.get('page') || '1')
    const size = parseInt(urlObj.searchParams.get('size') || '10')
    const list = direction === 'receipt' ? receiptPlans : payPlans
    return jsonResponse({ code: 200, data: paginate(list, page, size) })
  }
  if (url === '/api/payment-plans' && method === 'POST') {
    const p = { ...body }
    p.id = Math.max(...receiptPlans.map(x => x.id), ...payPlans.map(x => x.id), 0) + 1
    const list = (p.direction || 'receipt') === 'receipt' ? receiptPlans : payPlans
    list.unshift(p)
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (method === 'PUT' && url.startsWith('/api/payment-plans/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    let idx = receiptPlans.findIndex(c => c.id === id)
    let list = receiptPlans
    if (idx < 0) { idx = payPlans.findIndex(c => c.id === id); list = payPlans }
    if (idx >= 0) list[idx] = { ...list[idx], ...body, id }
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (method === 'DELETE' && url.startsWith('/api/payment-plans/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    let idx = receiptPlans.findIndex(c => c.id === id)
    if (idx >= 0) receiptPlans.splice(idx, 1)
    else { idx = payPlans.findIndex(c => c.id === id); if (idx >= 0) payPlans.splice(idx, 1) }
    return jsonResponse({ code: 200, msg: '删除成功' })
  }

  // 资金记录
  if (url.startsWith('/api/payment-records') && method === 'GET') {
    const urlObj = new URL(url, 'http://localhost')
    const direction = urlObj.searchParams.get('direction') || 'receipt'
    const page = parseInt(urlObj.searchParams.get('page') || '1')
    const size = parseInt(urlObj.searchParams.get('size') || '10')
    const list = direction === 'receipt' ? receiptRecords : receiptRecords // simplified
    return jsonResponse({ code: 200, data: paginate(list, page, size) })
  }
  if (url === '/api/payment-records' && method === 'POST') {
    const r = { ...body }
    r.id = Math.max(...receiptRecords.map(x => x.id), 0) + 1
    receiptRecords.unshift(r)
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (method === 'PUT' && url.startsWith('/api/payment-records/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    const idx = receiptRecords.findIndex(c => c.id === id)
    if (idx >= 0) receiptRecords[idx] = { ...receiptRecords[idx], ...body, id }
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (method === 'DELETE' && url.startsWith('/api/payment-records/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    const idx = receiptRecords.findIndex(c => c.id === id)
    if (idx >= 0) receiptRecords.splice(idx, 1)
    return jsonResponse({ code: 200, msg: '删除成功' })
  }

  // 项目列表
  if (url === '/api/projects' && method === 'GET') {
    const page = 1, size = 10
    return jsonResponse({ code: 200, data: paginate(projects, page, size) })
  }
  if (url === '/api/projects' && method === 'POST') {
    const p = { ...body }
    p.id = Math.max(...projects.map(x => x.id), 0) + 1
    p.status = p.status || 'init'
    projects.unshift(p)
    return jsonResponse({ code: 200, msg: '保存成功', data: p })
  }
  if (method === 'PUT' && url.startsWith('/api/projects/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    const idx = projects.findIndex(c => c.id === id)
    if (idx >= 0) projects[idx] = { ...projects[idx], ...body, id }
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (method === 'DELETE' && url.startsWith('/api/projects/') && !isNaN(Number(url.split('/')[3]))) {
    const id = Number(url.split('/')[3])
    const idx = projects.findIndex(c => c.id === id)
    if (idx >= 0) projects.splice(idx, 1)
    return jsonResponse({ code: 200, msg: '删除成功' })
  }

  // 客户列表
  if (url === '/api/customers' && method === 'GET') {
    return jsonResponse({ code: 200, data: paginate(customers, 1, 20) })
  }
  if (url === '/api/customers' && method === 'POST') {
    const c = { ...body }
    c.id = Math.max(...customers.map(x => x.id), 0) + 1
    c.type = c.type || 'customer'
    customers.unshift(c)
    return jsonResponse({ code: 200, msg: '保存成功', data: c })
  }

  // 供应商列表
  if (url === '/api/suppliers' && method === 'GET') {
    return jsonResponse({ code: 200, data: paginate(suppliers, 1, 20) })
  }
  if (url === '/api/suppliers' && method === 'POST') {
    const s = { ...body }
    s.id = Math.max(...suppliers.map(x => x.id), 0) + 1
    s.type = 'supplier'
    suppliers.unshift(s)
    return jsonResponse({ code: 200, msg: '保存成功', data: s })
  }

  // 用户列表
  if (url === '/api/users' && method === 'GET') {
    return jsonResponse({ code: 200, data: paginate(users, 1, 20) })
  }

  // 角色列表
  if (url === '/api/roles' && method === 'GET') {
    return jsonResponse({ code: 200, data: paginate(roles, 1, 20) })
  }

  // 部门列表
  if (url === '/api/depts' && method === 'GET') {
    const depts = [
      { id: 1, deptName: '总经办', deptCode: '-', parentId: null, manager: '-', phone: '-', sortOrder: 1, status: 'enabled', remark: '' },
      { id: 2, deptName: '财务部', deptCode: 'FINANCE', parentId: null, manager: '王会计', phone: '13800138003', sortOrder: 2, status: 'enabled', remark: '财务管理部门' },
      { id: 3, deptName: '销售部', deptCode: 'SALES', parentId: null, manager: '李经理', phone: '13800138002', sortOrder: 5, status: 'enabled', remark: '销售业务部门' },
    ]
    return jsonResponse({ code: 200, data: depts })
  }

  // 字典列表
  if (url.startsWith('/api/settings/dict') && method === 'GET') {
    const urlObj = new URL(url, 'http://localhost')
    const dictType = urlObj.searchParams.get('type') || 'contract_type'
    const dicts: Record<string, any[]> = {
      contract_type: [
        { id: 1, dictType: 'contract_type', label: '销售合同', value: '', sortOrder: 1, status: 'enabled', receivableDefault: true, payableDefault: false, remark: '产品销售相关合同' },
        { id: 2, dictType: 'contract_type', label: '采购合同', value: '', sortOrder: 2, status: 'enabled', receivableDefault: false, payableDefault: true, remark: '原材料采购相关合同' },
        { id: 3, dictType: 'contract_type', label: '服务合同', value: '', sortOrder: 3, status: 'enabled', remark: '技术服务相关合同' },
        { id: 4, dictType: 'contract_type', label: '北斗续费', value: '', sortOrder: 4, status: 'enabled', remark: '' },
        { id: 5, dictType: 'contract_type', label: 'AI智能体实施', value: '', sortOrder: 5, status: 'enabled', remark: '' },
      ],
      project_type: [
        { id: 10, dictType: 'project_type', label: '研发项目', sortOrder: 1, status: 'enabled' },
        { id: 11, dictType: 'project_type', label: '实施项目', sortOrder: 2, status: 'enabled' },
        { id: 12, dictType: 'project_type', label: '维护项目', sortOrder: 3, status: 'enabled' },
      ],
      payment_method: [
        { id: 20, dictType: 'payment_method', label: '农业银行', sortOrder: 1, status: 'enabled', isDefault: true },
        { id: 21, dictType: 'payment_method', label: '工商银行', sortOrder: 2, status: 'enabled' },
        { id: 22, dictType: 'payment_method', label: '微信支付', sortOrder: 3, status: 'enabled' },
        { id: 23, dictType: 'payment_method', label: '现金', sortOrder: 4, status: 'enabled' },
      ],
      expense_type: [
        { id: 30, dictType: 'expense_type', label: '电费', sortOrder: 0, status: 'enabled' },
        { id: 31, dictType: 'expense_type', label: '材料费', sortOrder: 1, status: 'enabled' },
        { id: 32, dictType: 'expense_type', label: '设备费', sortOrder: 2, status: 'enabled' },
        { id: 33, dictType: 'expense_type', label: '人工费', sortOrder: 3, status: 'enabled' },
      ],
      tax_rate: [
        { id: 40, dictType: 'tax_rate', label: '13%', value: '0.13', sortOrder: 1, status: 'enabled' },
        { id: 41, dictType: 'tax_rate', label: '9%', value: '0.09', sortOrder: 2, status: 'enabled' },
        { id: 42, dictType: 'tax_rate', label: '6%', value: '0.06', sortOrder: 3, status: 'enabled' },
        { id: 43, dictType: 'tax_rate', label: '3%', value: '0.03', sortOrder: 4, status: 'enabled' },
        { id: 44, dictType: 'tax_rate', label: '1%', value: '0.01', sortOrder: 5, status: 'enabled', isDefault: true },
        { id: 45, dictType: 'tax_rate', label: '0%', value: '0', sortOrder: 6, status: 'enabled' },
      ],
    }
    return jsonResponse({ code: 200, data: dicts[dictType] || [] })
  }

  // 经营明细
  if (url === '/api/statistics/overview' && method === 'GET') {
    const totalReceivable = receivableContracts.reduce((s, c) => s + c.contractAmount, 0)
    const totalPayable = payableContracts.reduce((s, c) => s + c.contractAmount, 0)
    const invoiceTax = outputInvoices.reduce((s, i) => s + i.taxAmount, 0)
    const receiptTax = inputInvoices.reduce((s, i) => s + i.taxAmount, 0)
    const totalReceived = receivableContracts.reduce((s, c) => s + c.receivedAmount, 0)
    const totalPaid = payableContracts.reduce((s, c) => s + c.paidAmount, 0)
    const totalInvoiced = outputInvoices.reduce((s, i) => s + i.amountWithTax, 0)
    const totalUninvoiced = totalReceivable - totalInvoiced
    const totalReceivedInvoice = inputInvoices.reduce((s, i) => s + i.amountWithTax, 0)
    const totalUnpaid = totalPayable - totalPaid
    return jsonResponse({ code: 200, data: {
      summary: [
        { label:'应收合同总金额', value:'¥' + totalReceivable.toLocaleString(), suffix:'', color:'#1890ff' },
        { label:'收款比例', value: ((totalReceived/totalReceivable)*100).toFixed(1) + '%', suffix:'', color:'#52c41a' },
        { label:'应付合同总金额', value:'¥' + totalPayable.toLocaleString(), suffix:'', color:'#ff4d4f' },
        { label:'付款比例', value: ((totalPaid/totalPayable)*100).toFixed(1) + '%', suffix:'', color:'#faad14' },
      ],
      receivable: [
        { indicator:'应收合同', amount:'¥' + totalReceivable.toLocaleString(), rate:'—' },
        { indicator:'已开票', amount:'¥' + totalInvoiced.toLocaleString(), rate: ((totalInvoiced/totalReceivable)*100).toFixed(1) + '%' },
        { indicator:'开票税额', amount:'¥' + invoiceTax.toLocaleString(), rate:'—' },
        { indicator:'未开票', amount:'¥' + Math.max(0,totalUninvoiced).toLocaleString(), rate:'—' },
        { indicator:'已收款', amount:'¥' + totalReceived.toLocaleString(), rate: ((totalReceived/totalReceivable)*100).toFixed(1) + '%' },
        { indicator:'未收款', amount:'¥' + Math.max(0,totalReceivable-totalReceived).toLocaleString(), rate:'—' },
      ],
      payable: [
        { indicator:'应付合同', amount:'¥' + totalPayable.toLocaleString(), rate:'—' },
        { indicator:'已收票', amount:'¥' + totalReceivedInvoice.toLocaleString(), rate: ((totalReceivedInvoice/totalPayable)*100).toFixed(1) + '%' },
        { indicator:'收票专票税额', amount:'¥' + receiptTax.toLocaleString(), rate:'—' },
        { indicator:'未收票', amount:'¥' + Math.max(0,totalPayable-totalReceivedInvoice).toLocaleString(), rate:'—' },
        { indicator:'已付款', amount:'¥' + totalPaid.toLocaleString(), rate: ((totalPaid/totalPayable)*100).toFixed(1) + '%' },
        { indicator:'未付款', amount:'¥' + Math.max(0,totalUnpaid).toLocaleString(), rate:'—' },
      ],
    }})
  }

  // 图表分析
  if (url === '/api/statistics/chart' && method === 'GET') {
    return jsonResponse({ code: 200, data: {
      trend: [12, 28, 35, 42, 38, 45],
      receivableTypes: [{ name:'销售合同', value:16 }, { name:'服务合同', value:7 }, { name:'采购合同', value:1 }, { name:'北斗续费', value:1 }],
      payableTypes: [{ name:'采购合同', value:8 }, { name:'销售合同', value:2 }, { name:'服务合同', value:3 }],
      income: [120, 200, 150, 80, 70, 110],
      expense: [30, 50, 40, 20, 15, 25],
    }})
  }

  // 项目统计
  if (url.startsWith('/api/statistics/projects') && method === 'GET') {
    const urlObj = new URL(url, 'http://localhost')
    const page = parseInt(urlObj.searchParams.get('page') || '1')
    const size = parseInt(urlObj.searchParams.get('size') || '10')
    const list = projects.map(p => ({
      projectName: p.projectName,
      projectNo: p.projectNo,
      budgetAmount: p.budgetAmount,
      receivableAmount: receivableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.contractAmount, 0),
      settlementAmount: receivableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.settlementAmount, 0),
      receivedAmount: receivableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.receivedAmount, 0),
      invoicedAmount: receivableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.invoicedAmount, 0),
      unreceivedAmount: receivableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.unreceivedAmount, 0),
      uninvoicedAmount: receivableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.uninvoicedAmount, 0),
      revenueRate: 0,
      invoiceRate: 0,
      payableAmount: payableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.contractAmount, 0),
      paidAmount: payableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.paidAmount, 0),
      receivedInvoice: payableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.receivedInvoiceAmount, 0),
      unpaidAmount: payableContracts.filter(c => c.projectNo === p.projectNo).reduce((s,c) => s + c.unpaidAmount, 0),
      profit: 0,
      profitMargin: 0,
      paidProfit: 0,
    }))
    return jsonResponse({ code: 200, data: { records: paginate(list, page, size).records, total: list.length, page, size } })
  }

  // 公司列表（公司管理 + 登录选择）
  const companiesStore = [
    { id:1, companyName:'成者为译', taxId:'91350100MA12345', phone:'0591-1234567', address:'福建省福州市', invoiceTitle:'成者为译科技有限公司', bankName:'中国银行', bankAccount:'6217001000123456', isDefault:true },
    { id:2, companyName:'筱凡网络科技', taxId:'91350200MA67890', phone:'0592-7654321', address:'福建省厦门市', invoiceTitle:'筱凡网络科技有限公司', bankName:'农业银行', bankAccount:'6217002000678901', isDefault:false },
    { id:3, companyName:'苏州诺码智能', taxId:'91320500MA99999', phone:'0512-88886666', address:'江苏省苏州市', invoiceTitle:'苏州诺码智能科技有限公司', bankName:'工商银行', bankAccount:'6217003000999999', isDefault:false },
  ]

  if (url === '/api/settings/companies' && method === 'GET') {
    return jsonResponse({ code: 200, data: companiesStore })
  }
  if (url === '/api/settings/companies' && method === 'POST') {
    const newCompany = { id: companiesStore.length + 1, ...body, isDefault: companiesStore.length === 0 }
    companiesStore.push(newCompany)
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (url.startsWith('/api/settings/companies/') && method === 'PUT' && url.endsWith('/default')) {
    companiesStore.forEach(c => c.isDefault = false)
    const id = parseInt(url.split('/')[4])
    const company = companiesStore.find(c => c.id === id)
    if (company) company.isDefault = true
    return jsonResponse({ code: 200, msg: '设置成功' })
  }
  if (url.startsWith('/api/settings/companies/') && method === 'PUT') {
    const id = parseInt(url.split('/')[4])
    const idx = companiesStore.findIndex(c => c.id === id)
    if (idx >= 0) {
      companiesStore[idx] = { ...companiesStore[idx], ...body }
    }
    return jsonResponse({ code: 200, msg: '保存成功' })
  }
  if (url.startsWith('/api/settings/companies/') && method === 'DELETE') {
    const id = parseInt(url.split('/')[4])
    const idx = companiesStore.findIndex(c => c.id === id)
    if (idx >= 0) companiesStore.splice(idx, 1)
    return jsonResponse({ code: 200, msg: '删除成功' })
  }
  // 兼容旧的单公司接口
  if (url === '/api/settings/company' && method === 'GET') {
    return jsonResponse({ code: 200, data: companiesStore[0] })
  }
  if (url === '/api/settings/company' && method === 'PUT') {
    return jsonResponse({ code: 200, msg:'保存成功' })
  }

  // 资金账户
  if (url === '/api/settings/fund-accounts' && method === 'GET') {
    return jsonResponse({ code: 200, data: [
      { id:1, accountName:'基本户', accountNo:'621700123456', bankName:'农业银行', type:'company', status:'enabled', remark:'' },
      { id:2, accountName:'一般户', accountNo:'621700789012', bankName:'工商银行', type:'company', status:'enabled', remark:'' },
    ]})
  }

  // 岗位
  if (url === '/api/posts' && method === 'GET') {
    return jsonResponse({ code: 200, data: [
      { id:1, postName:'总经理', postCode:'CEO', sortOrder:1, status:'enabled', remark:'' },
      { id:2, postName:'部门经理', postCode:'MGR', sortOrder:2, status:'enabled', remark:'' },
      { id:3, postName:'普通员工', postCode:'EMP', sortOrder:3, status:'enabled', remark:'' },
    ]})
  }

  // 登录日志
  if (url === '/api/logs/login' && method === 'GET') {
    return jsonResponse({ code: 200, data: [
      { id:1, username:'admin', ip:'192.168.1.1', status:'success', message:'登录成功', loginTime:'2026-06-26 15:33:28' },
      { id:2, username:'admin', ip:'192.168.1.1', status:'success', message:'登录成功', loginTime:'2026-06-25 09:12:05' },
      { id:3, username:'zhangsan', ip:'192.168.1.2', status:'fail', message:'密码错误', loginTime:'2026-06-25 08:30:00' },
    ]})
  }

  // 操作日志
  if (url === '/api/logs/operation' && method === 'GET') {
    return jsonResponse({ code: 200, data: [
      { id:1, username:'admin', module:'合同管理', action:'新建', target:'HT202512001', detail:'新建应收合同', ip:'192.168.1.1', operationTime:'2026-06-26 15:35:00' },
      { id:2, username:'admin', module:'发票管理', action:'编辑', target:'111117', detail:'编辑销项发票', ip:'192.168.1.1', operationTime:'2026-06-26 14:20:00' },
    ]})
  }

  // 选项（给合同页面的下拉框）
  if (url === '/api/options' && method === 'GET') {
    return jsonResponse({ code: 200, data: {
      projects: projects.map(p => p.projectName).filter(Boolean),
      customers: customers.map(c => c.name).filter(Boolean),
      suppliers: suppliers.map(s => s.name).filter(Boolean),
    }})
  }

  // 默认返回
  return jsonResponse({ code: 404, msg: 'API not found: ' + url })
}

function jsonResponse(data: any): Response {
  return new Response(JSON.stringify(data), {
    headers: { 'Content-Type': 'application/json' }
  })
}

// ---------- Install Mock Interceptor ----------
const originalFetch = window.fetch.bind(window)
window.fetch = async (input: RequestInfo | URL, init?: RequestInit): Promise<Response> => {
  const url = typeof input === 'string' ? input : (input as Request).url
  if (url.startsWith('/api/')) {
    return mockHandler(url, init)
  }
  return originalFetch(input, init)
}

export { }
