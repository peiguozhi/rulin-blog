package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rulin.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 博客面试题分类表
 * </p>
 *
 * @author 程序儒
 * @date 2023年4月13日 15点03分
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_q_category")
@ApiModel(value = "QCategory对象", description = "博客面试题分类表")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "面试题分类名称")
    private String name;

    @ApiModelProperty(value = "点击量")
    private Integer clickVolume;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private int questionCount;
}
