package com.rulin.controller.api;

import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 程序儒
 * @version 1.0.0
 * @date 2023/4/15
 * @apiNote
 */
@RestController
@RequestMapping("/web/question")
@Api(tags = "前台面试题管理")
@RequiredArgsConstructor
public class ApiQuestionController {

    private final QuestionService questionService;

    @BusinessLogger(value = "门户-用户访问面试题", type = "查询", desc = "查询面试题分类信息和该分类下的问题列表")
    @GetMapping(value = "/list")
    @ApiOperation(value = "面试题列表", httpMethod = "GET", response = ResponseResult.class, notes = "面试题列表")
    public ResponseResult listWebQuestion(Integer cid) {
        return questionService.listWebQuestion(cid);
    }

    @BusinessLogger(value = "门户-面试题搜索", type = "查询", desc = "文章搜索")
    @GetMapping(value = "/searchQuestion")
    @ApiOperation(value = "面试题搜索", httpMethod = "GET", response = ResponseResult.class, notes = "面试题搜索")
    public ResponseResult searchQuestion(String keywords) {
        return questionService.searchQuestion(keywords);
    }


    @BusinessLogger(value = "门户-面试题收藏操作", type = "修改", desc = "修改是否收藏该题目")
    @GetMapping(value = "/favoriteQuestion")
    @ApiOperation(value = "面试题收藏", httpMethod = "GET", response = ResponseResult.class, notes = "面试题收藏")
    public ResponseResult favoriteQuestion(Integer questionId, Integer isFavorite) {
        return questionService.favoriteQuestion(questionId, isFavorite);
    }

    @BusinessLogger(value = "门户-用户访问收藏的面试题", type = "查询", desc = "查询收藏的面试题分类信息和该分类下的问题列表")
    @GetMapping(value = "/listFavoriteQuestions")
    @ApiOperation(value = "收藏的面试题列表", httpMethod = "GET", response = ResponseResult.class, notes = "收藏的面试题列表")
    public ResponseResult listFavoriteQuestions() {
        return questionService.listFavoriteQuestions();
    }
}
