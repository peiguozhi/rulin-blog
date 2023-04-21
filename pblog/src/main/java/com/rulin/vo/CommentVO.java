package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 评论
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 回复量
     */
    private Integer replyCount;

    /**
     * 回复列表
     */
    private List<ReplyVO> replyVOList;

}
