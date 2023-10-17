package com.rulin.media.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


/**
 * 媒资文件查询请求模型类
 *
 * @author 程序儒
 * @date 2023-08-25 18:25:05
 */
@Data
@ToString
public class QueryMediaParamsDto {

    @ApiModelProperty("媒资文件名称")
    private String filename;
    @ApiModelProperty("媒资类型")
    private String fileType;
    @ApiModelProperty("审核状态")
    private String auditStatus;
}
