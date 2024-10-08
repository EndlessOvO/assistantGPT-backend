package com.endlessovo.assistantGPT.controller;

import com.endlessovo.assistantGPT.common.ResponseVO;
import com.endlessovo.assistantGPT.model.vo.user.UserLoginQuery;
import com.endlessovo.assistantGPT.model.vo.user.UserLoginVO;
import com.endlessovo.assistantGPT.model.vo.user.UserRegisterQuery;
import com.endlessovo.assistantGPT.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 用户登录 - email + password 登录方式
     * @param userLoginQuery 用户登录 Query
     * @return 用户登录结果
     */
    @PostMapping("/login")
    public ResponseVO<UserLoginVO> login(@RequestBody UserLoginQuery userLoginQuery) {
        return ResponseVO.success(authService.login(userLoginQuery));
    }

    /**
     * 发送登录链接到 用户email
     *
     * @param email 用户email
     */
    @GetMapping("/send-login-email")
    public ResponseVO<Void> sendLoginEmail(@RequestParam("email")
                                               @NotBlank(message = "邮箱不可为空")
                                               String email) {
        authService.sendLoginEmail(email);
        return ResponseVO.success();
    }

    /**
     * 用户登录 - email邮件登录链接方式
     * @return 用户登录结果
     */
    @GetMapping("/email-login/{credentials}")
    public ResponseVO<UserLoginVO> login(@PathVariable String credentials) {
        return ResponseVO.success(authService.login(credentials));
    }

    @PostMapping("/register")
    public ResponseVO<Void> register(@RequestBody UserRegisterQuery userRegisterQuery) {
        authService.register(userRegisterQuery);
        return ResponseVO.success();
    }

    @GetMapping("/logout")
    public ResponseVO<Void> logout() {
        authService.logout();
        return ResponseVO.success();
    }

    @GetMapping("/logoff")
    public ResponseVO<Void> logoff() {
        authService.logoff();
        return ResponseVO.success();
    }
}
