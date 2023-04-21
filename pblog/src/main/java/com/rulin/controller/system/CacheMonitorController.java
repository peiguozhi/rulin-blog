package com.rulin.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.RedisConstants;
import com.rulin.common.ResponseResult;
import com.rulin.vo.SysCacheVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Author CodeScholar
 * @Date 2023年4月9日
 * @Description:
 * @Version 1.0
 */
@RestController
@RequestMapping("/monitor/cache")
@RequiredArgsConstructor
public class CacheMonitorController {

    private RedisTemplate<String, String> redisTemplate;

    private final static List<SysCacheVO> caches = new ArrayList<>();

    static {
        caches.add(new SysCacheVO(RedisConstants.TAG_CLICK_VOLUME, "标签点击量", true));
        caches.add(new SysCacheVO(RedisConstants.ARTICLE_READING, "文章阅读量", true));
        caches.add(new SysCacheVO(RedisConstants.EMAIL_CODE, "邮箱验证码", false));
        caches.add(new SysCacheVO(RedisConstants.WECHAT_CODE, "公众号验证码", false));
        caches.add(new SysCacheVO(RedisConstants.CHECK_CODE_IP, "验证通过的ip", true));
        caches.add(new SysCacheVO(RedisConstants.UNIQUE_VISITOR, "访客", true));
        caches.add(new SysCacheVO(RedisConstants.VISITOR_AREA, "访客地区", true));
        caches.add(new SysCacheVO(RedisConstants.BLOG_VIEWS_COUNT, "博客浏览量", true));
        caches.add(new SysCacheVO(RedisConstants.ARTICLE_USER_LIKE, "用户点赞文章", true));
        caches.add(new SysCacheVO(RedisConstants.ARTICLE_LIKE_COUNT, "文章点赞量", true));
    }

    @GetMapping("/getNames")
    @SaCheckLogin
    public ResponseResult cache() {
        return ResponseResult.success(caches);
    }

    @GetMapping("/getKeys/{cacheName}")
    public ResponseResult getCacheKeys(@PathVariable String cacheName) {
        Set<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        return ResponseResult.success(cacheKeys);
    }

    @GetMapping("/getValue/{cacheName}/{cacheKey}")
    public ResponseResult getCacheValue(@PathVariable String cacheName, @PathVariable String cacheKey) {
        String cacheValue = redisTemplate.opsForValue().get(cacheKey);
        SysCacheVO sysCache = new SysCacheVO(cacheName, cacheKey, cacheValue);
        return ResponseResult.success(sysCache);
    }

    @OperationLogger("删除key")
    @DeleteMapping("/clearCacheName/{cacheName}")
    @SaCheckPermission("/monitor/cache/clearCacheName")
    public ResponseResult clearCacheName(@PathVariable String cacheName) {
        Collection<String> cacheKeys = redisTemplate.keys(cacheName + "*");
        redisTemplate.delete(cacheKeys);
        return ResponseResult.success();
    }

    @OperationLogger("删除key")
    @SaCheckPermission("/monitor/cache/clearCacheKey")
    @DeleteMapping("/clearCacheKey/{cacheKey}")
    public ResponseResult clearCacheKey(@PathVariable String cacheKey) {
        redisTemplate.delete(cacheKey);
        return ResponseResult.success();
    }

    @OperationLogger("清空全部")
    @SaCheckPermission("/monitor/cache/clearCacheAll")
    @DeleteMapping("/clearCacheAll")
    public ResponseResult clearCacheAll() {
        Collection<String> cacheKeys = redisTemplate.keys("*");
        redisTemplate.delete(cacheKeys);
        return ResponseResult.success();
    }
}
