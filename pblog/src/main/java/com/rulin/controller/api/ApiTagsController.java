package com.rulin.controller.api;


import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.service.CategoryService;
import com.rulin.service.TagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 博客标签表 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/web/tags")
@Api(tags = "标签分类接口")
@RequiredArgsConstructor
public class ApiTagsController {

    private final TagsService tagsService;

    private final CategoryService categoryService;

    @BusinessLogger(value = "标签模块-用户访问页面", type = "查询", desc = "用户访问页面")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "标签列表", httpMethod = "GET", response = ResponseResult.class, notes = "标签列表")
    public ResponseResult tagList() {
        return tagsService.webList();
    }

    @BusinessLogger(value = "分类模块-用户访问页面", type = "查询", desc = "用户访问页面")
    @RequestMapping(value = "/categoryList", method = RequestMethod.GET)
    @ApiOperation(value = "分类列表", httpMethod = "GET", response = ResponseResult.class, notes = "分类列表")
    public ResponseResult categoryList() {
        return categoryService.webList();
    }

    @BusinessLogger(value = "笔记分类模块-用户访问页面", type = "查询", desc = "用户访问页面")
    @RequestMapping(value = "/categoryBookList", method = RequestMethod.GET)
    @ApiOperation(value = "笔记分类列表", httpMethod = "GET", response = ResponseResult.class, notes = "笔记分类列表")
    public ResponseResult categoryBookList() {
        return categoryService.categoryBookList();
    }
}

