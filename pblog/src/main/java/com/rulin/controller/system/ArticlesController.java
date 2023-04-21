package com.rulin.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.service.ArticleService;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.dto.ArticleDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/system/article")
@RequiredArgsConstructor
@Api(tags = "后台文章管理")
public class ArticlesController {

    private final ArticleService articleService;

    @PostMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "文章列表", httpMethod = "POST", response = ResponseResult.class, notes = "文章列表")
    public ResponseResult list(@RequestBody Map<String, Object> map) {
        return articleService.listArticle(map);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/article/info")
    @ApiOperation(value = "文章详情", httpMethod = "GET", response = ResponseResult.class, notes = "文章详情")
    public ResponseResult getArticleById(Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/article/add")
    @ApiOperation(value = "保存文章", httpMethod = "POST", response = ResponseResult.class, notes = "保存文章")
    @OperationLogger(value = "保存文章")
    public ResponseResult insert(@RequestBody ArticleDTO article) {
        return articleService.insertArticle(article);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/article/update")
    @ApiOperation(value = "修改文章", httpMethod = "POST", response = ResponseResult.class, notes = "修改文章")
    @OperationLogger(value = "修改文章")
    public ResponseResult update(@RequestBody ArticleDTO article) {
        return articleService.updateArticle(article);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/article/delete")
    @ApiOperation(value = "删除文章", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除文章")
    @OperationLogger(value = "删除文章")
    public ResponseResult delete(Long id) {
        return articleService.deleteArticle(id);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/article/deleteBatch")
    @ApiOperation(value = "批量删除文章", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除文章")
    @OperationLogger(value = "批量删除文章")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return articleService.deleteBatchArticle(ids);
    }

    @PostMapping(value = "/top")
    @SaCheckPermission("/system/article/top")
    @ApiOperation(value = "置顶文章", httpMethod = "POST", response = ResponseResult.class, notes = "置顶文章")
    @OperationLogger(value = "置顶文章")
    public ResponseResult putTopArticle(@RequestBody ArticleDTO article) {
        return articleService.putTopArticle(article);
    }

    @PostMapping(value = "/pubOrShelf")
    @SaCheckPermission("/system/article/pubOrShelf")
    @ApiOperation(value = "发布或下架文章", httpMethod = "POST", response = ResponseResult.class, notes = "发布或下架文章")
    @OperationLogger(value = "发布或下架文章")
    public ResponseResult publishAndShelf(@RequestBody ArticleDTO article) {
        return articleService.publishAndShelf(article);
    }

    @PostMapping(value = "/baiduSeo")
    @SaCheckPermission("/system/article/baiduSeo")
    @ApiOperation(value = "文章SEO", httpMethod = "POST", response = ResponseResult.class, notes = "文章SEO")
    @OperationLogger(value = "文章SEO")
    public ResponseResult articleSeo(@RequestBody List<Long> ids) {
        return articleService.articleSeo(ids);
    }

    @GetMapping(value = "/reptile")
    @SaCheckPermission("/system/article/reptile")
    @ApiOperation(value = "文章爬虫", httpMethod = "GET", response = ResponseResult.class, notes = "文章爬虫")
    @OperationLogger(value = "文章爬虫")
    public ResponseResult reptile(String url) {
        return articleService.reptile(url);
    }

    @GetMapping(value = "/randomImg")
    @ApiOperation(value = "随机获取一张图片", httpMethod = "GET", response = ResponseResult.class, notes = "随机获取一张图片")
    public ResponseResult randomImg() {
        return articleService.randomImg();
    }

}
