package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rulin.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 友情链接表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FriendLink对象", description="友情链接表")
@TableName("b_friend_link")
public class FriendLink implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "网站名称")
    private String name;

    @ApiModelProperty(value = "网站地址")
    private String url;

    @ApiModelProperty(value = "网站头像地址")
    private String avatar;

    @ApiModelProperty(value = "网站描述")
    private String info;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态( 0,下架;1,申请;2:上架)")
    private Integer status;

    @ApiModelProperty(value = "下架原因")
    private String reason;

    @ApiModelProperty(value = "创建时间")
      @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
      @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;


}
