package com.rulin.config.satoken;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rulin.util.DateUtils;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
@Builder
public class OnlineUser {
    private String loginId;
    private Long userId;
    private String nickname;
    private String avatar;
    private String ip;
    private String os;
    private String city;
    private String browser;
    private String tokenValue;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date loginTime;
    /**
     * 最近一次操作时间
     */
    @JsonFormat(pattern = DateUtils.FORMAT_STRING, timezone = "GMT+8")
    private Date lastActivityTime;
}

