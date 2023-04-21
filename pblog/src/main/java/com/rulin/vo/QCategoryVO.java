package com.rulin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QCategoryVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分类名")
    private String name;

    @ApiModelProperty(value = "面试题数")
    private int questionNum;
}
