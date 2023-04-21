package com.rulin.enums;

/**
 * 用户状态枚举类
 */
public enum UserStatusEnum {
    /**
     * 禁用
     */
    disable(0),
    /**
     * 正常
     */
    normal(1);

    public int code;

    UserStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
