package com.rulin.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
public class UserVO {

    private Integer id;

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

    private Integer loginType;

    /**
     * 昵称
     * */
    private String nickname;

    /**
     * 头像
     * */
    private String avatar;
}
