package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 社交登录token
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialTokenVO {

    /**
     * 开放id
     */
    private String openId;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 登录类型
     */
    private Integer loginType;

}

