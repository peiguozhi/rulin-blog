package com.rulin.strategy;

import com.rulin.vo.UserInfoVO;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
public interface SocialLoginStrategy {
    /**
     * 登录
     *
     * @param data 数据
     * @return {@link UserInfoVO} 用户信息
     */
    UserInfoVO login(String data);
}
