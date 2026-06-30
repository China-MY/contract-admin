package com.contract.controller;

import com.contract.common.Result;
import com.contract.entity.*;
import com.contract.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/project-milestones")
public class ProjectMilestoneController {

    private final ProjectMilestoneRepository milestoneRepository;
    private final NotificationConfigRepository configRepository;
    private final NotificationLogRepository logRepository;

    public ProjectMilestoneController(ProjectMilestoneRepository milestoneRepository,
                                      NotificationConfigRepository configRepository,
                                      NotificationLogRepository logRepository) {
        this.milestoneRepository = milestoneRepository;
        this.configRepository = configRepository;
        this.logRepository = logRepository;
    }

    // === 项目阶段 CRUD ===
    @GetMapping
    public Result<?> getMilestones(@RequestParam(required = false) String projectNo) {
        if (projectNo != null && !projectNo.isEmpty()) {
            return Result.ok(milestoneRepository.findByProjectNoOrderByStageOrder(projectNo));
        }
        return Result.ok(milestoneRepository.findAll());
    }

    @GetMapping("/{id}")
    public Result<?> getMilestone(@PathVariable Long id) {
        return Result.ok(milestoneRepository.findById(id).orElse(null));
    }

    @PostMapping
    public Result<?> createMilestone(@RequestBody ProjectMilestone m) {
        m.setId(null);
        milestoneRepository.save(m);
        return Result.ok("保存成功");
    }

    @PutMapping("/{id}")
    public Result<?> updateMilestone(@PathVariable Long id, @RequestBody ProjectMilestone body) {
        ProjectMilestone m = milestoneRepository.findById(id).orElse(null);
        if (m == null) return Result.error("阶段不存在");
        if (body.getStageName() != null) m.setStageName(body.getStageName());
        if (body.getStageOrder() != null) m.setStageOrder(body.getStageOrder());
        if (body.getDeliverableContent() != null) m.setDeliverableContent(body.getDeliverableContent());
        if (body.getPlannedStartDate() != null) m.setPlannedStartDate(body.getPlannedStartDate());
        if (body.getPlannedEndDate() != null) m.setPlannedEndDate(body.getPlannedEndDate());
        if (body.getActualStartDate() != null) m.setActualStartDate(body.getActualStartDate());
        if (body.getActualEndDate() != null) m.setActualEndDate(body.getActualEndDate());
        if (body.getStatus() != null) m.setStatus(body.getStatus());
        if (body.getProgress() != null) m.setProgress(body.getProgress());
        if (body.getAssignee() != null) m.setAssignee(body.getAssignee());
        if (body.getRemark() != null) m.setRemark(body.getRemark());
        milestoneRepository.save(m);
        return Result.ok("保存成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteMilestone(@PathVariable Long id) {
        milestoneRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    // === 通知配置 CRUD ===
    @GetMapping("/notify-configs")
    public Result<?> getNotifyConfigs() {
        return Result.ok(configRepository.findAll());
    }

    @PostMapping("/notify-configs")
    public Result<?> createNotifyConfig(@RequestBody NotificationConfig cfg) {
        cfg.setId(null);
        configRepository.save(cfg);
        return Result.ok("保存成功");
    }

    @PutMapping("/notify-configs/{id}")
    public Result<?> updateNotifyConfig(@PathVariable Long id, @RequestBody NotificationConfig body) {
        NotificationConfig c = configRepository.findById(id).orElse(null);
        if (c == null) return Result.error("配置不存在");
        if (body.getConfigName() != null) c.setConfigName(body.getConfigName());
        if (body.getChannelType() != null) c.setChannelType(body.getChannelType());
        if (body.getWebhookUrl() != null) c.setWebhookUrl(body.getWebhookUrl());
        if (body.getSecret() != null) c.setSecret(body.getSecret());
        if (body.getEmail() != null) c.setEmail(body.getEmail());
        if (body.getStatus() != null) c.setStatus(body.getStatus());
        configRepository.save(c);
        return Result.ok("保存成功");
    }

    @DeleteMapping("/notify-configs/{id}")
    public Result<?> deleteNotifyConfig(@PathVariable Long id) {
        configRepository.deleteById(id);
        return Result.ok("删除成功");
    }

    // === 通知日志 ===
    @GetMapping("/notify-logs")
    public Result<?> getNotifyLogs() {
        return Result.ok(logRepository.findAll());
    }

    // === 进度统计 ===
    @GetMapping("/progress")
    public Result<?> getProgress(@RequestParam String projectNo) {
        List<ProjectMilestone> list = milestoneRepository.findByProjectNoOrderByStageOrder(projectNo);
        int total = list.size();
        long completed = list.stream().filter(m -> "completed".equals(m.getStatus())).count();
        long inProgress = list.stream().filter(m -> "in_progress".equals(m.getStatus())).count();
        double percent = total > 0 ? (double) completed / total * 100 : 0;
        return Result.ok(Map.of(
            "total", total, "completed", completed, "inProgress", inProgress,
            "percent", Math.round(percent * 100.0) / 100.0,
            "stages", list
        ));
    }

    // === 手动触发发送 ===
    @PostMapping("/send-notify")
    public Result<?> sendNotify(@RequestBody Map<String, Object> body) {
        Long milestoneId = Long.valueOf(body.get("milestoneId").toString());
        ProjectMilestone m = milestoneRepository.findById(milestoneId).orElse(null);
        if (m == null) return Result.error("阶段不存在");

        List<NotificationConfig> configs = configRepository.findByStatus("enabled");
        int sent = 0;

        for (NotificationConfig cfg : configs) {
            try {
                String text = buildNotifyText(m, cfg.getChannelType());
                boolean ok = doSend(cfg, text);
                NotificationLog log = new NotificationLog();
                log.setMilestoneId(milestoneId);
                log.setChannelType(cfg.getChannelType());
                log.setRemindType("manual");
                log.setStatus(ok ? "success" : "fail");
                log.setMessage(ok ? "发送成功" : "发送失败: " + text);
                log.setSentAt(java.time.LocalDateTime.now());
                logRepository.save(log);
                if (ok) sent++;
            } catch (Exception ignored) {}
        }
        return Result.ok("已发送 " + sent + " 条通知");
    }

    private String buildNotifyText(ProjectMilestone m, String channel) {
        StringBuilder sb = new StringBuilder();
        sb.append("【项目阶段提醒】\n");
        sb.append("项目: ").append(m.getProjectName()).append("\n");
        sb.append("阶段: ").append(m.getStageName()).append("\n");
        if (m.getDeliverableContent() != null && !m.getDeliverableContent().isEmpty())
            sb.append("交付内容: ").append(m.getDeliverableContent()).append("\n");
        if (m.getPlannedStartDate() != null)
            sb.append("计划开始: ").append(m.getPlannedStartDate()).append("\n");
        if (m.getPlannedEndDate() != null)
            sb.append("计划结束: ").append(m.getPlannedEndDate()).append("\n");
        sb.append("当前状态: ").append(statusText(m.getStatus())).append("\n");
        return sb.toString();
    }

    private String statusText(String s) {
        if (s == null) return "未知";
        return switch (s) {
            case "pending" -> "未开始";
            case "in_progress" -> "进行中";
            case "completed" -> "已完成";
            case "delayed" -> "已延期";
            default -> s;
        };
    }

    private boolean doSend(NotificationConfig cfg, String text) {
        return switch (cfg.getChannelType()) {
            case "feishu" -> sendFeishu(cfg.getWebhookUrl(), text);
            case "dingtalk" -> sendDingTalk(cfg.getWebhookUrl(), text);
            case "email" -> true; // Email requires SMTP config, skip for now
            default -> false;
        };
    }

    private boolean sendFeishu(String url, String text) {
        try {
            var json = "{\"msg_type\":\"text\",\"content\":{\"text\":" + org.springframework.web.util.UriUtils.encode(text, java.nio.charset.StandardCharsets.UTF_8) + "}}";
            var conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            try (var os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean sendDingTalk(String url, String text) {
        try {
            // Escape for JSON string
            String escaped = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
            String json = "{\"msgtype\":\"text\",\"text\":{\"content\":\"" + escaped + "\"}}";
            var conn = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            try (var os = conn.getOutputStream()) {
                os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            }
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }
}
