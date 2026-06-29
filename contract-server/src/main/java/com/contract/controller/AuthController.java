package com.contract.controller;

import com.contract.common.Result;
import com.contract.config.JwtUtil;
import com.contract.entity.User;
import com.contract.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final com.contract.repository.LoginLogRepository loginLogRepository;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder,
                          com.contract.repository.LoginLogRepository loginLogRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.loginLogRepository = loginLogRepository;
    }

    private void recordLoginLog(String username, String status, String message) {
        com.contract.entity.LoginLog log = new com.contract.entity.LoginLog();
        log.setUsername(username);
        log.setStatus(status);
        log.setMessage(message);
        log.setLoginTime(java.time.LocalDateTime.now());
        loginLogRepository.save(log);
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            recordLoginLog(username, "fail", "密码错误");
            return Result.error(401, "用户名或密码错误");
        }

        recordLoginLog(username, "success", "登录成功");
        String token = jwtUtil.generateToken(username);
        return Result.ok(Map.of(
            "token", token,
            "user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "realName", user.getRealName(),
                "roleNames", user.getRoleNames(),
                "companyIds", user.getCompanyIds() != null ? user.getCompanyIds() : "",
                "companyNames", user.getCompanyNames() != null ? user.getCompanyNames() : ""
            )
        ));
    }
}
