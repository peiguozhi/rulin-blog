package com.rulin.enums;

/**
 * 文章发布状态枚举类
 */
public enum PublishEnum {

    /**
     * 文章下架
     */
    shelf(0),

    /**
     * 文章发布
     */
    PUBLISH(1);

    public int code;

    PublishEnum(int code) {
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
