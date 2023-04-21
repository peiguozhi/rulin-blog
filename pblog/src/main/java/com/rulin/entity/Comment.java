package com.rulin.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 博客评论表
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("b_comment")
@Builder
@ApiModel(value = "Comment对象", description = "博客评论表")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论用户Id
     */
    private Long userId;

    /**
     * 回复用户id
     */
    private Integer replyUserId;

    /**
     * 评论文章id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}