package com.rulin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
@ApiModel(description = "邮箱登录信息")
public class EmailLoginDTO {
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(name = "email", value = "email", required = true, dataType = "String")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(name = "password", value = "password", required = true, dataType = "String")
    private String password;

}
