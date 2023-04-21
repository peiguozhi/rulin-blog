package com.rulin.mapper;

import com.rulin.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 定时任务调度表 Mapper 接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Repository
public interface JobMapper extends BaseMapper<Job> {

}
