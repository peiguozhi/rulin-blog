package com.rulin.service;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    void setCacheObject(String key, Object value);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    void setCacheObject(String key, Object value, Integer timeout, TimeUnit timeUnit);

    /**
     * 设置有效时间
     *
     * @param key Redis键
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return true=设置成功；false=设置失败
     */
    boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    Object getCacheObject(String key);

    /**
     * 删除单个对象
     *
     * @param key 缓存键值
     */
    boolean deleteObject(final String key);

    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    long setCacheList(String key, List<Object> dataList);

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    List<Object> getCacheList(final String key);

    /**
     * 缓存Map
     *
     * @param key 键
     * @param dataMap 值
     */
    void setCacheMap(String key, Map<String, Object> dataMap);

    /**
     * 获得缓存的Map
     *
     * @param key 键
     * @return
     */
    Map<String, Object> getCacheMap(final String key);

    /**
     * 返回集合key1中存在，但是key2中不存在的数据集合  sdiff
     *
     * @param key1 键1
     * @param key2 键2
     * @return
     */
    Set<String> diff(String key1, String key2);

    Boolean sIsMember(String key, Object value);

    /**
     *  键的值相加指定数量
     * @param key 键
     * @param hashKey 键值
     * @param delta 数量
     * @return
     */
    Long hIncr(String key, String hashKey, Long delta);

    /**
     *  键的值相减指定数量
     * @param key 键
     * @param hashKey 键值
     * @param delta 数量
     * @return
     */
    Long hDecr(String key, String hashKey, Long delta);

    Set<Object> sMembers(String key);

    Long sRemove(String key, Object... values);

    Object hGet(String key, String hashKey);

    Long incr(String key, long delta);

    /**
     *  当前键添加一个值
     * @param key 键
     * @param values 值
     * @return
     */
    Long sAdd(String key, Object... values);

    RedisTemplate getRedisTemplate();

    /**
     *  增加文字阅读量或标签点击量
     */
    void incrArticle(Long id,String key);

}
