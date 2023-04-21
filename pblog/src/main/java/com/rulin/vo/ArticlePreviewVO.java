package com.rulin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticlePreviewVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图片")
    private String avatar;

    @ApiModelProperty(value = "内容MD版")
    private String contentMd;

    @ApiModelProperty(value = "是否置顶")
    private int isStick;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "分类Id")
    private Long categoryId;

    @ApiModelProperty(value = "分类名")
    private String categoryName;

    @ApiModelProperty(value = "标签")
    private List<TagVO> tagVOList;

}
