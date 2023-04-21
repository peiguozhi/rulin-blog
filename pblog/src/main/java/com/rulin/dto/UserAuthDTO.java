package com.rulin.dto;

import lombok.Data;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
@Data
public class UserAuthDTO {
    /**
     * 昵称
     * */
    private String nickname;
    /**
     * 简介
     * */
    private String intro;

    /**
     * 个人网站
     * */
    private String webSite;

    /**
     * 头像
     * */
    private String avatar;

   /**
     * 邮箱
     * */
    private String email;

    /**
     * 验证码
     * */
    private String code;
}
