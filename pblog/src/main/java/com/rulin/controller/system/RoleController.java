package com.rulin.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Role;
import com.rulin.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/role")
@Api(tags = "角色管理-接口")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "角色列表", httpMethod = "GET", response = ResponseResult.class, notes = "角色列表")
    public ResponseResult list(String name) {
        return roleService.listRole(name);
    }

    @RequestMapping(value = "queryUserRole", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "获取当前登录用户所拥有的权限", httpMethod = "GET", response = ResponseResult.class, notes = "获取当前登录用户所拥有的权限")
    public ResponseResult getCurrentUserRole() {
        return roleService.getCurrentUserRole();
    }

    @RequestMapping(value = "queryRoleId", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "获取该角色所有的权限", httpMethod = "GET", response = ResponseResult.class, notes = "获取该角色所有的权限")
    public ResponseResult selectById(Integer roleId) {
        return roleService.selectById(roleId);
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    @SaCheckPermission("/system/role/create")
    @ApiOperation(value = "添加角色", httpMethod = "POST", response = ResponseResult.class, notes = "添加角色")
    @OperationLogger(value = "添加角色")
    public ResponseResult insert(@RequestBody Role role) {
        return roleService.insertRole(role);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    @SaCheckPermission("/system/role/update")
    @ApiOperation(value = "修改角色", httpMethod = "POST", response = ResponseResult.class, notes = "修改角色")
    @OperationLogger(value = "修改角色")
    public ResponseResult update(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @RequestMapping(value = "remove", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/role/remove")
    @ApiOperation(value = "删除角色", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除角色")
    @OperationLogger(value = "删除角色")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return roleService.deleteBatch(ids);
    }


}
