package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Page;
import com.rulin.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/page")
@Api(tags = "后台页面管理")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "页面列表", httpMethod = "GET", response = ResponseResult.class, notes = "页面列表")
    public ResponseResult list() {
        return pageService.listPage();
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/page/add")
    @ApiOperation(value = "新增页面", httpMethod = "POST", response = ResponseResult.class, notes = "新增页面")
    @OperationLogger(value = "新增页面")
    public ResponseResult insert(@RequestBody Page page) {
        return pageService.insertPage(page);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/page/update")
    @ApiOperation(value = "修改页面", httpMethod = "POST", response = ResponseResult.class, notes = "修改页面")
    @OperationLogger(value = "修改页面")
    public ResponseResult update(@RequestBody Page page) {
        return pageService.updatePage(page);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/page/delete")
    @ApiOperation(value = "删除页面", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除页面")
    @OperationLogger(value = "删除页面")
    public ResponseResult deletePageById(Long id) {
        return pageService.deletePageById(id);
    }
}

