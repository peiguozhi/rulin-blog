package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "SystemConfig对象", description = "系统配置表")
@TableName("b_system_config")
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "七牛云公钥")
    private String qiNiuAccessKey;

    @ApiModelProperty(value = "七牛云私钥")
    private String qiNiuSecretKey;

    @ApiModelProperty(value = "七牛云上传空间")
    private String qiNiuBucket;

    @ApiModelProperty(value = "七牛云存储区域 华东（z0），华北(z1)，华南(z2)，北美(na0)，东南亚(as0)")
    private String qiNiuArea;

    @ApiModelProperty(value = "七牛云域名前缀：http://img.rulint.com/")
    private String qiNiuPictureBaseUrl;

    @ApiModelProperty(value = "是否开启邮件通知(0:否， 1:是)")
    private String startEmailNotification;

    @ApiModelProperty(value = "是否开启仪表盘通知(0:否， 1:是)")
    private String openDashboardNotification;

    @ApiModelProperty(value = "仪表盘通知【用于首次登录弹框】")
    private String dashboardNotification;

    @ApiModelProperty(value = "仪表盘通知【用于首次登录弹框】MD")
    private String dashboardNotificationMd;

    @ApiModelProperty(value = "搜索模式【0:SQL搜索 、1：全文检索】")
    private int searchModel;

    @ApiModelProperty(value = "是否开启注册用户邮件激活(0:否， 1:是)")
    private String openEmailActivate;

    @ApiModelProperty(value = "文件上传七牛云(0:否， 1:是)")
    private String uploadQiNiu;

    @ApiModelProperty(value = "邮箱地址")
    private String emailHost;
    @ApiModelProperty(value = "邮箱发件人")
    private String emailUsername;
    @ApiModelProperty(value = "邮箱授权码")
    private String emailPassword;
    @ApiModelProperty(value = "邮箱端口")
    private int emailPort;
    @ApiModelProperty(value = "启用邮箱发送")
    private int openEmail;

    @ApiModelProperty(value = "本地文件地址")
    private String localFileUrl;
    @ApiModelProperty(value = "文件上传方式 0:本地 1：七牛云")
    private int fileUploadWay;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "阿里云AccessKey")
    private String aliYunAccessKey;
    @ApiModelProperty(value = "阿里云SecretKey")
    private String aliYunSecretKey;
    @ApiModelProperty(value = "阿里云Bucket名称")
    private String aliYunBucket;
    @ApiModelProperty(value = "阿里云Endpoint")
    private String aliYunEndpoint;

}
