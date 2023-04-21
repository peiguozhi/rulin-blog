package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rulin.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


/**
 * 面试题表
 *
 * @author 程序儒
 * @date 2023-04-13 17:14:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_question")
@ApiModel(value = "Question对象", description = "面试题表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;


    @JsonProperty("qCategoryId")
    @ApiModelProperty(value = "分类id")
    private Long qCategoryId;

    @ApiModelProperty(value = "面试题问题")
    private String quContent;

    @ApiModelProperty(value = "面试题答案")
    private String analysis;

    @ApiModelProperty(value = "面试题答案MD版")
    private String analysisMd;

    @ApiModelProperty(value = "发布状态 0：下架；1：上架")
    private Integer isPublish;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "面试题阅读量")
    private Integer quantity;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

}
