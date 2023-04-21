package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.common.ResponseResult;
import com.rulin.service.impl.HomeServiceImpl;
import com.rulin.vo.SystemHardwareInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/home")
@Api(tags = "后台首页")
@RequiredArgsConstructor
public class HomeController {

    private final HomeServiceImpl homeService;

    @GetMapping(value = "/init")
    @SaCheckLogin
    @ApiOperation(value = "首页各种统计信息", httpMethod = "GET", response = ResponseResult.class, notes = "首页各种统计信息")
    public ResponseResult init() {
        return ResponseResult.success(homeService.init());
    }

    @GetMapping(value = "/lineCount")
    @SaCheckLogin
    @ApiOperation(value = "首页文章、ip用户、留言统计", httpMethod = "GET", response = ResponseResult.class, notes = "首页文章、ip用户、留言统计")
    public ResponseResult lineCount() {
        return ResponseResult.success(homeService.lineCount());
    }

    @GetMapping(value = "/systemInfo")
    @SaCheckPermission("/system/home/systemInfo")
    @ApiOperation(value = "服务器监控", httpMethod = "GET", response = ResponseResult.class, notes = "服务器监控")
    public ResponseResult systemInfo() {
        SystemHardwareInfoVO systemHardwareInfoVO = new SystemHardwareInfoVO();
        try {
            systemHardwareInfoVO.copyTo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.success(systemHardwareInfoVO);
    }

    @GetMapping(value = "/cache")
    @SaCheckPermission("/system/home/cache")
    @ApiOperation(value = "redis监控", httpMethod = "GET", response = ResponseResult.class, notes = "redis监控")
    public ResponseResult getCacheInfo() {
        return homeService.getCacheInfo();
    }
}
