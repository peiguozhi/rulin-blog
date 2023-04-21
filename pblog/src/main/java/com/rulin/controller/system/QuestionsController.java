package com.rulin.controller.system;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.dto.QuestionDTO;
import com.rulin.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/system/question")
@RequiredArgsConstructor
@Api(tags = "后台面试题管理")
public class QuestionsController {

    private final QuestionService questionService;

    @PostMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "面试题列表", httpMethod = "POST", response = ResponseResult.class, notes = "问题列表")
    public ResponseResult list(@RequestBody Map<String, Object> map) {
        return questionService.listQuestion(map);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/question/info")
    @ApiOperation(value = "面试题详情", httpMethod = "GET", response = ResponseResult.class, notes = "面试题详情")
    public ResponseResult getQuestionById(Long id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/question/add")
    @ApiOperation(value = "保存面试题", httpMethod = "POST", response = ResponseResult.class, notes = "保存面试题")
    @OperationLogger(value = "保存面试题")
    public ResponseResult insert(@RequestBody QuestionDTO question) {
        return questionService.insertQuestion(question);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/question/update")
    @ApiOperation(value = "修改面试题", httpMethod = "POST", response = ResponseResult.class, notes = "修改面试题")
    @OperationLogger(value = "修改面试题")
    public ResponseResult update(@RequestBody QuestionDTO question) {
        return questionService.updateQuestion(question);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/question/delete")
    @ApiOperation(value = "删除面试题", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除面试题")
    @OperationLogger(value = "删除面试题")
    public ResponseResult delete(Long id) {
        return questionService.deleteQuestion(id);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/question/deleteBatch")
    @ApiOperation(value = "批量删除面试题", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除面试题")
    @OperationLogger(value = "批量删除面试题")
    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
        return questionService.deleteBatchQuestion(ids);
    }

    @GetMapping(value = "/top")
    @SaCheckPermission("/system/question/top")
    @ApiOperation(value = "置顶面试题", httpMethod = "GET", response = ResponseResult.class, notes = "置顶面试题")
    @OperationLogger(value = "置顶面试题")
    public ResponseResult top(Long id) {
        return questionService.top(id);
    }

    @PostMapping(value = "/pubOrShelf")
    @SaCheckPermission("/system/question/pubOrShelf")
    @ApiOperation(value = "发布或下架面试题", httpMethod = "POST", response = ResponseResult.class, notes = "发布或下架面试题")
    @OperationLogger(value = "发布或下架面试题")
    public ResponseResult publishAndShelf(@RequestBody QuestionDTO question) {
        return questionService.publishAndShelf(question);
    }
}
