package com.rulin.service;

import com.rulin.entity.FriendLink;

import javax.mail.MessagingException;

public interface EmailService {
    /**
     * 发送验证码
     * @param email 邮箱账号
     */
    void sendCode(String email) throws MessagingException;


    /**
     * 友链通过通知
     * @param email 邮箱账号
     */
    void friendPassSendEmail(String email);

    /**
     * 友链未通过通知
     * @param email 邮箱账号
     * @param reason 未通过原因
     */
    void friendFailedSendEmail(String email,String reason);

    /**
     * 邮箱通知我
     * @param subject 邮箱主题
     * @param content 内容
     */
    void emailNoticeMe(String subject,String content);

    /**
     *  审核友链通不通过发送通知
     * @param friendLink 友链对象
     */
    void sendFriendEmail(FriendLink friendLink);
}
