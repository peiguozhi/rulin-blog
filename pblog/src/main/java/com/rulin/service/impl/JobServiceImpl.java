package com.rulin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rulin.common.FieldConstants;
import com.rulin.entity.Job;
import com.rulin.enums.ScheduleConstants;
import com.rulin.exception.BusinessException;
import com.rulin.mapper.UserMapper;
import com.rulin.quartz.CronUtils;
import com.rulin.quartz.ScheduleUtils;
import com.rulin.common.ResponseResult;
import com.rulin.entity.User;
import com.rulin.enums.TaskException;
import com.rulin.mapper.JobMapper;
import com.rulin.service.JobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {

    private final Scheduler scheduler;

    private final UserMapper userMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理
     * （注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<Job> jobList = baseMapper.selectList(null);
        for (Job job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    /**
     * 任务列表
     *
     * @return
     */
    @Override
    public ResponseResult listJob(String jobName, String jobGroup, String status) {
        QueryWrapper<Job> queryWrapper = new QueryWrapper<Job>()
                .like(StringUtils.isNotBlank(jobName), FieldConstants.JOB_NAME,jobName)
                .eq(StringUtils.isNotBlank(jobGroup), FieldConstants.JOB_GROUP,jobGroup)
                .eq(StringUtils.isNotBlank(status), FieldConstants.STATUS,status);

        Page<Job> sysJobPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        return ResponseResult.success(sysJobPage);
    }

    /**
     *  任务详情
     * @param jobId
     * @return
     */
    @Override
    public ResponseResult getJobById(Long jobId) {
        Job job = baseMapper.selectById(jobId);
        Date nextExecution = CronUtils.getNextExecution(job.getCronExpression());
        job.setNextValidTime(nextExecution);
        return ResponseResult.success(job);
    }

    /**
     * 新增任务
     *
     * @param job 调度信息
     * @return
     * @throws SchedulerException
     * @throws TaskException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertJob(Job job) throws SchedulerException, TaskException {
        checkCronIsValid(job);

        User user = userMapper.selectById(StpUtil.getLoginIdAsInt());
        job.setCreateBy(user.getUsername());
        int row = baseMapper.insert(job);
        if (row > 0) ScheduleUtils.createScheduleJob(scheduler, job);

        return ResponseResult.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateJob(Job job) throws SchedulerException, TaskException {
        checkCronIsValid(job);

        User user = userMapper.selectById(StpUtil.getLoginIdAsInt());
        job.setUpdateBy(user.getUsername());
        Job properties = baseMapper.selectById(job.getJobId());
        int row = baseMapper.updateById(job);
        if (row > 0) updateSchedulerJob(job, properties.getJobGroup());

        return ResponseResult.success();
    }

    /**
     * 删除任务
     *
     * @param jobId
     * @return
     * @throws SchedulerException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteJob(Long jobId) throws SchedulerException {

        Job job = baseMapper.selectById(jobId);
        int row = baseMapper.deleteById(jobId);
        if (row > 0) scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, job.getJobGroup()));

        return ResponseResult.success();
    }
    /**
     * 批量删除任务
     *
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
     * 暂停任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult pauseJob(Job job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = baseMapper.updateById(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return ResponseResult.success();
    }

    /**
     * 立即运行任务
     *
     * @param job 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult run(Job job) {
        try {
            Long jobId = job.getJobId();
            String jobGroup = job.getJobGroup();
            // 参数
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleConstants.TASK_PROPERTIES, job);
            scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
            return ResponseResult.success();
        } catch (Exception e) {
            throw new BusinessException("定时任务运行失败！失败原因:" + e.getMessage());
        }
    }

    /**
     * 任务调度状态修改
     *
     * @param job 调度信息
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult changeStatus(Job job) throws SchedulerException {
        String status = job.getStatus();
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int row = baseMapper.updateById(job);
        if (row > 0){
            if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
                scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
            } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
                scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
            }
        }

        return ResponseResult.success();
    }


    //---------自定义方法开始--------
    /**
     * 验证cron
     * @param job
     */
    private void checkCronIsValid(Job job) {
        boolean valid = CronUtils.isValid(job.getCronExpression());
        Assert.isTrue(valid,"Cron表达式无效!");
    }

    /**
     * 更新任务
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}
