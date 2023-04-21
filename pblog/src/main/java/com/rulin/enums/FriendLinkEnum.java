package com.rulin.enums;

/**
 * 友链状态枚举类
 */
public enum FriendLinkEnum {
    /**
     * 友链下架
     */
    DOWN(0, "下架"),

    /**
     * 友链申请
     */
    APPLY(1, "申请"),

    /**
     * 友链上架
     */
    UP(2, "上架");


    //创建构造函数
    FriendLinkEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    //定义私有方法，获取枚举值
    public final Integer code;
    public final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
