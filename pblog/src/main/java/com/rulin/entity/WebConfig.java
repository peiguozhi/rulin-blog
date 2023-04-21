package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 网站配置表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TWebConfig对象", description = "网站配置表")
@TableName("b_web_config")
public class WebConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "logo(文件UID)")
    private String logo;

    @ApiModelProperty(value = "网站名称")
    private String name;

    @ApiModelProperty(value = "网站地址")
    private String webUrl;

    @ApiModelProperty(value = "介绍")
    private String summary;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "备案号")
    private String recordNum;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "支付宝收款码FileId")
    private String aliPay;

    @ApiModelProperty(value = "微信收款码FileId")
    private String weixinPay;

    @ApiModelProperty(value = "github地址")
    private String github;

    @ApiModelProperty(value = "gitee地址")
    private String gitee;

    @ApiModelProperty(value = "QQ号")
    private String qqNumber;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "显示的列表（用于控制邮箱、QQ、QQ群、Github、Gitee、微信是否显示在前端）")
    private String showList;

    @ApiModelProperty(value = "登录方式列表（用于控制前端登录方式，如账号密码,码云,Github,QQ,微信）")
    private String loginTypeList;

    @ApiModelProperty(value = "是否开启评论(0:否 1:是)")
    private String openComment;

    @ApiModelProperty(value = "是否开启赞赏(0:否， 1:是)")
    private String openAdmiration;

    @ApiModelProperty(value = "作者简介")
    private String authorInfo;

    @ApiModelProperty(value = "作者头像")
    private String authorAvatar;

    @ApiModelProperty(value = "游客头像")
    private String touristAvatar;

    @ApiModelProperty(value = "公告")
    private String bulletin;

    @ApiModelProperty(value = "关于我")
    private String aboutMe;

    @ApiModelProperty(value = "是否开启音乐播放器")
    private Integer isMusicPlayer;
}
