package com.contract.config;

import com.contract.entity.*;
import com.contract.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class NotificationScheduler {

    private final ProjectMilestoneRepository milestoneRepository;
    private final NotificationConfigRepository configRepository;
    private final NotificationLogRepository logRepository;
    private final SystemConfigRepository systemConfigRepository;

    public NotificationScheduler(ProjectMilestoneRepository milestoneRepository,
                                  NotificationConfigRepository configRepository,
                                  NotificationLogRepository logRepository,
                                  SystemConfigRepository systemConfigRepository) {
        this.milestoneRepository = milestoneRepository;
        this.configRepository = configRepository;
        this.logRepository = logRepository;
        this.systemConfigRepository = systemConfigRepository;
    }

    /** 每天 8:00 执行一次 */
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkMilestones() {
        List<NotificationConfig> configs = configRepository.findByStatus("enabled");
        if (configs.isEmpty()) return;

        // 读取提醒配置
        int daysBefore = getConfigInt("remind_days_before", 3);
        boolean enableBeforeStart = "enabled".equals(getConfig("enable_before_start", "enabled"));
        boolean enableBeforeEnd = "enabled".equals(getConfig("enable_before_end", "enabled"));
        boolean enableOnEnd = "enabled".equals(getConfig("enable_on_end", "enabled"));
        boolean enableOverdue = "enabled".equals(getConfig("enable_overdue", "enabled"));

        List<ProjectMilestone> milestones = milestoneRepository.findByStatusNot("completed");
        LocalDate today = LocalDate.now();

        for (ProjectMilestone m : milestones) {
            String remindType = null;

            // 计划开始前 N 天提醒
            if (enableBeforeStart && m.getPlannedStartDate() != null && m.getPlannedStartDate().minusDays(daysBefore).equals(today)) {
                remindType = "before_start";
            }
            // 计划结束前 N 天提醒
            if (enableBeforeEnd && m.getPlannedEndDate() != null && m.getPlannedEndDate().minusDays(daysBefore).equals(today)) {
                remindType = "before_end";
            }
            // 计划结束当天提醒
            if (enableOnEnd && m.getPlannedEndDate() != null && m.getPlannedEndDate().equals(today)) {
                remindType = "on_end";
            }
            // 已超期提醒
            if (enableOverdue && m.getPlannedEndDate() != null && m.getPlannedEndDate().isBefore(today)
                    && !"completed".equals(m.getStatus()) && !"delayed".equals(m.getStatus())) {
                remindType = "overdue";
            }

            if (remindType == null) continue;

            for (NotificationConfig cfg : configs) {
                try {
                    String text = buildNotifyText(m, remindType);
                    boolean ok = doSend(cfg, text);
                    NotificationLog log = new NotificationLog();
                    log.setMilestoneId(m.getId());
                    log.setChannelType(cfg.getChannelType());
                    log.setRemindType(remindType);
                    log.setStatus(ok ? "success" : "fail");
                    log.setMessage(ok ? "自动发送成功" : "发送失败");
                    log.setSentAt(java.time.LocalDateTime.now());
                    logRepository.save(log);
                } catch (Exception ignored) {}
            }
        }
    }

    private String getConfig(String key, String defaultValue) {
        SystemConfig cfg = systemConfigRepository.findByConfigKey(key);
        return cfg != null ? cfg.getConfigValue() : defaultValue;
    }

    private int getConfigInt(String key, int defaultValue) {
        try { return Integer.parseInt(getConfig(key, String.valueOf(defaultValue))); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    private String buildNotifyText(ProjectMilestone m, String remindType) {
        String title = switch (remindType) {
            case "before_start" -> "【项目阶段即将开始】";
            case "before_end" -> "【项目阶段即将到期】";
            case "on_end" -> "【项目阶段今日到期】";
            case "overdue" -> "【项目阶段已超期】";
            default -> "【项目阶段提醒】";
        };

        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append("项目: ").append(m.getProjectName() != null ? m.getProjectName() : "").append("\n");
        sb.append("阶段: ").append(m.getStageName() != null ? m.getStageName() : "").append("\n");
        if (m.getDeliverableContent() != null && !m.getDeliverableContent().isEmpty())
            sb.append("交付内容: ").append(m.getDeliverableContent()).append("\n");
        if (m.getPlannedStartDate() != null)
            sb.append("计划开始: ").append(m.getPlannedStartDate()).append("\n");
        if (m.getPlannedEndDate() != null)
            sb.append("计划结束: ").append(m.getPlannedEndDate()).append("\n");
        sb.append("负责人: ").append(m.getAssignee() != null ? m.getAssignee() : "未指定").append("\n");
        return sb.toString();
    }

    private boolean doSend(NotificationConfig cfg, String text) {
        try {
            String json = switch (cfg.getChannelType()) {
                case "feishu" -> "{\"msg_type\":\"text\",\"content\":{\"text\":\"" + escapeJson(text) + "\"}}";
                case "dingtalk" -> "{\"msgtype\":\"text\",\"text\":{\"content\":\"" + escapeJson(text) + "\"}}";
                default -> null;
            };
            if (json == null) return false;
            var conn = (java.net.HttpURLConnection) new java.net.URL(cfg.getWebhookUrl()).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            try (var os = conn.getOutputStream()) {
                os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            if (code != 200) return false;
            try (var is = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
                String resp = is.lines().collect(java.util.stream.Collectors.joining());
                return resp.contains("\"errcode\":0") || resp.contains("\"code\":0") || !resp.contains("\"errcode\"");
            }
        } catch (Exception e) {
            return false;
        }
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}
