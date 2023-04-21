package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.rulin.util.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 照片
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_photo")
@ApiModel(value = "Photo对象", description = "照片")
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "相册id")
    private Integer albumId;

    @ApiModelProperty(value = "照片名")
    private String name;

    @ApiModelProperty(value = "照片描述")
    private String info;

    @ApiModelProperty(value = "照片地址")
    private String url;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date updateTime;


}
