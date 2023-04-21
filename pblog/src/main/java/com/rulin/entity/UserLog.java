package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "UserLog对象", description = "日志表")
@TableName("b_user_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "操作地址")
    private String address;

    @ApiModelProperty(value = "操作类型")
    private String type;

    @ApiModelProperty(value = "操作日志")
    private String description;

    @ApiModelProperty(value = "操作模块")
    private String model;

    @ApiModelProperty(value = "操作系统")
    private String accessOs;
    @ApiModelProperty(value = "客户端类型")
    private String clientType;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "操作时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "操作结果")
    private String result;


}
