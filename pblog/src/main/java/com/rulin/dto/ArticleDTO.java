package com.rulin.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rulin.util.DateUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
public class ArticleDTO {
    private Long id;
    private Long userId;
    private String title;
    private String avatar;
    private String summary;
    private Integer quantity;
    private String content;
    private String contentMd;
    private Integer isSecret;
    private Integer isStick;
    private Integer isOriginal;
    private String originalUrl;
    private String remark;
    private String keywords;
    private String categoryName;
    private Integer isPublish;
    private List<String> tags;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;
}
