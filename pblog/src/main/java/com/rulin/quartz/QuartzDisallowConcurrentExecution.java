package com.rulin.quartz;

import com.rulin.entity.Job;
import org.quartz.JobExecutionContext;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote 定时任务处理（禁止并发执行）
 */
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Job job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }
}
