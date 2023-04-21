package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.service.AdminLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 操作日志管理
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/adminLog")
@RequiredArgsConstructor
@Api(tags = "操作日志管理")
public class AdminLogController {

    private final AdminLogService adminLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "操作日志列表", httpMethod = "GET", response = ResponseResult.class, notes = "操作日志列表")
    public ResponseResult list() {
        return adminLogService.listAdminLog();
    }

    @DeleteMapping(value = "/delete")
    @OperationLogger(value = "删除操作日志")
    @SaCheckPermission("/system/adminLog/delete")
    @ApiOperation(value = "删除操作日志", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除操作日志")
    public ResponseResult delete(@RequestBody List<Long> ids) {
        return adminLogService.deleteAdminLog(ids);
    }
}

