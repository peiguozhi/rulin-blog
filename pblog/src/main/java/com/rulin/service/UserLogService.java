package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.UserLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface UserLogService extends IService<UserLog> {

    ResponseResult listUserLog();

    ResponseResult deleteBatch(List<Long> ids);
}
