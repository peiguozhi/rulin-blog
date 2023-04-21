package com.rulin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Boolean rememberMe;
}
