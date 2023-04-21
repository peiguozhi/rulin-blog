package com.rulin.config.satoken;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import com.rulin.entity.UserAuth;
import com.rulin.mapper.UserAuthMapper;
import com.rulin.mapper.UserMapper;
import com.rulin.util.DateUtils;
import com.rulin.util.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 自定义侦听器的实现
 */
@Component
@RequiredArgsConstructor
public class MySaTokenListener implements SaTokenListener {

    private static final Logger logger = LoggerFactory.getLogger(MySaTokenListener.class);

    public static final List<OnlineUser> ONLINE_USERS = new CopyOnWriteArrayList<>();

    private final UserMapper userMapper;

    private final HttpServletRequest request;

    private final UserAuthMapper userAuthMapper;

    @PostConstruct
    public void init() {
        initRefreshThread();
    }

    /** 每次登录时触发 */
    @Override
    public void doLogin(String loginType, Object loginId, SaLoginModel loginModel) {
        //修改登录信息
        String ip = IpUtils.getIp(request);
        String cityInfo = IpUtils.getIp2region(ip);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        userMapper.updateLoginInfo(loginId,ip,cityInfo,userAgent.getOperatingSystem().getName(),userAgent.getBrowser().getName());
        //暂时使用内存方式存储在线用户信息
        String token = StpUtil.getTokenValueByLoginId(loginId);
        UserAuth auth = userAuthMapper.getByUserId(loginId);
        ONLINE_USERS.add(OnlineUser.builder()
                .avatar(auth.getAvatar())
                .ip(ip)
                .city(cityInfo)
                .loginTime(DateUtils.getNowDate())
                .os(userAgent.getOperatingSystem().getName())
                .userId((Long) loginId)
                .tokenValue(token)
                .nickname(userMapper.getById(loginId).getNickname())
                .browser(userAgent.getBrowser().getName()).build());
        logger.info("用户已登录,useId:{},token:{}", loginId, token);
    }

    /** 每次注销时触发 */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        ONLINE_USERS.removeIf(onlineUser ->
                onlineUser.getTokenValue().equals(tokenValue)
        );
        logger.info("用户已注销,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被踢下线时触发 */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        ONLINE_USERS.removeIf(onlineUser ->
                onlineUser.getTokenValue().equals(tokenValue)
        );
        logger.info("用户已踢下线,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被顶下线时触发 */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        ONLINE_USERS.removeIf(onlineUser ->
                onlineUser.getTokenValue().equals(tokenValue)
        );
        logger.info("用户已顶下线,useId:{},token:{}", loginId, tokenValue);
    }

    /** 每次被封禁时触发 */
    @Override
    public void doDisable(String loginType, Object loginId, long disableTime) {
        // ...
    }

    /** 每次被解封时触发 */
    @Override
    public void doUntieDisable(String loginType, Object loginId) {
        // ...
    }

    /** 每次创建Session时触发 */
    @Override
    public void doCreateSession(String id) {
        // ...
    }

    /** 每次注销Session时触发 */
    @Override
    public void doLogoutSession(String id) {
        // ...
        logger.info("user doLogoutSession,id:{}", id);
    }

    // --------------------- 定时清理过期数据

    /**
     * 执行数据清理的线程
     */
    public Thread refreshThread;

    /**
     * 是否继续执行数据清理的线程标记
     */
    public boolean refreshFlag;

    /**
     * 初始化定时任务
     */
    public void initRefreshThread() {

        // 如果配置了<=0的值，则不启动定时清理
        if (SaManager.getConfig().getDataRefreshPeriod() <= 0) {
            return;
        }
        // 启动定时刷新
        this.refreshFlag = true;
        this.refreshThread = new Thread(() -> {
            for (; ; ) {
                //logger.info("定时清理过期会话开始。间隔：{}s,在线人数：{}", SaManager.getConfig().getDataRefreshPeriod() + 5, ONLINE_USERS.size());
                try {
                    try {
                        // 如果已经被标记为结束
                        if (!refreshFlag) {
                            return;
                        }
                        long start = System.currentTimeMillis();
                        ONLINE_USERS.removeIf(onlineUser -> {
                            Object user = StpUtil.getLoginIdByToken(onlineUser.getTokenValue());
                            return ObjectUtils.isEmpty(user);
                        });
                       // logger.info("定时清理过期会话结束，在线人数：{},耗时：{}ms", ONLINE_USERS.size(), System.currentTimeMillis() - start);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 休眠N秒
                    int dataRefreshPeriod = SaManager.getConfig().getDataRefreshPeriod();
                    if (dataRefreshPeriod <= 0) {
                        dataRefreshPeriod = 1;
                    }
                    dataRefreshPeriod = dataRefreshPeriod + 5;
                    Thread.sleep(dataRefreshPeriod * 1000L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        refreshThread.start();
    }

}