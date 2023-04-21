package com.rulin.strategy.imp;

import com.rulin.config.properties.WeiboConfigProperties;
import com.rulin.vo.SocialTokenVO;
import com.rulin.vo.SocialUserInfoVO;
import com.rulin.vo.WeiboTokenVO;
import com.rulin.vo.WeiboUserInfoVO;
import com.rulin.common.ResultCode;
import com.rulin.common.SocialLoginConst;
import com.rulin.enums.LoginTypeEnum;
import com.rulin.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微博登录策略实现
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service("weiboLoginStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WeiboLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    private static final Logger logger = LoggerFactory.getLogger(WeiboLoginStrategyImpl.class);

    private final RestTemplate restTemplate;

    private final WeiboConfigProperties weiboConfigProperties;

    @Override
    public SocialTokenVO getSocialToken(String code) {
        // 获取微博token信息
        WeiboTokenVO weiboToken = getWeiboToken(code);
        logger.info("weibo login as weiboToken :{}",weiboToken.toString());
        // 返回token信息
        return SocialTokenVO.builder()
                .openId(weiboToken.getUid())
                .accessToken(weiboToken.getAccess_token())
                .loginType(LoginTypeEnum.WEIBO.getType())
                .build();
    }

    @Override
    public SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialTokenVO) {
        // 定义请求参数
        Map<String, String> data = new HashMap<>(2);
        data.put(SocialLoginConst.UID, socialTokenVO.getOpenId());
        data.put(SocialLoginConst.ACCESS_TOKEN, socialTokenVO.getAccessToken());
        // 获取微博用户信息
        WeiboUserInfoVO weiboUserInfoVO = restTemplate.getForObject(weiboConfigProperties.getUserInfoUrl(), WeiboUserInfoVO.class, data);
        logger.info("weibo login as info :{}", weiboUserInfoVO.toString());
        // 返回用户信息
        return SocialUserInfoVO.builder()
                .nickname(Objects.requireNonNull(weiboUserInfoVO).getScreen_name())
                .avatar(weiboUserInfoVO.getAvatar_hd())
                .build();
    }

    /**
     * 获取微博token信息
     *
     * @param code 微博code
     * @return {@link WeiboTokenVO} 微博token
     */
    private WeiboTokenVO getWeiboToken(String code) {

        // 根据code换取微博uid和accessToken
        MultiValueMap<String, String> weiboData = new LinkedMultiValueMap<>();
        // 定义微博token请求参数
        weiboData.add(SocialLoginConst.CLIENT_ID, weiboConfigProperties.getAppId());
        weiboData.add(SocialLoginConst.CLIENT_SECRET, weiboConfigProperties.getAppSecret());
        weiboData.add(SocialLoginConst.GRANT_TYPE, weiboConfigProperties.getGrantType());
        weiboData.add(SocialLoginConst.REDIRECT_URI, weiboConfigProperties.getRedirectUrl());
        weiboData.add(SocialLoginConst.CODE, code);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(weiboData, null);
        try {
            return restTemplate.exchange(weiboConfigProperties.getAccessTokenUrl(), HttpMethod.POST, requestEntity, WeiboTokenVO.class).getBody();
        } catch (Exception e) {
            throw new BusinessException(ResultCode.WEIBO_LOGIN_ERROR.getDesc());
        }
    }

}

