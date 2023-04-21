package com.rulin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class QuestionPreviewVO {

    @JsonProperty("qCategoryList")
    @ApiModelProperty(value = "面试题分类")
    private List<QCategoryVO> qCategoryList;


    @ApiModelProperty(value = "面试题问题")
    private List<QuestionVO> questionList;

}
