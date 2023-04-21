package com.rulin.controller.api;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.dto.CommentDTO;
import com.rulin.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/comment")
@Api(tags = "评论接口")
@RequiredArgsConstructor
public class ApiCommentController {

    private final CommentService commentService;

    @BusinessLogger(value = "评论模块-用户评论",type = "添加",desc = "用户评论")
    @RequestMapping(value = "/addComment",method = RequestMethod.POST)
    @SaCheckLogin
    @ApiOperation(value = "添加评论", httpMethod = "POST", response = ResponseResult.class, notes = "添加评论")
    public ResponseResult addComment(@RequestBody CommentDTO comment){
        return commentService.addComment(comment);
    }

    @RequestMapping(value = "/comments",method = RequestMethod.GET)
    @ApiOperation(value = "查询文章评论", httpMethod = "GET", response = ResponseResult.class, notes = "查询文章评论")
    public ResponseResult comments(Long articleId){
        return commentService.comments(articleId);
    }

    @RequestMapping(value = "/repliesByComId",method = RequestMethod.GET)
    @ApiOperation(value = "查询评论回复", httpMethod = "GET", response = ResponseResult.class, notes = "查询文章评论")
    public ResponseResult repliesByComId(Integer commentId){
        return commentService.repliesByComId(commentId);
    }
}
