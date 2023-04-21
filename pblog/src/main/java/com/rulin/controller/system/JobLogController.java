package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.service.JobLogService;
import com.rulin.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/jobLog")
@Api(tags = "定时任务调度日志管理")
@RequiredArgsConstructor
public class JobLogController {

    private final JobLogService jobLogService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "定时任务日志列表", httpMethod = "GET", response = ResponseResult.class, notes = "定时任务日志列表")
    public ResponseResult list(String jobName, String jobGroup, String status, String startTime,
                               String endTime, Long jobId) {
        return jobLogService.listJobLog(jobName, jobGroup, status, startTime, endTime, jobId);
    }

    @PostMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/jobLog/deleteBatch")
    @ApiOperation(value = "批量删除日志列表", httpMethod = "POST", response = ResponseResult.class, notes = "批量删除日志列表")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return jobLogService.deleteBatch(ids);
    }

    @GetMapping(value = "/clean")
    @SaCheckPermission("/system/jobLog/clean")
    @ApiOperation(value = "清空日志列表", httpMethod = "GET", response = ResponseResult.class, notes = "清空日志列表")
    public ResponseResult clean() {
        return jobLogService.clean();
    }
}

