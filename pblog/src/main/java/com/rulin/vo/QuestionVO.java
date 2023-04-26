package com.rulin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 程序儒
 * @version 1.0.0
 * @date 2023/4/15
 * @apiNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "面试题问题")
    private String quContent;

    @ApiModelProperty(value = "面试题答案MD版")
    private String analysisMd;

    @ApiModelProperty(value = "是否收藏")
    private Integer isFavorite;

}