package com.endlessovo.assistantGPT.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户注册 Query
 */
@Data
public class UserRegisterQuery {
    @NotBlank(message = "用户名不可为空")
    private String name;
    @NotBlank(message = "邮箱不可为空")
    private String email;
    @NotBlank(message = "密码不可为空")
    private String password;
}
