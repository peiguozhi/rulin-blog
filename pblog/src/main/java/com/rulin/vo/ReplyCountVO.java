package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyCountVO {

    /**
     * 评论id
     */
    private Integer commentId;

    /**
     * 回复数量
     */
    private Integer replyCount;

}
