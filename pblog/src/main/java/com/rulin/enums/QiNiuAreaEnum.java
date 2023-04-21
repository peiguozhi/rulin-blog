package com.rulin.enums;

import com.qiniu.storage.Region;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote
 */
public enum QiNiuAreaEnum {
    /**
     * 七牛云存储区域
     */
    EastChina("z0", "华东", Region.region0()),

    NorthChina("z1", "华北", Region.region1()),

    NorthAmerica("na0", "北美", Region.regionNa0()),

    SoutheastAsia("as0", "东南亚", Region.regionAs0()),

    SouthChina("z2", "华南", Region.region2());

    //创建构造函数
    QiNiuAreaEnum(String alias, String name, Region region) {
        this.alias = alias;
        this.name = name;
        this.region = region;
    }

    //定义私有方法，获取枚举值
    public final String alias;
    public final String name;
    public final Region region;

    public Region getRegion() {
        return region;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }


    public static Region getRegion(String alias) {
        for (QiNiuAreaEnum value : QiNiuAreaEnum.values()) {
            if (value.getAlias().equals(alias)) {
                return value.getRegion();
            }
        }
        return null;
    }
}
