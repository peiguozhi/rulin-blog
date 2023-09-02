package com.rulin.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author 程序儒
 * @version 1.0.0
 * @date 2023/8/11
 * @apiNote 课程查询参数Dto
 */
@Data
@ToString
@ApiModel("课程查询条件参数")
public class QueryCourseParamsDto {

    //审核状态
    @ApiModelProperty("审核状态")
    private String auditStatus;
    //课程名称
    @ApiModelProperty("课程名称")
    private String courseName;
    //发布状态
    @ApiModelProperty("发布状态")
    private String publishStatus;

}
