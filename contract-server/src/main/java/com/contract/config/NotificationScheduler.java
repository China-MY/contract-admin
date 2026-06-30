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

    public NotificationScheduler(ProjectMilestoneRepository milestoneRepository,
                                  NotificationConfigRepository configRepository,
                                  NotificationLogRepository logRepository) {
        this.milestoneRepository = milestoneRepository;
        this.configRepository = configRepository;
        this.logRepository = logRepository;
    }

    /** 每天 8:00 执行一次 */
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkMilestones() {
        List<NotificationConfig> configs = configRepository.findByStatus("enabled");
        if (configs.isEmpty()) return;

        List<ProjectMilestone> milestones = milestoneRepository.findByStatusNot("completed");
        LocalDate today = LocalDate.now();

        for (ProjectMilestone m : milestones) {
            String remindType = null;

            // 计划开始前 3 天提醒
            if (m.getPlannedStartDate() != null && m.getPlannedStartDate().minusDays(3).equals(today)) {
                remindType = "before_start";
            }
            // 计划结束前 3 天提醒
            if (m.getPlannedEndDate() != null && m.getPlannedEndDate().minusDays(3).equals(today)) {
                remindType = "before_end";
            }
            // 计划结束当天提醒
            if (m.getPlannedEndDate() != null && m.getPlannedEndDate().equals(today)) {
                remindType = "on_end";
            }
            // 已超期提醒（计划结束已过且状态不是 completed）
            if (m.getPlannedEndDate() != null && m.getPlannedEndDate().isBefore(today)
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
        return switch (cfg.getChannelType()) {
            case "feishu" -> sendWebhook(cfg.getWebhookUrl(),
                    "{\"msg_type\":\"text\",\"content\":{\"text\":\"" + escapeJson(text) + "\"}}");
            case "dingtalk" -> sendWebhook(cfg.getWebhookUrl(),
                    "{\"msgtype\":\"text\",\"text\":{\"content\":\"" + escapeJson(text) + "\"}}");
            case "email" -> true;
            default -> false;
        };
    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private boolean sendWebhook(String url, String json) {
        try {
            var conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            try (var os = conn.getOutputStream()) {
                os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
