package com.rulin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rulin.util.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
public class ArticleListVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "文章标题")
    private String title;

    @ApiModelProperty(value = "文章封面地址")
    private String avatar;

    @ApiModelProperty(value = "是否是私密文章 0 否 1是")
    private Integer isSecret;

    @ApiModelProperty(value = "是否置顶 0否 1是")
    private Integer isStick;

    @ApiModelProperty(value = "是否原创 0：转载 1:原创")
    private Integer isOriginal;

    @ApiModelProperty(value = "文章阅读量")
    private Integer quantity;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtils.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "状态")
    private Integer isPublish;

    @ApiModelProperty(value = "分类名")
    private String categoryName;

    @ApiModelProperty(value = "标签名集合")
    private String tagNames;
}
