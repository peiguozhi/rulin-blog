package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.service.FeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  后台反馈管理
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/feedback")
@Api(tags = "后台反馈管理")
@RequiredArgsConstructor
public class FeedBackController {

    private final FeedBackService feedBackService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "反馈列表", httpMethod = "GET", response = ResponseResult.class, notes = "反馈列表")
    public ResponseResult list(Integer type) {
        return feedBackService.listFeedBack(type);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/feedback/deleteBatch")
    @ApiOperation(value = "删除反馈", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除反馈")
    @OperationLogger(value = "删除反馈")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return feedBackService.deleteBatch(ids);
    }
}

