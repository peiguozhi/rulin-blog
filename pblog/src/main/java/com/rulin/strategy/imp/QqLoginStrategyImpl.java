package com.rulin.strategy.imp;

import com.alibaba.fastjson.JSON;

import com.rulin.config.properties.QqConfigProperties;
import com.rulin.vo.QQTokenVO;
import com.rulin.vo.QQUserInfoVO;
import com.rulin.vo.SocialTokenVO;
import com.rulin.vo.SocialUserInfoVO;
import com.rulin.common.ResultCode;
import com.rulin.common.SocialLoginConst;
import com.rulin.enums.LoginTypeEnum;
import com.rulin.exception.BusinessException;
import com.rulin.dto.QQLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * qq登录策略实现
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service("qqLoginStrategyImpl")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QqLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    private final QqConfigProperties qqConfigProperties;

    private final RestTemplate restTemplate;

    @Override
    public SocialTokenVO getSocialToken(String data) {
        QQLoginDTO qqLoginDTO = JSON.parseObject(data, QQLoginDTO.class);
        // 校验QQ token信息
        checkQQToken(qqLoginDTO);
        // 返回token信息
        return SocialTokenVO.builder()
                .openId(qqLoginDTO.getOpenId())
                .accessToken(qqLoginDTO.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    @Override
    public SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialTokenVO) {
        // 定义请求参数
        Map<String, String> formData = new HashMap<>(3);
        formData.put(SocialLoginConst.QQ_OPEN_ID, socialTokenVO.getOpenId());
        formData.put(SocialLoginConst.ACCESS_TOKEN, socialTokenVO.getAccessToken());
        formData.put(SocialLoginConst.OAUTH_CONSUMER_KEY, qqConfigProperties.getAppId());
        // 获取QQ返回的用户信息
        QQUserInfoVO qqUserInfoVO = JSON.parseObject(restTemplate.getForObject(qqConfigProperties.getUserInfoUrl(), String.class, formData), QQUserInfoVO.class);
        // 返回用户信息
        return SocialUserInfoVO.builder()
                .nickname(Objects.requireNonNull(qqUserInfoVO).getNickname())
                .avatar(qqUserInfoVO.getFigureurl_qq_1())
                .build();
    }

    /**
     * 校验qq token信息
     *
     * @param qqLoginDTO qq登录信息
     */
    private void checkQQToken(QQLoginDTO qqLoginDTO) {
        // 根据token获取qq openId信息
        Map<String, String> qqData = new HashMap<>(1);
        qqData.put(SocialLoginConst.ACCESS_TOKEN, qqLoginDTO.getAccessToken());
        try {
            String result = restTemplate.getForObject(qqConfigProperties.getCheckTokenUrl(), String.class, qqData);
            QQTokenVO qqTokenVO = JSON.parseObject(getBracketsContent(Objects.requireNonNull(result)), QQTokenVO.class);
            // 判断openId是否一致
            if (!qqLoginDTO.getOpenId().equals(qqTokenVO.getOpenid())) {
                throw new BusinessException(ResultCode.QQ_LOGIN_ERROR.getDesc());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.QQ_LOGIN_ERROR.getDesc());
        }
    }

    /**
     * 获取括号内容
     *
     * @param str str
     * @return {@link String} 括号内容
     */
    public static String getBracketsContent(String str) {
        return str.substring(str.indexOf("(") + 1, str.indexOf(")"));
    }
}
