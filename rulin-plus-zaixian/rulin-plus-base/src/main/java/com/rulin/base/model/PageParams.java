package com.rulin.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author 程序儒
 * @version 1.0.0
 * @date 2023/8/11
 * @apiNote 分页查询通用参数
 */
@Data
@ToString
public class PageParams {
    //当前页码
    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;

    //每页记录数默认值
    @ApiModelProperty("每页记录数默认值")
    private Long pageSize =10L;

    public PageParams(){

    }

    public PageParams(long pageNo,long pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
