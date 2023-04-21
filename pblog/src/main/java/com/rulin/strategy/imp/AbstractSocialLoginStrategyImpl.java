package com.rulin.strategy.imp;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.rulin.service.RedisService;
import com.rulin.vo.SocialTokenVO;
import com.rulin.vo.SocialUserInfoVO;
import com.rulin.vo.UserDetailVO;
import com.rulin.vo.UserInfoVO;
import com.rulin.common.RedisConstants;
import com.rulin.entity.Role;
import com.rulin.entity.User;
import com.rulin.entity.UserAuth;
import com.rulin.enums.LoginTypeEnum;
import com.rulin.mapper.RoleMapper;
import com.rulin.mapper.UserAuthMapper;
import com.rulin.mapper.UserMapper;
import com.rulin.strategy.SocialLoginStrategy;
import com.rulin.util.*;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.rulin.common.ResultCode.DISABLE_ACCOUNT;
import static com.rulin.enums.UserStatusEnum.disable;

/**
 * 第三方登录抽象模板
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserInfoVO login(String data) {
        // 创建登录信息
        UserDetailVO userDetailVO;
        // 获取第三方token信息
        SocialTokenVO socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getIp2region(ipAddress);
        // 获取第三方用户信息
        SocialUserInfoVO socialUserInfo = getSocialUserInfo(socialToken);
        if (socialToken.getLoginType().equals(LoginTypeEnum.GITEE.getType())){
            socialToken.setOpenId(socialUserInfo.getId());
        }
        // 判断是否已注册
        User user = getUser(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailVO = getUserDetail(user, ipAddress, ipSource,socialUserInfo);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailVO = saveUserDetail(socialToken, ipAddress, ipSource,socialUserInfo);
        }
        // 判断账号是否禁用
        Assert.isTrue(!userDetailVO.getIsDisable().equals(disable.code),DISABLE_ACCOUNT.desc);

        // 返回用户信息
        UserInfoVO userInfoVO = BeanCopyUtils.copyObject(userDetailVO, UserInfoVO.class);
        StpUtil.login(userInfoVO.getId().longValue());
        userInfoVO.setToken(StpUtil.getTokenValue());
        return userInfoVO;
    }

    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return {@link SocialTokenVO} 第三方token信息
     */
    public abstract SocialTokenVO getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenVO 第三方token信息
     * @return {@link SocialUserInfoVO} 第三方用户信息
     */
    public abstract SocialUserInfoVO getSocialUserInfo(SocialTokenVO socialTokenVO);

    /**
     * 获取用户账号
     *
     * @return {@link UserAuth} 用户账号
     */
    private User getUser(SocialTokenVO socialTokenVO) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, socialTokenVO.getOpenId())
                .eq(User::getLoginType, socialTokenVO.getLoginType()));
    }

    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailVO} 用户信息
     */
    private UserDetailVO getUserDetail(User user, String ipAddress, String ipSource, SocialUserInfoVO socialUserInfo) {
        // 更新登录信息
        userMapper.update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getLastLoginTime, LocalDateTime.now())
                .set(User::getIpAddress, ipAddress)
                .set(User::getIpSource, ipSource)
                .eq(User::getId, user.getId()));

        //更新头像和昵称
        userAuthMapper.update(new UserAuth(),new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getAvatar, socialUserInfo.getAvatar())
                .set(UserAuth::getNickname, socialUserInfo.getNickname())
                .eq(UserAuth::getId, user.getUserAuthId()));

        // 封装信息
        return convertUserDetail(user);
    }


    /**
     * 新增用户信息
     *
     * @param socialToken token信息
     * @param ipAddress   ip地址
     * @param ipSource    ip源
     * @return {@link UserDetailVO} 用户信息
     */
    private UserDetailVO saveUserDetail(SocialTokenVO socialToken, String ipAddress, String ipSource, SocialUserInfoVO socialUserInfo) {

        // 保存用户信息
        UserAuth userAuth = UserAuth.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .build();
        userAuthMapper.insert(userAuth);
        // 保存账号信息
        User user = User.builder()
                .userAuthId(userAuth.getId())
                .username(socialToken.getOpenId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .lastLoginTime(DateUtils.getNowDate())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .roleId(2)
                .build();
        userMapper.insert(user);

        return convertUserDetail(user);
    }

    private UserDetailVO convertUserDetail(User user) {
        // 查询账号信息
        UserAuth userAuth = userAuthMapper.selectById(user.getUserAuthId());
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisService.sMembers(RedisConstants.ARTICLE_USER_LIKE + user.getId());
        // 获取设备信息
        String ipAddress = IpUtils.getIp(request);
        String ipSource = IpUtils.getIp2region(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        // 查询账号角色
        Role role = roleMapper.selectById(user.getRoleId());
        List<String> roleList = new ArrayList<>();
        roleList.add(role.getCode());
        // 封装权限集合
        return UserDetailVO.builder()
                .id(user.getId())
                .loginType(user.getLoginType())
                .userAuthId(userAuth.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userAuth.getEmail())
                .roleList(roleList)
                .nickname(userAuth.getNickname())
                .avatar(userAuth.getAvatar())
                .intro(userAuth.getIntro())
                .webSite(userAuth.getWebSite())
                .articleLikeSet(articleLikeSet)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(user.getStatus())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai")))
                .build();
    }
}
