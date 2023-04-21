package com.rulin.annotation;

import java.lang.annotation.*;

/**
 * @author CodeScholar
 * @title: BusinessLog
 * @description: 自定义操作日志注解
 */
@Target(ElementType.METHOD)//注解放置的目标位置即方法级别
@Retention(RetentionPolicy.RUNTIME)//注解在哪个阶段执行
@Documented
public @interface BusinessLogger {
    String value() default ""; // 操作模块

    String type() default "";  // 操作类型

    String desc() default "";  // 操作说明

    boolean save() default true; //是否将当前日志记录到数据库中
}
