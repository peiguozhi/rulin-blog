package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * BExceptionLog对象
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "BExceptionLog对象", description = "")
@TableName("b_exception_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "ip来源")
    private String ipSource;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "描述")
    private String operation;

    @ApiModelProperty(value = "参数")
    private String params;

    @ApiModelProperty(value = "异常对象json格式")
    private String exceptionJson;

    @ApiModelProperty(value = "异常简单信息,等同于e.getMessage")
    private String exceptionMessage;

    @ApiModelProperty(value = "发生时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
