package com.rulin.aspect;

import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.UserLog;
import com.rulin.mapper.UserLogMapper;
import com.rulin.util.DateUtils;
import com.rulin.util.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author CodeScholar
 * @title: OperationAspect
 * @description: 操作日志切面处理类
 */
@Aspect
@Component
@RequiredArgsConstructor
public class BusinessLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(BusinessLoggerAspect.class);

    private final UserLogMapper sysLogMapper;

    /**
     * 设置操作日志切入点   在注解的位置切入代码
     */
    @Pointcut("@annotation(businessLogger)")
    public void pointcut(BusinessLogger businessLogger) {
    }

    @Around(value = "pointcut(businessLogger)")
    public Object doAround(ProceedingJoinPoint joinPoint, BusinessLogger businessLogger) throws Throwable {

        //先执行业务
        Object result = joinPoint.proceed();

        try {
            // 日志收集
            handle(joinPoint,(ResponseResult) result);

        } catch (Exception e) {
            logger.error("日志记录出错!", e);
        }

        return result;
    }
    /**
     * 记录操作日志
     * @param joinPoint 方法的执行点
     * @param result  方法返回值
     * @throws Throwable
     */
    public void handle(ProceedingJoinPoint  joinPoint, ResponseResult result) throws Throwable {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        try {
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = signature.getMethod();
            //获取操作
            BusinessLogger annotation = method.getAnnotation(BusinessLogger.class);
            if (!annotation.save()) {
                return;
            }

            assert request != null;
            String ip = IpUtils.getIp(request);
            UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
            String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
            String os = userAgent.getOperatingSystem().getName();
            String browser = userAgent.getBrowser().toString();

            UserLog userLog = UserLog.builder().model(annotation.value()).type(annotation.type())
                    .description(annotation.desc()).createTime(DateUtils.getNowDate())
                    .ip(ip).address(IpUtils.getIp2region(ip)).clientType(clientType).accessOs(os)
                    .browser(browser).result(result.getMessage()).build();
            sysLogMapper.insert(userLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
