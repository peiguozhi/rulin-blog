package com.rulin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CategoryBookVO extends CategoryVO {

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "是否为笔记")
    private int isBook;

    @ApiModelProperty(value = "排序关键字")
    private String desc = "id";
}
