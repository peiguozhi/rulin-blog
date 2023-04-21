package com.rulin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rulin.util.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 面试题列表
 *
 * @author 程序儒
 * @date 2023-04-13 17:11:42
 */
@Data
public class QuestionListVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "面试题标题")
    private String quContent;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "面试题阅读量")
    private Integer quantity;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtils.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "状态")
    private Integer isPublish;

    @JsonProperty("qCategoryName")
    @ApiModelProperty(value = "分类名")
    private String qCategoryName;

    @JsonProperty("qCategoryId")
    @ApiModelProperty(value = "分类id")
    private String qCategoryId;
}
