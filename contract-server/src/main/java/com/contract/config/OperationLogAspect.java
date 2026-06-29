package com.contract.config;

import com.contract.entity.OperationLog;
import com.contract.repository.OperationLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogRepository operationLogRepository;

    public OperationLogAspect(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @Pointcut("within(com.contract.controller..*) && !within(com.contract.controller.AuthController)")
    public void controllerMethods() {}

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logOperation(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();

            // Only log write operations: save, create, update, delete, remove, setDefault, reset
            if (!methodName.matches("^(save|create|add|update|set|delete|remove|reset).*")) {
                return;
            }

            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = request.getRemoteAddr();
            String username = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "anonymous";

            String action;
            if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
                action = "删除";
            } else if (methodName.startsWith("update") || methodName.startsWith("set") || methodName.startsWith("reset")) {
                action = "修改";
            } else {
                action = "新增";
            }

            String className = joinPoint.getTarget().getClass().getSimpleName();
            String module = className.replace("Controller", "");

            // Build target description from method args
            StringBuilder target = new StringBuilder();
            for (Object arg : joinPoint.getArgs()) {
                if (arg != null && !(arg instanceof HttpServletRequest)) {
                    String argStr = arg.toString();
                    if (argStr.length() > 100) argStr = argStr.substring(0, 100) + "...";
                    if (target.length() > 0) target.append(" | ");
                    target.append(argStr);
                }
            }
            if (target.length() == 0) target.append(methodName);

            OperationLog log = new OperationLog();
            log.setUsername(username);
            log.setModule(module);
            log.setAction(action);
            log.setTarget(target.toString().trim());
            log.setDetail(className + "." + methodName);
            log.setIp(ip);
            log.setOperationTime(LocalDateTime.now());
            operationLogRepository.save(log);
        } catch (Exception ignored) {
            // Silently ignore logging errors to avoid disrupting business flow
        }
    }
}
