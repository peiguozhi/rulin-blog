package com.rulin.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Job;
import com.rulin.enums.TaskException;
import com.rulin.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 定时任务调度表 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/job")
@Api(tags = "定时任务管理")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "定时任务列表", httpMethod = "GET", response = ResponseResult.class, notes = "定时任务列表")
    public ResponseResult list(String jobName, String jobGroup, String status) {
        return jobService.listJob(jobName, jobGroup, status);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/job/info")
    @ApiOperation(value = "定时任务详情", httpMethod = "GET", response = ResponseResult.class, notes = "定时任务详情")
    public ResponseResult getJobById(Long jobId) {
        return jobService.getJobById(jobId);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/job/add")
    @ApiOperation(value = "添加定时任务", httpMethod = "POST", response = ResponseResult.class, notes = "添加定时任务")
    @OperationLogger(value = "添加定时任务")
    public ResponseResult insert(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.insertJob(job);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/job/update")
    @ApiOperation(value = "修改定时任务", httpMethod = "POST", response = ResponseResult.class, notes = "修改定时任务")
    @OperationLogger(value = "修改定时任务")
    public ResponseResult update(@RequestBody Job job) throws SchedulerException, TaskException {
        return jobService.updateJob(job);
    }

    @GetMapping(value = "/delete")
    @SaCheckPermission("/system/job/delete")
    @ApiOperation(value = "删除定时任务", httpMethod = "GET", response = ResponseResult.class, notes = "删除定时任务")
    @OperationLogger(value = "删除定时任务")
    public ResponseResult deleteJob(Long jobId) throws SchedulerException, TaskException {
        return jobService.deleteJob(jobId);
    }

    @PostMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/job/deleteBatch")
    @ApiOperation(value = "批量删除定时任务", httpMethod = "POST", response = ResponseResult.class, notes = "批量删除定时任务")
    @OperationLogger(value = "批量删除定时任务")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) throws SchedulerException, TaskException {
        return jobService.deleteBatch(ids);
    }

    @PostMapping(value = "/run")
    @SaCheckPermission("/system/job/run")
    @ApiOperation(value = "立即执行", httpMethod = "POST", response = ResponseResult.class, notes = "立即执行")
    @OperationLogger(value = "立即执行")
    public ResponseResult run(@RequestBody Job job) {
        return jobService.run(job);
    }

    @PostMapping(value = "/change")
    @SaCheckPermission("/system/job/change")
    @ApiOperation(value = "修改状态", httpMethod = "POST", response = ResponseResult.class, notes = "修改状态")
    @OperationLogger(value = "修改状态")
    public ResponseResult changeStatus(@RequestBody Job job) throws SchedulerException {
        return jobService.changeStatus(job);
    }
}

