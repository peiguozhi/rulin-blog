package com.rulin.service;

import com.rulin.entity.JobLog;
import com.rulin.common.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface JobLogService extends IService<JobLog> {

    ResponseResult listJobLog(String jobName, String jobGroup, String status, String startTime,
                            String endTime, Long jobId);

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult clean();
}
