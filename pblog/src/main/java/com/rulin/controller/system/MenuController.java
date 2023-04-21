package com.rulin.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Menu;
import com.rulin.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CodeScholar
 * @description: 后台系统菜单管理控制器
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/menu")
@Api(tags = "系统菜单管理-接口")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = "/getMenuTree")
    @SaCheckLogin
    @ApiOperation(value = "获取菜单树", httpMethod = "GET", response = ResponseResult.class, notes = "获取菜单树")
    public ResponseResult getMenuTree() {
        List<Menu> result = menuService.listMenuTree(menuService.list());
        return ResponseResult.success("获取菜单树成功", result);
    }

    @GetMapping(value = "/getMenuApi")
    @SaCheckLogin
    @ApiOperation(value = "获取所有接口", httpMethod = "GET", response = ResponseResult.class, notes = "获取所有接口")
    public ResponseResult listMenuApi(Integer id) {
        return menuService.listMenuApi(id);
    }

    @PostMapping(value = "/create")
    @SaCheckPermission("/system/menu/create")
    @ApiOperation(value = "添加菜单", httpMethod = "POST", response = ResponseResult.class, notes = "添加菜单")
    @OperationLogger(value = "添加菜单")
    public ResponseResult insert(@RequestBody Menu menu) {
        return menuService.insertMenu(menu);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/menu/update")
    @ApiOperation(value = "修改菜单", httpMethod = "POST", response = ResponseResult.class, notes = "修改菜单")
    @OperationLogger(value = "修改菜单")
    public ResponseResult update(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }

    @DeleteMapping(value = "/remove")
    @SaCheckPermission("/system/menu/remove")
    @ApiOperation(value = "删除菜单", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除菜单")
    @OperationLogger(value = "删除菜单")
    public ResponseResult deleteMenuById(Integer id) {
        return menuService.deleteMenuById(id);
    }
}
