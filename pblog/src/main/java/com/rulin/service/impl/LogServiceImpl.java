package com.rulin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.ResponseResult;
import com.rulin.common.FieldConstants;
import com.rulin.entity.UserLog;
import com.rulin.mapper.UserLogMapper;
import com.rulin.service.UserLogService;
import com.rulin.util.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@Service
public class LogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements UserLogService {

    /**
     * 用户日志列表
     * @return
     */
    @Override
    public ResponseResult listUserLog() {
        QueryWrapper<UserLog> queryWrapper = new QueryWrapper<UserLog>()
                .orderByDesc(FieldConstants.CREATE_TIME);
        Page<UserLog> sysLogPage = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), queryWrapper);
        return ResponseResult.success(sysLogPage);
    }

    /**
     * 批量删除用户日志
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("批量删除用户日志失败");
    }
}
