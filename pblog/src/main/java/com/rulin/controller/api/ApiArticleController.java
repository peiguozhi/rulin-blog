package com.rulin.controller.api;

import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author CodeScholar
 * @Description: 博客前台博客文章控制器
 * @Date 2023年4月9日
 */
@RestController
@RequestMapping("/web/article")
@Api(tags = "前台文章管理")
@RequiredArgsConstructor
public class ApiArticleController {

    private final ArticleService articleService;

    @BusinessLogger(value = "首页-用户访问首页", type = "查询", desc = "查询所有文章")
    @GetMapping(value = "/list")
    @ApiOperation(value = "文章列表", httpMethod = "GET", response = ResponseResult.class, notes = "文章列表")
    public ResponseResult listWebArticle(Long categoryId, Long tagId, String orderByDescColumn) {
        return articleService.listWebArticle(categoryId, tagId, orderByDescColumn);
    }

    @BusinessLogger(value = "首页-文章搜索", type = "查询", desc = "文章搜索")
    @GetMapping(value = "/searchArticle")
    @ApiOperation(value = "文章搜索", httpMethod = "GET", response = ResponseResult.class, notes = "文章搜索")
    public ResponseResult searchArticle(String keywords) {
        return articleService.searchArticle(keywords);
    }

    @BusinessLogger(value = "首页-归档", type = "查询", desc = "归档")
    @GetMapping(value = "/archive")
    @ApiOperation(value = "归档", httpMethod = "GET", response = ResponseResult.class, notes = "归档")
    public ResponseResult archive() {
        return articleService.archive();
    }

    @BusinessLogger(value = "分类标签文章列表", type = "查询", desc = "分类标签文章列表")
    @GetMapping(value = "/condition")
    @ApiOperation(value = "分类标签文章列表", httpMethod = "GET", response = ResponseResult.class, notes = "分类标签文章列表")
    public ResponseResult condition(Long categoryId, Long tagId, @RequestParam(defaultValue = "6") Integer pageSize) {
        return articleService.condition(categoryId, tagId, pageSize);
    }

    @BusinessLogger(value = "门户-用户查看文章详情", type = "查询", desc = "查看文章详情")
    @GetMapping(value = "/info")
    @ApiOperation(value = "文章详情", httpMethod = "GET", response = ResponseResult.class, notes = "文章详情")
    public ResponseResult getArticle(Integer id) {
        return articleService.webArticleInfo(id);
    }

    @BusinessLogger(value = "门户-文章点赞", type = "查询", desc = "文章点赞")
    @GetMapping(value = "/articleLike")
    @ApiOperation(value = "文章点赞", httpMethod = "GET", response = ResponseResult.class, notes = "文章点赞")
    public ResponseResult articleLike(Integer articleId) {
        return articleService.articleLike(articleId);
    }

    @BusinessLogger(value = "文章中-用户验证秘钥", type = "查询", desc = "验证秘钥")
    @GetMapping(value = "/checkSecret")
    @ApiOperation(value = "验证秘钥", httpMethod = "GET", response = ResponseResult.class, notes = "验证秘钥")
    public ResponseResult checkSecret(String code) {
        return articleService.checkSecret(code);
    }
}
