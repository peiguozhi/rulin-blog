package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微博token
 *
 * @author CodeScholar
 * @date 2023年4月9日
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeiboTokenVO {

    /**
     * 微博uid
     */
    private String uid;

    /**
     * 访问令牌
     */
    private String access_token;

}
