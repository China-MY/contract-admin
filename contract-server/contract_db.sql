/*
 Navicat Premium Dump SQL

 Source Server         : 公司内网
 Source Server Type    : MySQL
 Source Server Version : 80410 (8.4.10)
 Source Host           : 10.10.32.210:3306
 Source Schema         : contract_db

 Target Server Type    : MySQL
 Target Server Version : 80410 (8.4.10)
 File Encoding         : 65001

 Date: 30/06/2026 15:29:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for contracts
-- ----------------------------
DROP TABLE IF EXISTS `contracts`;
CREATE TABLE `contracts`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_amount` decimal(18, 2) NULL DEFAULT NULL,
  `contract_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `counterparty` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `end_date` date NULL DEFAULT NULL,
  `invoice_progress` decimal(5, 2) NULL DEFAULT NULL,
  `invoiced_amount` decimal(18, 2) NULL DEFAULT NULL,
  `invoiced_not_received` decimal(18, 2) NULL DEFAULT NULL,
  `manager` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `our_company` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `paid_amount` decimal(18, 2) NULL DEFAULT NULL,
  `paid_not_received_invoice` decimal(18, 2) NULL DEFAULT NULL,
  `paid_profit` decimal(18, 2) NULL DEFAULT NULL,
  `payment_progress` decimal(5, 2) NULL DEFAULT NULL,
  `payment_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pricing_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `procurement_contract_amount` decimal(18, 2) NULL DEFAULT NULL,
  `procurement_paid_amount` decimal(18, 2) NULL DEFAULT NULL,
  `procurement_received_invoice` decimal(18, 2) NULL DEFAULT NULL,
  `procurement_settlement_amount` decimal(18, 2) NULL DEFAULT NULL,
  `profit` decimal(18, 2) NULL DEFAULT NULL,
  `profit_margin` decimal(5, 2) NULL DEFAULT NULL,
  `project_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `project_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `receipt_progress` decimal(5, 2) NULL DEFAULT NULL,
  `receipt_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `received_amount` decimal(18, 2) NULL DEFAULT NULL,
  `received_invoice_amount` decimal(18, 2) NULL DEFAULT NULL,
  `received_invoice_not_paid` decimal(18, 2) NULL DEFAULT NULL,
  `received_invoice_progress` decimal(5, 2) NULL DEFAULT NULL,
  `received_not_invoiced` decimal(18, 2) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `settlement_amount` decimal(18, 2) NULL DEFAULT NULL,
  `sign_date` date NULL DEFAULT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uninvoiced_amount` decimal(18, 2) NULL DEFAULT NULL,
  `unpaid_amount` decimal(18, 2) NULL DEFAULT NULL,
  `unreceived_amount` decimal(18, 2) NULL DEFAULT NULL,
  `unreceived_invoice_amount` decimal(18, 2) NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of contracts
-- ----------------------------

-- ----------------------------
-- Table structure for customers
-- ----------------------------
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_person` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `bank_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bank_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tax_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of customers
-- ----------------------------

-- ----------------------------
-- Table structure for invoices
-- ----------------------------
DROP TABLE IF EXISTS `invoices`;
CREATE TABLE `invoices`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount_with_tax` decimal(18, 2) NULL DEFAULT NULL,
  `amount_without_tax` decimal(18, 2) NULL DEFAULT NULL,
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `invoice_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `issue_date` date NULL DEFAULT NULL,
  `issuer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `issuer_tax_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `receiver` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `receiver_tax_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `tax_amount` decimal(18, 2) NULL DEFAULT NULL,
  `tax_rate` decimal(5, 2) NULL DEFAULT NULL,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `voucher_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of invoices
-- ----------------------------

-- ----------------------------
-- Table structure for notification_config
-- ----------------------------
DROP TABLE IF EXISTS `notification_config`;
CREATE TABLE `notification_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `channel_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `secret` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `webhook_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notification_config
-- ----------------------------
INSERT INTO `notification_config` VALUES (1, 'dingtalk', '钉钉提醒', '2026-06-30 10:02:41.524011', '', '', 'SECb92c00a445c29093efec51802bc2f683da73641cda0ccecacf115370650b1325', 'enabled', '2026-06-30 10:02:41.524011', 'https://oapi.dingtalk.com/robot/send?access_token=a7301c2a35423cbf31218873ad93c209084b89e910094302469d519ac356c46f');

-- ----------------------------
-- Table structure for notification_log
-- ----------------------------
DROP TABLE IF EXISTS `notification_log`;
CREATE TABLE `notification_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `channel_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `milestone_id` bigint NULL DEFAULT NULL,
  `remind_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sent_at` datetime(6) NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notification_log
-- ----------------------------

-- ----------------------------
-- Table structure for payment_plans
-- ----------------------------
DROP TABLE IF EXISTS `payment_plans`;
CREATE TABLE `payment_plans`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contract_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `invoice_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `paid_amount` decimal(18, 2) NULL DEFAULT NULL,
  `payee` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `plan_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `planned_amount` decimal(18, 2) NULL DEFAULT NULL,
  `planned_date` date NULL DEFAULT NULL,
  `progress` decimal(5, 2) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `unpaid_amount` decimal(18, 2) NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of payment_plans
-- ----------------------------

-- ----------------------------
-- Table structure for payment_records
-- ----------------------------
DROP TABLE IF EXISTS `payment_records`;
CREATE TABLE `payment_records`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amount` decimal(18, 2) NULL DEFAULT NULL,
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contract_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `expense_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `invoice_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payee` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payee_bank` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payer` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payer_bank` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `record_date` date NULL DEFAULT NULL,
  `record_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `voucher_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of payment_records
-- ----------------------------

-- ----------------------------
-- Table structure for project_milestones
-- ----------------------------
DROP TABLE IF EXISTS `project_milestones`;
CREATE TABLE `project_milestones`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_end_date` date NULL DEFAULT NULL,
  `actual_start_date` date NULL DEFAULT NULL,
  `assignee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `deliverable_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `planned_end_date` date NULL DEFAULT NULL,
  `planned_start_date` date NULL DEFAULT NULL,
  `progress` decimal(5, 2) NULL DEFAULT NULL,
  `project_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `project_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stage_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stage_order` int NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_milestones
-- ----------------------------

-- ----------------------------
-- Table structure for projects
-- ----------------------------
DROP TABLE IF EXISTS `projects`;
CREATE TABLE `projects`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `attachment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `budget_amount` decimal(18, 2) NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `customer_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `end_date` date NULL DEFAULT NULL,
  `manager` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `project_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `project_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `project_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `source` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `start_date` date NULL DEFAULT NULL,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `year` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of projects
-- ----------------------------

-- ----------------------------
-- Table structure for sys_company
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bank_account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bank_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `invoice_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_default` bit(1) NULL DEFAULT NULL,
  `logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `tax_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_company
-- ----------------------------
INSERT INTO `sys_company` VALUES (1, '南京市鼓楼区广东路38号物联网科技园大楼13楼', '10111001040011227', '农业银行四牌楼支行', '南京盛网信息科技有限公司', '2026-06-29 09:44:53.753801', '南京盛网信息科技有限公司', b'1', NULL, '025-58065808', NULL, '91320113593546745G', '2026-06-29 09:55:09.155417');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `dept_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `manager` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` bigint NULL DEFAULT NULL,
  `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `sort_order` int NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '2026-06-29 10:22:46.852814', '-', '总经办', NULL, NULL, NULL, NULL, 0, 'enabled', '2026-06-29 10:22:46.852814');
INSERT INTO `sys_dept` VALUES (2, '2026-06-29 10:22:47.008497', 'FINANCE', '财务部', NULL, NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:22:47.008497');
INSERT INTO `sys_dept` VALUES (3, '2026-06-29 10:22:47.042642', 'SALES', '销售部', NULL, NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:22:47.042642');
INSERT INTO `sys_dept` VALUES (4, '2026-06-29 10:22:47.065039', 'TECH', '技术部', NULL, NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:22:47.065039');

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_default` bit(1) NULL DEFAULT NULL,
  `label` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `payable_default` bit(1) NULL DEFAULT NULL,
  `receivable_default` bit(1) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `sort_order` int NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, '2026-06-29 09:44:53.783401', 'contract_type', NULL, '销售合同', b'0', b'1', NULL, 1, 'enabled', '2026-06-29 09:44:53.783401', NULL);
INSERT INTO `sys_dict_item` VALUES (2, '2026-06-29 09:44:53.811285', 'contract_type', NULL, '采购合同', b'1', b'0', NULL, 2, 'enabled', '2026-06-29 09:44:53.811285', NULL);
INSERT INTO `sys_dict_item` VALUES (3, '2026-06-29 09:44:53.824319', 'contract_type', NULL, '服务合同', b'0', b'0', NULL, 3, 'enabled', '2026-06-29 09:44:53.824319', NULL);
INSERT INTO `sys_dict_item` VALUES (4, '2026-06-29 09:44:53.839388', 'tax_rate', NULL, '13%', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 09:44:53.839388', '0.13');
INSERT INTO `sys_dict_item` VALUES (5, '2026-06-29 09:44:53.850914', 'tax_rate', NULL, '9%', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 09:44:53.850914', '0.09');
INSERT INTO `sys_dict_item` VALUES (6, '2026-06-29 09:44:53.870990', 'tax_rate', NULL, '6%', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 09:44:53.870990', '0.06');
INSERT INTO `sys_dict_item` VALUES (7, '2026-06-29 09:44:53.892059', 'tax_rate', NULL, '3%', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 09:44:53.892059', '0.03');
INSERT INTO `sys_dict_item` VALUES (8, '2026-06-29 09:44:53.907114', 'tax_rate', b'1', '1%', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 09:44:53.907114', '0.01');
INSERT INTO `sys_dict_item` VALUES (9, '2026-06-29 09:44:53.922166', 'tax_rate', NULL, '0%', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 09:44:53.922166', '0');
INSERT INTO `sys_dict_item` VALUES (10, '2026-06-29 10:02:07.062435', 'contract_type', NULL, '测试合同类型', NULL, NULL, NULL, 10, 'enabled', '2026-06-29 10:02:07.062435', NULL);
INSERT INTO `sys_dict_item` VALUES (11, '2026-06-29 10:04:45.838597', 'project_type', NULL, '研发项目', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:04:45.838597', NULL);
INSERT INTO `sys_dict_item` VALUES (12, '2026-06-29 10:04:45.920999', 'project_type', NULL, '实施项目', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:04:45.920999', NULL);
INSERT INTO `sys_dict_item` VALUES (13, '2026-06-29 10:04:45.937530', 'project_type', NULL, '维护项目', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:45.937530', NULL);
INSERT INTO `sys_dict_item` VALUES (14, '2026-06-29 10:04:45.950057', 'project_type', NULL, '咨询项目', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:04:45.950057', NULL);
INSERT INTO `sys_dict_item` VALUES (15, '2026-06-29 10:04:45.957578', 'project_type', NULL, '其他', NULL, NULL, NULL, 5, 'enabled', '2026-06-29 10:04:45.957578', NULL);
INSERT INTO `sys_dict_item` VALUES (16, '2026-06-29 10:04:45.975627', 'project_source', NULL, '客户介绍', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:04:45.975627', NULL);
INSERT INTO `sys_dict_item` VALUES (17, '2026-06-29 10:04:45.999675', 'project_source', NULL, '公开招标', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:04:45.999675', NULL);
INSERT INTO `sys_dict_item` VALUES (18, '2026-06-29 10:04:46.009203', 'project_source', NULL, '邀请招标', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:46.009203', NULL);
INSERT INTO `sys_dict_item` VALUES (19, '2026-06-29 10:04:46.019728', 'project_source', NULL, '自研项目', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:04:46.019728', NULL);
INSERT INTO `sys_dict_item` VALUES (20, '2026-06-29 10:04:46.033776', 'region', NULL, '华东', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:04:46.033776', NULL);
INSERT INTO `sys_dict_item` VALUES (21, '2026-06-29 10:04:46.043290', 'region', NULL, '华南', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:04:46.043290', NULL);
INSERT INTO `sys_dict_item` VALUES (22, '2026-06-29 10:04:46.053375', 'region', NULL, '华北', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:46.053375', NULL);
INSERT INTO `sys_dict_item` VALUES (23, '2026-06-29 10:04:46.061383', 'region', NULL, '华中', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:04:46.061383', NULL);
INSERT INTO `sys_dict_item` VALUES (24, '2026-06-29 10:04:46.069905', 'region', NULL, '西南', NULL, NULL, NULL, 5, 'enabled', '2026-06-29 10:04:46.069905', NULL);
INSERT INTO `sys_dict_item` VALUES (25, '2026-06-29 10:04:46.080434', 'region', NULL, '西北', NULL, NULL, NULL, 6, 'enabled', '2026-06-29 10:04:46.080434', NULL);
INSERT INTO `sys_dict_item` VALUES (26, '2026-06-29 10:04:46.093122', 'region', NULL, '东北', NULL, NULL, NULL, 7, 'enabled', '2026-06-29 10:04:46.093122', NULL);
INSERT INTO `sys_dict_item` VALUES (27, '2026-06-29 10:04:46.111215', 'payment_method', b'1', '银行转账', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:04:46.111215', NULL);
INSERT INTO `sys_dict_item` VALUES (28, '2026-06-29 10:04:46.124263', 'payment_method', NULL, '现金', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:04:46.124263', NULL);
INSERT INTO `sys_dict_item` VALUES (29, '2026-06-29 10:04:46.139788', 'payment_method', NULL, '微信支付', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:46.139788', NULL);
INSERT INTO `sys_dict_item` VALUES (30, '2026-06-29 10:04:46.155838', 'payment_method', NULL, '支付宝', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:04:46.155838', NULL);
INSERT INTO `sys_dict_item` VALUES (31, '2026-06-29 10:04:46.163353', 'payment_method', NULL, '支票', NULL, NULL, NULL, 5, 'enabled', '2026-06-29 10:04:46.163353', NULL);
INSERT INTO `sys_dict_item` VALUES (32, '2026-06-29 10:04:46.174879', 'payment_method', NULL, '汇票', NULL, NULL, NULL, 6, 'enabled', '2026-06-29 10:04:46.174879', NULL);
INSERT INTO `sys_dict_item` VALUES (33, '2026-06-29 10:04:46.189400', 'expense_type', NULL, '材料费', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:04:46.189400', NULL);
INSERT INTO `sys_dict_item` VALUES (34, '2026-06-29 10:04:46.201425', 'expense_type', NULL, '人工费', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:04:46.201425', NULL);
INSERT INTO `sys_dict_item` VALUES (35, '2026-06-29 10:04:46.209445', 'expense_type', NULL, '设备费', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:46.209445', NULL);
INSERT INTO `sys_dict_item` VALUES (36, '2026-06-29 10:04:46.218968', 'expense_type', NULL, '运输费', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:04:46.218968', NULL);
INSERT INTO `sys_dict_item` VALUES (37, '2026-06-29 10:04:46.225499', 'expense_type', NULL, '管理费', NULL, NULL, NULL, 5, 'enabled', '2026-06-29 10:04:46.226497', NULL);
INSERT INTO `sys_dict_item` VALUES (38, '2026-06-29 10:04:46.233020', 'expense_type', NULL, '税费', NULL, NULL, NULL, 6, 'enabled', '2026-06-29 10:04:46.233020', NULL);
INSERT INTO `sys_dict_item` VALUES (39, '2026-06-29 10:04:46.240028', 'expense_type', NULL, '租赁费', NULL, NULL, NULL, 7, 'enabled', '2026-06-29 10:04:46.240028', NULL);
INSERT INTO `sys_dict_item` VALUES (40, '2026-06-29 10:04:46.248543', 'expense_type', NULL, '电费', NULL, NULL, NULL, 8, 'enabled', '2026-06-29 10:04:46.248543', NULL);
INSERT INTO `sys_dict_item` VALUES (41, '2026-06-29 10:04:46.256071', 'expense_type', NULL, '其他', NULL, NULL, NULL, 9, 'enabled', '2026-06-29 10:04:46.256071', NULL);
INSERT INTO `sys_dict_item` VALUES (42, '2026-06-29 10:04:46.268594', 'bank', NULL, '中国银行', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:04:46.268594', NULL);
INSERT INTO `sys_dict_item` VALUES (43, '2026-06-29 10:04:46.282630', 'bank', NULL, '工商银行', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:04:46.282630', NULL);
INSERT INTO `sys_dict_item` VALUES (44, '2026-06-29 10:04:46.300166', 'bank', b'1', '农业银行', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:46.300166', NULL);
INSERT INTO `sys_dict_item` VALUES (45, '2026-06-29 10:04:46.313562', 'bank', NULL, '建设银行', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:04:46.313562', NULL);
INSERT INTO `sys_dict_item` VALUES (46, '2026-06-29 10:04:46.321072', 'bank', NULL, '交通银行', NULL, NULL, NULL, 5, 'enabled', '2026-06-29 10:04:46.321072', NULL);
INSERT INTO `sys_dict_item` VALUES (47, '2026-06-29 10:04:46.332609', 'bank', NULL, '招商银行', NULL, NULL, NULL, 6, 'enabled', '2026-06-29 10:04:46.332609', NULL);
INSERT INTO `sys_dict_item` VALUES (48, '2026-06-29 10:04:46.342133', 'bank', NULL, '浦发银行', NULL, NULL, NULL, 7, 'enabled', '2026-06-29 10:04:46.342133', NULL);
INSERT INTO `sys_dict_item` VALUES (49, '2026-06-29 10:04:46.355663', 'reminder_settings', NULL, '合同到期前7天提醒', NULL, NULL, NULL, 7, 'enabled', '2026-06-29 10:04:46.355663', NULL);
INSERT INTO `sys_dict_item` VALUES (50, '2026-06-29 10:04:46.413302', 'reminder_settings', NULL, '合同到期前30天提醒', NULL, NULL, NULL, 30, 'enabled', '2026-06-29 10:04:46.413302', NULL);
INSERT INTO `sys_dict_item` VALUES (51, '2026-06-29 10:04:46.441362', 'reminder_settings', NULL, '收款到期前3天提醒', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:04:46.441362', NULL);
INSERT INTO `sys_dict_item` VALUES (52, '2026-06-29 10:04:46.528808', 'reminder_settings', NULL, '收款到期前7天提醒', NULL, NULL, NULL, 7, 'enabled', '2026-06-29 10:04:46.528808', NULL);
INSERT INTO `sys_dict_item` VALUES (53, '2026-06-29 10:05:37.333397', 'payment_settings', NULL, '启用收付款计划', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 10:05:37.334396', NULL);
INSERT INTO `sys_dict_item` VALUES (54, '2026-06-29 10:05:37.443630', 'payment_settings', NULL, '启用收付款确认', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 10:05:37.443630', NULL);
INSERT INTO `sys_dict_item` VALUES (55, '2026-06-29 10:05:37.454161', 'payment_settings', NULL, '启用项目功能', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 10:05:37.454161', NULL);
INSERT INTO `sys_dict_item` VALUES (56, '2026-06-29 10:05:37.461189', 'payment_settings', NULL, '启用合同地址', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 10:05:37.461189', NULL);
INSERT INTO `sys_dict_item` VALUES (57, '2026-06-29 10:05:37.473657', 'payment_settings', NULL, '启用利润统计', NULL, NULL, NULL, 5, 'enabled', '2026-06-29 10:05:37.473657', NULL);
INSERT INTO `sys_dict_item` VALUES (58, '2026-06-29 13:59:15.619545', 'invoice_type', NULL, '专用发票', NULL, NULL, NULL, 1, 'enabled', '2026-06-29 13:59:15.619545', NULL);
INSERT INTO `sys_dict_item` VALUES (59, '2026-06-29 13:59:15.746094', 'invoice_type', NULL, '普通发票', NULL, NULL, NULL, 2, 'enabled', '2026-06-29 13:59:15.746094', NULL);
INSERT INTO `sys_dict_item` VALUES (60, '2026-06-29 13:59:15.760927', 'invoice_type', NULL, '电子发票', NULL, NULL, NULL, 3, 'enabled', '2026-06-29 13:59:15.760927', NULL);
INSERT INTO `sys_dict_item` VALUES (61, '2026-06-29 13:59:15.766935', 'invoice_type', NULL, '增值税发票', NULL, NULL, NULL, 4, 'enabled', '2026-06-29 13:59:15.766935', NULL);

-- ----------------------------
-- Table structure for sys_fund_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_fund_account`;
CREATE TABLE `sys_fund_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `account_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `account_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `bank_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `balance` decimal(18, 2) NULL DEFAULT NULL,
  `is_default` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_fund_account
-- ----------------------------
INSERT INTO `sys_fund_account` VALUES (2, '基本户', '10111001040011227', '农业银行四牌楼支行', '2026-06-29 09:59:58.413025', '', 'enabled', 'company', '2026-06-29 09:59:58.413025', NULL, NULL);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_time` datetime(6) NULL DEFAULT NULL,
  `message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1, '2026-06-29 11:04:41.456398', '192.168.1.1', '2026-06-29 09:30:00.000000', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (2, '2026-06-29 11:04:41.596340', '192.168.1.2', '2026-06-29 08:15:00.000000', '登录成功', 'success', 'zhangsan');
INSERT INTO `sys_login_log` VALUES (3, '2026-06-29 11:04:41.612348', '192.168.1.3', '2026-06-28 18:00:00.000000', '密码错误', 'fail', 'admin');
INSERT INTO `sys_login_log` VALUES (4, '2026-06-29 11:04:41.624866', '192.168.1.4', '2026-06-28 09:00:00.000000', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (5, '2026-06-29 11:04:41.629866', '192.168.1.5', '2026-06-27 15:30:00.000000', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (6, '2026-06-29 13:05:50.187369', NULL, '2026-06-29 13:05:50.184368', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (7, '2026-06-29 15:49:57.240033', NULL, '2026-06-29 15:49:57.231838', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (8, '2026-06-29 15:59:48.996673', NULL, '2026-06-29 15:59:48.995676', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (9, '2026-06-29 16:00:56.237103', NULL, '2026-06-29 16:00:56.231469', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (10, '2026-06-29 16:03:23.480110', NULL, '2026-06-29 16:03:23.479103', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (11, '2026-06-29 16:04:16.294344', NULL, '2026-06-29 16:04:16.291816', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (12, '2026-06-29 16:04:17.572092', NULL, '2026-06-29 16:04:17.401450', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (13, '2026-06-29 16:05:10.056403', NULL, '2026-06-29 16:05:10.054394', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (14, '2026-06-29 16:05:49.435706', NULL, '2026-06-29 16:05:49.432266', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (15, '2026-06-29 16:06:06.063362', NULL, '2026-06-29 16:06:06.062351', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (16, '2026-06-29 16:06:13.733277', NULL, '2026-06-29 16:06:13.731770', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (17, '2026-06-29 16:08:01.485968', NULL, '2026-06-29 16:08:01.484860', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (18, '2026-06-29 17:06:31.905389', NULL, '2026-06-29 17:06:31.897867', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (19, '2026-06-29 17:10:39.426603', NULL, '2026-06-29 17:10:39.418969', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (20, '2026-06-29 17:10:45.032059', NULL, '2026-06-29 17:10:45.031060', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (21, '2026-06-29 17:23:00.565278', NULL, '2026-06-29 17:23:00.564150', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (22, '2026-06-30 09:03:21.879528', NULL, '2026-06-30 09:03:21.872433', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (23, '2026-06-30 09:03:41.963160', NULL, '2026-06-30 09:03:41.962158', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (24, '2026-06-30 10:12:55.998983', NULL, '2026-06-30 10:12:55.997959', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (25, '2026-06-30 10:13:03.305771', NULL, '2026-06-30 10:13:03.304090', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (26, '2026-06-30 10:14:55.337752', NULL, '2026-06-30 10:14:55.330070', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (27, '2026-06-30 10:17:28.327689', NULL, '2026-06-30 10:17:28.326684', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (28, '2026-06-30 10:18:05.703489', NULL, '2026-06-30 10:18:05.701288', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (29, '2026-06-30 10:23:09.262949', NULL, '2026-06-30 10:23:09.261170', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (30, '2026-06-30 10:23:19.016623', NULL, '2026-06-30 10:23:19.015924', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (31, '2026-06-30 10:23:32.746238', NULL, '2026-06-30 10:23:32.744229', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (32, '2026-06-30 10:25:12.816686', NULL, '2026-06-30 10:25:12.809172', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (33, '2026-06-30 10:25:22.794578', NULL, '2026-06-30 10:25:22.793577', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (34, '2026-06-30 10:25:28.513363', NULL, '2026-06-30 10:25:28.512359', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (35, '2026-06-30 10:28:12.134913', NULL, '2026-06-30 10:28:12.128202', '登录成功', 'success', 'admin');
INSERT INTO `sys_login_log` VALUES (36, '2026-06-30 14:43:23.199687', NULL, '2026-06-30 14:43:23.192805', '登录成功', 'success', 'admin');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `module` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operation_time` datetime(6) NULL DEFAULT NULL,
  `target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, '新建', '2026-06-29 11:04:41.645870', '新建应收合同', '192.168.1.1', '合同管理', '2026-06-29 09:00:00.000000', 'HT20260501', 'admin');
INSERT INTO `sys_operation_log` VALUES (2, '编辑', '2026-06-29 11:04:41.655865', '修改销项发票金额', '192.168.1.1', '发票管理', '2026-06-29 08:30:00.000000', 'INV2026001', 'admin');
INSERT INTO `sys_operation_log` VALUES (3, '修改', '2026-06-29 11:04:41.661865', '更新公司地址', '192.168.1.2', '系统设置', '2026-06-28 16:00:00.000000', '公司信息', 'zhangsan');
INSERT INTO `sys_operation_log` VALUES (4, '新增', '2026-06-29 17:04:41.454044', 'ContractController.createInvoice', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-29 17:04:41.449290', 'Invoice(id=5, contractNo=HT20260501, contractName=智慧校园平台开发合同, invoiceNo=INV-20260629-005, direction=...', 'admin');
INSERT INTO `sys_operation_log` VALUES (5, '修改', '2026-06-29 17:08:40.943203', 'ContractController.updateInvoice', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-29 17:08:40.941198', '1 | Invoice(id=1, contractNo=HT20260501, contractName=智慧校园平台开发合同, invoiceNo=INV2026001, direction=output...', 'admin');
INSERT INTO `sys_operation_log` VALUES (6, '修改', '2026-06-29 17:08:44.174859', 'ContractController.updateInvoice', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-29 17:08:44.172849', '1 | Invoice(id=1, contractNo=HT20260501, contractName=智慧校园平台开发合同, invoiceNo=INV2026001, direction=output...', 'admin');
INSERT INTO `sys_operation_log` VALUES (7, '新增', '2026-06-30 09:59:16.452007', 'ProjectMilestoneController.createMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 09:59:16.447872', 'ProjectMilestone(id=1, projectNo=NM260101, projectName=苏州爱华MES项目, stageName=111, stageOrder=1, deliv...', 'admin');
INSERT INTO `sys_operation_log` VALUES (8, '新增', '2026-06-30 10:02:41.639991', 'ProjectMilestoneController.createNotifyConfig', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:02:41.638480', 'NotificationConfig(id=1, configName=钉钉提醒, channelType=dingtalk, webhookUrl=https://oapi.dingtalk.com...', 'admin');
INSERT INTO `sys_operation_log` VALUES (9, '修改', '2026-06-30 10:05:57.671847', 'ProjectMilestoneController.updateMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:05:57.666732', '1 | ProjectMilestone(id=1, projectNo=NM260101, projectName=苏州爱华MES项目, stageName=111, stageOrder=1, deliv...', 'admin');
INSERT INTO `sys_operation_log` VALUES (10, '修改', '2026-06-30 10:06:06.141406', 'ProjectMilestoneController.updateMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:06:06.140407', '1 | ProjectMilestone(id=1, projectNo=NM260101, projectName=苏州爱华MES项目, stageName=111, stageOrder=1, deliv...', 'admin');
INSERT INTO `sys_operation_log` VALUES (11, '修改', '2026-06-30 10:10:34.337949', 'ProjectMilestoneController.updateNotifyConfig', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:10:34.337198', '1 | NotificationConfig(id=1, configName=钉钉提醒, channelType=dingtalk, webhookUrl=https://oapi.dingtalk.com...', 'admin');
INSERT INTO `sys_operation_log` VALUES (12, '新增', '2026-06-30 10:16:25.091583', 'ProjectMilestoneController.createMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:16:25.089571', 'ProjectMilestone(id=2, projectNo=LY-BTH-01, projectName=白炭黑绿化工程, stageName=, stageOrder=1, deliverab...', 'admin');
INSERT INTO `sys_operation_log` VALUES (13, '新增', '2026-06-30 10:16:31.476725', 'ProjectMilestoneController.createMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:16:31.476186', 'ProjectMilestone(id=3, projectNo=202603001, projectName=智慧校园项目, stageName=, stageOrder=1, deliverabl...', 'admin');
INSERT INTO `sys_operation_log` VALUES (14, '修改', '2026-06-30 10:16:54.043663', 'ProjectMilestoneController.updateNotifyConfig', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:16:54.043158', '1 | NotificationConfig(id=1, configName=钉钉提醒, channelType=dingtalk, webhookUrl=https://oapi.dingtalk.com...', 'admin');
INSERT INTO `sys_operation_log` VALUES (15, '修改', '2026-06-30 10:17:28.745755', 'ProjectMilestoneController.updateNotifyConfig', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:17:28.745223', '1 | NotificationConfig(id=null, configName=null, channelType=null, webhookUrl=null, secret=SECb92c00a445...', 'admin');
INSERT INTO `sys_operation_log` VALUES (16, '新增', '2026-06-30 10:18:53.761301', 'ProjectMilestoneController.createMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 10:18:53.759292', 'ProjectMilestone(id=4, projectNo=NM260101, projectName=苏州爱华MES项目, stageName=222, stageOrder=2, deliv...', 'admin');
INSERT INTO `sys_operation_log` VALUES (17, '修改', '2026-06-30 10:23:33.109723', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:23:33.108647', '[SystemConfig(id=6, configKey=test, configValue=1, description=null, createdAt=2026-06-30T10:23:33.0...', 'admin');
INSERT INTO `sys_operation_log` VALUES (18, '修改', '2026-06-30 10:25:22.974077', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:25:22.972424', '[SystemConfig(id=7, configKey=test2, configValue=123, description=null, createdAt=2026-06-30T10:25:2...', 'admin');
INSERT INTO `sys_operation_log` VALUES (19, '修改', '2026-06-30 10:25:23.028508', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:25:23.027257', '[SystemConfig(id=null, configKey=remind_days_before, configValue=5, description=null, createdAt=null...', 'admin');
INSERT INTO `sys_operation_log` VALUES (20, '修改', '2026-06-30 10:25:28.663035', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:25:28.661530', '[SystemConfig(id=null, configKey=remind_days_before, configValue=5, description=null, createdAt=null...', 'admin');
INSERT INTO `sys_operation_log` VALUES (21, '修改', '2026-06-30 10:25:28.954446', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:25:28.953437', '[SystemConfig(id=null, configKey=remind_days_before, configValue=5, description=null, createdAt=null...', 'admin');
INSERT INTO `sys_operation_log` VALUES (22, '修改', '2026-06-30 10:25:29.793396', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:25:29.792407', '[SystemConfig(id=null, configKey=remind_days_before, configValue=7, description=提前提醒天数, createdAt=nu...', 'admin');
INSERT INTO `sys_operation_log` VALUES (23, '修改', '2026-06-30 10:28:12.916902', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:28:12.915898', '[SystemConfig(id=null, configKey=remind_days_before, configValue=3, description=null, createdAt=null...', 'admin');
INSERT INTO `sys_operation_log` VALUES (24, '修改', '2026-06-30 10:29:11.240225', 'ContractController.updateSystemConfig', '0:0:0:0:0:0:0:1', 'Contract', '2026-06-30 10:29:11.239225', '[SystemConfig(id=null, configKey=remind_days_before, configValue=7, description=提前提醒天数, createdAt=nu...', 'admin');
INSERT INTO `sys_operation_log` VALUES (25, '修改', '2026-06-30 12:51:44.381452', 'ProjectMilestoneController.updateMilestone', '0:0:0:0:0:0:0:1', 'ProjectMilestone', '2026-06-30 12:51:44.377453', '1 | ProjectMilestone(id=1, projectNo=NM260101, projectName=苏州爱华MES项目, stageName=111, stageOrder=1, deliv...', 'admin');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `post_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `post_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `sort_order` int NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, '2026-06-29 10:22:47.104466', 'CEO', '总经理', NULL, 0, 'enabled', '2026-06-29 10:22:47.104466');
INSERT INTO `sys_post` VALUES (2, '2026-06-29 10:22:47.127613', 'MGR', '部门经理', NULL, 1, 'enabled', '2026-06-29 10:22:47.127613');
INSERT INTO `sys_post` VALUES (3, '2026-06-29 10:22:47.156453', 'EMP', '普通员工', NULL, 2, 'enabled', '2026-06-29 10:22:47.156453');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort_order` int NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `menu_codes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '2026-06-29 10:22:46.322174', '超级管理员角色', 'SUPER_ADMIN', '超级管理员', 0, 'enabled', '2026-06-29 10:22:46.322174', NULL);
INSERT INTO `sys_role` VALUES (2, '2026-06-29 10:22:46.585777', '普通用户角色', 'USER', '普通用户', 4, 'enabled', '2026-06-29 10:22:46.585777', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `dept_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `last_login_at` datetime(6) NULL DEFAULT NULL,
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `post_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `real_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `role_names` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_ids` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `company_names` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_51bvuyvihefoh4kp5syh2jpi4`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '2026-06-29 09:44:53.640873', '技术部', 'mingyuxuezhang@qq.com', '男', '2026-06-30 14:43:23.262776', '$2a$10$2e14.A/SvjjXrRpUozkcRO3VqTy9n8BQCX8EVdSkk9oRd9qJluhyO', '19895906930', '普通员工', '管理员', NULL, '超级管理员', 'normal', '2026-06-30 14:43:23.270374', 'admin', '1', '南京盛网信息科技有限公司');

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `config_value` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created_at` datetime(6) NULL DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updated_at` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_npsxm1erd0lbetjn5d3ayrsof`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, 'remind_days_before', '7', '2026-06-30 10:20:27.424420', '提前提醒天数', '2026-06-30 10:29:11.127533');
INSERT INTO `system_config` VALUES (2, 'enable_before_start', 'enabled', '2026-06-30 10:20:27.442332', '阶段开始前提醒', '2026-06-30 10:20:27.442332');
INSERT INTO `system_config` VALUES (3, 'enable_before_end', 'enabled', '2026-06-30 10:20:27.457948', '阶段到期前提醒', '2026-06-30 10:20:27.457948');
INSERT INTO `system_config` VALUES (4, 'enable_on_end', 'enabled', '2026-06-30 10:20:27.475734', '阶段到期日提醒', '2026-06-30 10:20:27.475734');
INSERT INTO `system_config` VALUES (5, 'enable_overdue', 'enabled', '2026-06-30 10:20:27.487813', '阶段超期提醒', '2026-06-30 10:29:11.208360');
INSERT INTO `system_config` VALUES (6, 'test', '1', '2026-06-30 10:23:33.099526', NULL, '2026-06-30 10:23:33.099526');
INSERT INTO `system_config` VALUES (7, 'test2', '123', '2026-06-30 10:25:22.953800', NULL, '2026-06-30 10:25:22.953800');
INSERT INTO `system_config` VALUES (8, 'remind_hour', '12', '2026-06-30 10:28:12.900087', NULL, '2026-06-30 10:29:11.226151');

SET FOREIGN_KEY_CHECKS = 1;
