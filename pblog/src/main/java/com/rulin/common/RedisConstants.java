package com.rulin.common;

/**
 * @author CodeScholar
 * @description: redis常用常量
 * @date 2023年4月9日
 */
public class RedisConstants {

    /**
     * 验证码
     */
    public static String CAPTCHA_CODE = "CAPTCHA_CODE:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 5;

    /**
     * 标签点击量key
     */
    public static String TAG_CLICK_VOLUME = "TAG_CLICK_VOLUME";

    /**
     * 文章阅读key
     */
    public static String ARTICLE_READING = "ARTICLE_READING";

    /**
     * 存储所有图片路径集合
     */
    public static String ALL_IMG = "all_img";

    /**
     * 存储实际使用图片路径集合
     */
    public static String APPLY_IMG = "apply_img";

    /**
     * 邮箱验证码
     */
    public static String EMAIL_CODE = "email_code_";

    /**
     * 公众号获取得验证码
     */
    public static String WECHAT_CODE = "wechat_code_";

    /**
     * 文章验证码校验通过的ip
     */
    public static String CHECK_CODE_IP = "check_code_ip";

    /**
     * 访客
     */
    public static final String UNIQUE_VISITOR = "unique_visitor";

    /**
     * 访客地区
     */
    public static final String VISITOR_AREA = "visitor_area";

    /**
     * 博客浏览量
     */
    public static final String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * 用户点赞文章
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like:";


    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";
}
