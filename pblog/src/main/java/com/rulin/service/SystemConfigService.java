package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.SystemConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 系统配置表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface SystemConfigService extends IService<SystemConfig> {

    ResponseResult getConfig();

    ResponseResult updateConfig(SystemConfig systemConfig);

    SystemConfig getCustomizeOne();
}
