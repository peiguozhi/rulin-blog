package com.rulin.config.intercept;

import com.rulin.util.IpUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitIntercept implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AccessLimitIntercept.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${IpLimit.count}")
    private int count;
    @Value("${IpLimit.time}")
    private int time;

    /**
     * 接口调用前检查对方ip是否频繁调用接口
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // handler是否为 HandleMethod 实例
            if (handler instanceof HandlerMethod) {
                //文章搜索则不进行限流,如需部分接口地址限流可自定义注解实现
                String requestURI = request.getRequestURI();
                if (requestURI.contains("searchArticle")) {
                    return true;
                }

                // 拼接redis key = IP + Api限流
                String key = IpUtils.getIp(request) + request.getRequestURI();
                // 获取redis的value
                Integer maxTimes = null;
                Object value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    maxTimes = (Integer) value;
                }
                if (maxTimes == null) {
                    // 如果redis中没有该ip对应的时间则表示第一次调用，保存key到redis
                    redisTemplate.opsForValue().set(key, 1, time, TimeUnit.SECONDS);
                } else if (maxTimes < count) {
                    // 如果redis中的时间比注解上的时间小则表示可以允许访问,这是修改redis的value时间
                    redisTemplate.opsForValue().set(key, maxTimes + 1, time, TimeUnit.SECONDS);
                } else {
                    // 请求过于频繁
                    logger.info("API请求限流拦截启动,{} 请求过于频繁", key);
                    output(response, "{\"code\":\"8002\",\"message\":\"请求过于频繁，请稍后再试\"}");
                }
            }
        } catch (Exception e) {
            logger.error("API请求限流拦截异常，异常原因：", e);
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(msg.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ObjectUtils.isNotEmpty(outputStream)) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
