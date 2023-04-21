package com.rulin.mapper;

import com.rulin.entity.JobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 定时任务调度日志表 Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface JobLogMapper extends BaseMapper<JobLog> {

    void clean();

}
