package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.UserAuth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.dto.EmailLoginDTO;
import com.rulin.dto.EmailRegisterDTO;
import com.rulin.dto.QQLoginDTO;
import com.rulin.dto.UserAuthDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface UserAuthService extends IService<UserAuth> {

    ResponseResult emailRegister(EmailRegisterDTO emailRegisterDTO);

    ResponseResult updatePassword(EmailRegisterDTO emailRegisterDTO);

    ResponseResult emailLogin(EmailLoginDTO emailLoginDTO);

    ResponseResult qqLogin(QQLoginDTO qqLoginDTO);

    ResponseResult weiboLogin(String code);

    ResponseResult giteeLogin(String code);

    ResponseResult sendEmailCode(String email);

    ResponseResult bindEmail(UserAuthDTO vo);

    ResponseResult updateUser(UserAuthDTO vo);

}
