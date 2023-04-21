package com.rulin.enums;

public enum FileUploadModelEnum {
    /**
     * 本地上传
     */
    LOCAL(0, "本地上传", "localUploadStrategyImpl"),
    /**
     * 七牛云上传
     */
    QIN(1, "七牛云上传", "qiNiuUploadStrategyImpl"),

    /**
     * 阿里云上传
     */
    ALI(2, "阿里云上传", "aliUploadStrategyImpl");

    //创建构造函数
    FileUploadModelEnum(int code, String desc, String strategy) {
        this.type = code;
        this.desc = desc;
        this.strategy = strategy;
    }


    /**
     * 上传方式
     */
    private final Integer type;
    /**
     * 描述
     */
    private final String desc;
    /**
     * 策略
     */
    private final String strategy;

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getStrategy() {
        return strategy;
    }

    /**
     * 获取策略
     *
     * @param type 模式
     * @return {@link String} 上传策略
     */
    public static String getStrategy(int type) {
        for (FileUploadModelEnum value : FileUploadModelEnum.values()) {
            if (value.getType() == (type)) {
                return value.getStrategy();
            }
        }
        return null;
    }
}
