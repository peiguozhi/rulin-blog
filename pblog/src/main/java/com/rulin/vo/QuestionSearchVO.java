package com.rulin.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * 问题搜索
 *
 * @author 程序儒
 * @date 2023-04-17 14:15:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "question")
public class QuestionSearchVO {

    /**
     * 问题id
     */
    @Id
    private Long id;

    /**
     * 问题分类
     */
    @JsonProperty("qCategoryId")
    private Long qCategoryId;

    /**
     * 问题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String quContent;

    /**
     * 答案内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String analysis;
}
