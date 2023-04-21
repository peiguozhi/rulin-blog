package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.QCategory;
import com.rulin.service.QCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 程序儒
 * @date 2023年4月13日 14点36分
 * @apiNote 后台面试题分类管理
 */
@RestController
@RequestMapping("/system/qCategory")
@RequiredArgsConstructor
@Api(tags = "面试题分类")
public class QCategoryController {

    private final QCategoryService qCategoryService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "面试题分类列表", httpMethod = "GET", response = ResponseResult.class, notes = "面试题分类列表")
    public ResponseResult list(String name) {
        return qCategoryService.listQCategory(name);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @SaCheckPermission("/system/qCategory/info")
    @ApiOperation(value = "面试题分类详情", httpMethod = "GET", response = ResponseResult.class, notes = "面试题分类详情")
    public ResponseResult getQCategoryById(@RequestParam() Long id) {
        return qCategoryService.getQCategoryById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("/system/qCategory/add")
    @ApiOperation(value = "新增面试题分类", httpMethod = "POST", response = ResponseResult.class, notes = "新增面试题分类")
    @OperationLogger(value = "新增面试题分类")
    public ResponseResult insertQCategory(@RequestBody QCategory qCategory) {
        return qCategoryService.insertQCategory(qCategory);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @SaCheckPermission("/system/qCategory/update")
    @ApiOperation(value = "修改面试题分类", httpMethod = "POST", response = ResponseResult.class, notes = "修改面试题分类")
    @OperationLogger(value = "修改面试题分类")
    public ResponseResult update(@RequestBody QCategory qCategory) {
        return qCategoryService.updateQCategory(qCategory);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/qCategory/delete")
    @ApiOperation(value = "删除面试题分类", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除面试题分类")
    @OperationLogger(value = "删除面试题分类")
    public ResponseResult deleteQCategory(Long id) {
        return qCategoryService.deleteQCategory(id);
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/qCategory/deleteBatch")
    @ApiOperation(value = "批量删除面试题分类", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除面试题分类")
    @OperationLogger(value = "批量删除面试题分类")
    public ResponseResult deleteBatch(@RequestBody List<QCategory> list) {
        return qCategoryService.deleteBatch(list);
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @SaCheckPermission("/system/qCategory/top")
    @ApiOperation(value = "置顶面试题分类", httpMethod = "GET", response = ResponseResult.class, notes = "置顶面试题分类")
    @OperationLogger(value = "置顶面试题分类")
    public ResponseResult top(Long id) {
        return qCategoryService.top(id);
    }
}

