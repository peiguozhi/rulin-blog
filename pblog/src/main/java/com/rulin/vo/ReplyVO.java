package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 回复列表
 *
 * @date 2023年4月9日
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyVO {

    /**
     * 评论id
     */
    private Integer id;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 用户id
     */
    private Integer userId;

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
     * 被回复用户id
     */
    private Integer replyUserId;

    /**
     * 被回复用户昵称
     */
    private String replyNickname;

    /**
     * 被回复个人网站
     */
    private String replyWebSite;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论时间
     */
    private Date createTime;

}
