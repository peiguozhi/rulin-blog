package com.rulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.entity.JobLog;
import com.rulin.mapper.JobLogMapper;
import com.rulin.common.ResponseResult;
import com.rulin.common.FieldConstants;
import com.rulin.service.JobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
public class JobLogServiceImpl extends ServiceImpl<JobLogMapper, JobLog> implements JobLogService {

    /**
     * 任务日志列表
     * @param jobName
     * @param jobGroup
     * @param status
     * @param startTime
     * @param endTime
     * @param jobId
     * @return
     */
    @Override
    public ResponseResult listJobLog(String jobName, String jobGroup, String status, String startTime,
                                   String endTime, Long jobId) {
        QueryWrapper<JobLog> queryWrapper = new QueryWrapper<JobLog>()
                .orderByDesc(FieldConstants.CREATE_TIME).eq(jobId != null, FieldConstants.JOB_ID,jobId)
                .like(StringUtils.isNotBlank(jobName), FieldConstants.JOB_NAME,jobName)
                .like(StringUtils.isNotBlank(jobGroup), FieldConstants.JOB_GROUP,jobGroup)
                .eq(StringUtils.isNotBlank(status), FieldConstants.STATUS,status)
                .between(StringUtils.isNotBlank(startTime), FieldConstants.START_TIME,startTime,endTime);
        Page<JobLog> page = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        return ResponseResult.success(page);
    }

    /**
     * 批量删除日志
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        return ResponseResult.success();
    }

    /**
     * 清空日志
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult clean() {
        baseMapper.clean();
        return ResponseResult.success();
    }

}
