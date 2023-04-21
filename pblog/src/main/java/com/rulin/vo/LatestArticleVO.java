package com.rulin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
public class LatestArticleVO {
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面地址")
    private String avatar;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
