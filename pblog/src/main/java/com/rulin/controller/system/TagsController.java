package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Tags;
import com.rulin.service.TagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/tags")
@Api(tags = "标签管理")
@RequiredArgsConstructor
public class TagsController {

    private final TagsService tagsService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "标签列表", httpMethod = "GET", response = ResponseResult.class, notes = "标签列表")
    public ResponseResult list(String name) {
        return tagsService.listTags(name);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @SaCheckPermission("/system/tags/add")
    @ApiOperation(value = "新增标签", httpMethod = "POST", response = ResponseResult.class, notes = "新增标签")
    @OperationLogger(value = "新增标签")
    public ResponseResult insert(@RequestBody Tags tags) {
        return tagsService.insertTag(tags);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @SaCheckPermission("/system/tags/info")
    @ApiOperation(value = "标签详情", httpMethod = "GET", response = ResponseResult.class, notes = "标签详情")
    public ResponseResult getTagsById(Long id) {
        return tagsService.getTagsById(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @SaCheckPermission("/system/tags/update")
    @ApiOperation(value = "修改标签", httpMethod = "POST", response = ResponseResult.class, notes = "修改标签")
    @OperationLogger(value = "修改标签")
    public ResponseResult update(@RequestBody Tags tags) {
        return tagsService.updateTag(tags);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/tags/remove")
    @ApiOperation(value = "删除标签", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除标签")
    @OperationLogger(value = "删除标签")
    public ResponseResult deleteById(Long id) {
        return tagsService.deleteById(id);
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/tags/deleteBatch")
    @ApiOperation(value = "批量删除标签", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除标签")
    @OperationLogger(value = "批量删除标签")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return tagsService.deleteBatch(ids);
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @SaCheckPermission("/system/tags/top")
    @ApiOperation(value = "置顶标签", httpMethod = "GET", response = ResponseResult.class, notes = "置顶标签")
    @OperationLogger(value = "置顶标签")
    public ResponseResult top(Long id) {
        return tagsService.top(id);
    }
}

