package com.rulin.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.google.code.kaptcha.Producer;
import com.rulin.common.Constants;
import com.rulin.common.ResponseResult;
import com.rulin.dto.LoginDTO;
import com.rulin.entity.User;
import com.rulin.mapper.UserMapper;
import com.rulin.service.LoginService;
import com.rulin.util.AesEncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.rulin.common.ResultCode.ERROR_PASSWORD;

/**
 * @author CodeScholar
 * @description:
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginServiceImpl implements LoginService {

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final UserMapper userMapper;

    @Override
    public Map<String, String> getCode(HttpServletResponse response) throws IOException {
        Map<String, String> result = new HashMap<String,String>();
      /*  // 生成验证码的UUID
        String uuid = UUIDUtil.getUuid();
        String capStr = null;
        String code = null;
        BufferedImage image = null;
        // 生成验证码
        String capText = captchaProducerMath.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);
        redisCache.setCacheObject(RedisConstants.CAPTCHA_CODE+uuid,code,RedisConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        result.put("uuid",uuid);
        result.put("img", Base64.encode(os.toByteArray()));*/
        return result;
    }

    @Override
    public ResponseResult login(LoginDTO vo) {
        //校验用户名和密码
        User user = userMapper.selectNameAndPassword(vo.getUsername(), AesEncryptUtils.aesEncrypt(vo.getPassword()));

        Assert.isTrue(user != null, ERROR_PASSWORD.getDesc());

        if (vo.getRememberMe()) {
            StpUtil.login(user.getId().longValue(),new SaLoginModel().setTimeout(60 * 60 * 24 * 7));
        }else {
            StpUtil.login(user.getId().longValue());
        }
        StpUtil.getSession().set(Constants.CURRENT_USER,userMapper.getById(user.getId()));
        return ResponseResult.success(StpUtil.getTokenValue());
    }
}
