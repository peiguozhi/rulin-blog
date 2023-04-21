package com.rulin.controller.api;


import com.rulin.common.ResponseResult;
import com.rulin.service.impl.HomeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 门户首页管理
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/web/home")
@RequiredArgsConstructor
@Api(tags = "门户首页管理")
public class ApiHomeController {

    private final HomeServiceImpl homeService;


    @RequestMapping(value = "/webSiteInfo", method = RequestMethod.GET)
    @ApiOperation(value = "网站信息", httpMethod = "GET", response = ResponseResult.class, notes = "网站信息")
    public ResponseResult webSiteInfo() {
        return homeService.webSiteInfo();
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    @ApiOperation(value = "增加访问量", httpMethod = "GET", response = ResponseResult.class, notes = "增加访问量")
    public ResponseResult report(HttpServletRequest request) {
        return homeService.report(request);
    }
}

