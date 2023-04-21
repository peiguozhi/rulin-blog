package com.rulin.controller.api;


import com.rulin.annotation.BusinessLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Message;
import com.rulin.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 评论留言接口
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/web/message")
@Api(tags = "评论留言接口")
@RequiredArgsConstructor
public class ApiMessageController {

    private final MessageService messageService;

    @BusinessLogger(value = "留言模块-留言列表", type = "查询", desc = "留言列表")
    @RequestMapping(value = "/webMessage", method = RequestMethod.GET)
    @ApiOperation(value = "留言列表", httpMethod = "GET", response = ResponseResult.class, notes = "留言列表")
    public ResponseResult webMessage() {
        return messageService.webMessage();
    }


    @BusinessLogger(value = "留言模块-用户留言", type = "添加", desc = "用户留言")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加留言", httpMethod = "POST", response = ResponseResult.class, notes = "添加留言")
    public ResponseResult addMessage(@RequestBody Message message) {
        return messageService.webAddMessage(message);
    }

}

