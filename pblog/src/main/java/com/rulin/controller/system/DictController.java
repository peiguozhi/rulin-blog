package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Dict;
import com.rulin.service.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典表 字典类型管理
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/dict")
@Api(tags = "字典类型管理")
@RequiredArgsConstructor
public class DictController {

    private final DictService dictService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "字典类型列表", httpMethod = "GET", response = ResponseResult.class, notes = "字典类型列表")
    public ResponseResult list(String name, Integer isPublish, String descColumn, String ascColumn) {
        return dictService.listDict(name, isPublish, descColumn, ascColumn);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("/system/dict/add")
    @ApiOperation(value = "添加字典", httpMethod = "POST", response = ResponseResult.class, notes = "添加字典")
    @OperationLogger(value = "添加字典")
    public ResponseResult insert(@RequestBody Dict dict) {
        return dictService.insertDict(dict);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @SaCheckPermission("/system/dict/update")
    @ApiOperation(value = "修改字典", httpMethod = "POST", response = ResponseResult.class, notes = "修改字典")
    @OperationLogger(value = "修改字典")
    public ResponseResult update(@RequestBody Dict dict) {
        return dictService.updateDict(dict);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/dict/delete")
    @ApiOperation(value = "删除字典", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除字典")
    @OperationLogger(value = "删除字典")
    public ResponseResult deleteDict(int id) {
        return dictService.deleteDict(id);
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/dict/deleteBatch")
    @ApiOperation(value = "批量删除字典", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除字典")
    @OperationLogger(value = "批量删除字典")
    public ResponseResult deleteBatch(@RequestBody List<Long> list) {
        return dictService.deleteBatch(list);
    }
}

