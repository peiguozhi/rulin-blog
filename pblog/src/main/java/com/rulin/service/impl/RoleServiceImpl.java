package com.rulin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rulin.common.ResponseResult;
import com.rulin.common.FieldConstants;
import com.rulin.entity.Role;
import com.rulin.mapper.RoleMapper;
import com.rulin.service.RoleService;
import com.rulin.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    /**
     * 角色列表
     * @param name
     * @return
     */
    @Override
    public ResponseResult listRole(String name) {
        Page<Role> data = baseMapper.selectPage(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), new QueryWrapper<Role>()
                .like(StringUtils.isNotBlank(name), FieldConstants.NAME,name));
        return ResponseResult.success(data);
    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertRole(Role role) {
        baseMapper.insert(role);
        baseMapper.insertBatchByRole(role.getMenus(), role.getId());
        return ResponseResult.success();
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateRole(Role role) {
        baseMapper.updateById(role);

        //先删所有权限在新增
        baseMapper.delByRoleId(role.getId(),null);
        baseMapper.insertBatchByRole(role.getMenus(), role.getId());
        return ResponseResult.success("修改成功");
    }

    /**
     * 删除角色
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Integer> ids) {
        baseMapper.deleteBatchIds(ids);
        ids.forEach(id -> baseMapper.delByRoleId(id, null));
        return ResponseResult.success();
    }

    /**
     * 获取当前登录用户所拥有的权限
     * @param
     * @return
     */
    @Override
    public ResponseResult getCurrentUserRole() {
        Integer roleId = baseMapper.queryByUserId(StpUtil.getLoginId());
        List<Integer> list = baseMapper.queryByRoleMenu(roleId);
        return ResponseResult.success(list);
    }

    /**
     * 获取该角色所有的权限
     * @param
     * @return
     */
    @Override
    public ResponseResult selectById(Integer roleId) {
        List<Integer> list = baseMapper.queryByRoleMenu(roleId);
        return ResponseResult.success(list);
    }
}
