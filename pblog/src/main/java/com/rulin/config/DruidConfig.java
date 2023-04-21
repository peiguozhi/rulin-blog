package com.rulin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 线程池监控信息查看配置
 */
@Configuration
public class DruidConfig {
    @Bean
    public ServletRegistrationBean druidServlet() { // 主要实现WEB监控的配置处理
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
                new StatViewServlet(), "/druid/*"); // 现在要进行druid监控的配置处理操作
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1"); // 白名单
        // servletRegistrationBean.addInitParameter("deny", "192.168.1.200"); // 黑名单
        servletRegistrationBean.addInitParameter("loginUsername", "admin"); // 用户名
        servletRegistrationBean.addInitParameter("loginPassword", "123456"); // 密码
        servletRegistrationBean.addInitParameter("resetEnable", "false"); // 是否可以重置数据源
        return servletRegistrationBean ;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean() ;
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*"); // 所有请求进行监控处理
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.css,/druid/*");
        return filterRegistrationBean ;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }
}
