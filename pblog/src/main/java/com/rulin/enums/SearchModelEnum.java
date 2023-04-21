package com.rulin.enums;

public enum SearchModelEnum {

    /**
     * mysql搜索
     */
    MYSQL(0, "mysql搜索", "mysqlStrategyImpl"),
    /**
     * elasticsearch搜索
     */
    ELASTICSEARCH(1, "elasticsearch搜索", "elasticsearchStrategyImpl");

    //创建构造函数
    SearchModelEnum(int code, String desc, String strategy) {
        this.type = code;
        this.desc = desc;
        this.strategy = strategy;
    }


    /**
     * 搜索方式
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
     * @return {@link String} 搜索策略
     */
    public static String getStrategy(int type) {
        for (SearchModelEnum value : SearchModelEnum.values()) {
            if (value.getType() == (type)) {
                return value.getStrategy();
            }
        }
        return null;
    }



}
