package com.rulin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Role;

import java.util.List;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface RoleService extends IService<Role> {


    ResponseResult listRole(String name);

     ResponseResult insertRole(Role role);

    ResponseResult updateRole(Role role);

    ResponseResult deleteBatch(List<Integer> ids);

    ResponseResult getCurrentUserRole();

    ResponseResult selectById(Integer roleId);

}
