package com.rulin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微博用户信息
 *
 * @author CodeScholar
 * @date 2023年4月9日
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeiboUserInfoVO {

    /**
     * 昵称
     */
    private String screen_name;

    /**
     * 头像
     */
    private String avatar_hd;

}
