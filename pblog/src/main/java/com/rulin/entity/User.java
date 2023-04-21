package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统管理-用户基础信息表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("b_user")
@ApiModel(value = "User对象", description = "系统管理-用户基础信息表")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "账号")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "状态")
    private Integer status;


    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "最后登录时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date lastLoginTime;

    @ApiModelProperty(value = "角色ID")
    private Integer roleId;

    @ApiModelProperty(value = "IP地址")
    private String ipAddress;

    @ApiModelProperty(value = "IP来源")
    private String ipSource;

    @ApiModelProperty(value = "登录系统")
    private String os;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "用户信息id")
    private Integer userAuthId;

    @ApiModelProperty(value = "登录类型")
    private Integer loginType;

}
