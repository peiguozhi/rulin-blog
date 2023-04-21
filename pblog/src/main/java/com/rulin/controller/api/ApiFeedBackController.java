package com.rulin.controller.api;

import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.FeedBack;
import com.rulin.service.FeedBackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CodeScholar
 * @date 2023年4月9日
 * @apiNote 门户反馈管理
 */
@RestController
@RequestMapping("/web/feedback")
@RequiredArgsConstructor
@Api(tags = "门户反馈管理")
public class ApiFeedBackController {

    private final FeedBackService feedBackService;


    @PostMapping(value = "/add")
    @ApiOperation(value = "添加反馈", httpMethod = "POST", response = ResponseResult.class, notes = "添加反馈")
    @BusinessLogger(value = "首页-用户添加反馈",type = "添加",desc = "添加反馈")
    public ResponseResult addFeedback(@RequestBody FeedBack feedBack) {
        return  feedBackService.insertFeedback(feedBack);
    }

}
