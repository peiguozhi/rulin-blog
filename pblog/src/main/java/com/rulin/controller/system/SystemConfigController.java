package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.SystemConfig;
import com.rulin.service.SystemConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统配置表 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/config")
@Api(tags = "系统配置管理")
@RequiredArgsConstructor
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "查询系统配置", httpMethod = "GET", response = ResponseResult.class, notes = "查询系统配置")
    public ResponseResult getConfig() {
        return systemConfigService.getConfig();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @SaCheckPermission("/system/config/update")
    @ApiOperation(value = "修改系统配置", httpMethod = "POST", response = ResponseResult.class, notes = "修改系统配置")
    @OperationLogger(value = "修改系统配置")
    public ResponseResult update(@RequestBody SystemConfig systemConfig) {
        return systemConfigService.updateConfig(systemConfig);
    }
}

