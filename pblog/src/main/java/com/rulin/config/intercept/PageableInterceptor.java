package com.rulin.config.intercept;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.rulin.common.Constants.*;

/**
 * 分页拦截器
 *
 **/
public class PageableInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String currentPage = request.getParameter(PAGE_NO);
        String pageSize = Optional.ofNullable(request.getParameter(PAGE_SIZE)).orElse(DEFAULT_SIZE);
        if (StringUtils.isNoneBlank(currentPage)) {
            PageUtils.setCurrentPage(new Page<>(Long.parseLong(currentPage), Long.parseLong(pageSize)));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        PageUtils.remove();
    }

}
