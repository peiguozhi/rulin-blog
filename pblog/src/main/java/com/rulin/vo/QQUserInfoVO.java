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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQUserInfoVO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * qq头像
     */
    private String figureurl_qq_1;


}
