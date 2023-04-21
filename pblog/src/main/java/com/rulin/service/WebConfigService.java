package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网站配置表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface WebConfigService extends IService<WebConfig> {

    ResponseResult listWebConfig();

    ResponseResult updateWebConfig(WebConfig webConfig);
}
