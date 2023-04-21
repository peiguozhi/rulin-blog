package com.rulin.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rulin.util.DateUtils;
import lombok.Data;

import java.util.Date;


/**
 * 面试题dto
 *
 * @author 程序儒
 * @date 2023-04-13 17:17:50
 */
@Data
public class QuestionDTO {
    private Long id;
    private Long userId;
    private String quContent;
    private Integer quantity;
    private String analysis;
    private String analysisMd;
    private Integer sort;

    @JsonProperty("qCategoryName")
    private String qCategoryName;
    private Integer isPublish;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;
}
