package com.rulin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author 程序儒
 * @date 2023-08-21 03:03:54
 * @apiNote 笔记文章列表显示视图类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleBookVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图片")
    private String avatar;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}