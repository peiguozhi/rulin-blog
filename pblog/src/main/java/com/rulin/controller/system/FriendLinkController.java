package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.FriendLink;
import com.rulin.service.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 友情链接表 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/friend")
@Api(tags = "友情链接后端-接口")
@RequiredArgsConstructor
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @SaCheckLogin
    @ApiOperation(value = "友链列表", httpMethod = "GET", response = ResponseResult.class, notes = "友链列表")
    public ResponseResult list(String name, Integer status) {
        return friendLinkService.listFriendLink(name, status);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @SaCheckPermission("/system/friend/create")
    @ApiOperation(value = "添加友链", httpMethod = "POST", response = ResponseResult.class, notes = "添加友链")
    @OperationLogger(value = "添加友链")
    public ResponseResult insert(@RequestBody FriendLink friendLink) {
        return friendLinkService.insertFriendLink(friendLink);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @SaCheckPermission("/system/friend/update")
    @ApiOperation(value = "修改友链", httpMethod = "POST", response = ResponseResult.class, notes = "修改友链")
    @OperationLogger(value = "修改友链")
    public ResponseResult update(@RequestBody FriendLink friendLink) {
        return friendLinkService.updateFriendLink(friendLink);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.DELETE)
    @SaCheckPermission("/system/friend/remove")
    @ApiOperation(value = "删除友链", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除友链")
    @OperationLogger(value = "删除友链")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return friendLinkService.deleteBatch(ids);
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @SaCheckPermission("/system/friend/top")
    @ApiOperation(value = "置顶友链", httpMethod = "GET", response = ResponseResult.class, notes = "置顶友链")
    @OperationLogger(value = "置顶友链")
    public ResponseResult top(Integer id) {
        return friendLinkService.top(id);
    }
}

