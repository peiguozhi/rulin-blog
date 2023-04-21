package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.Photo;
import com.rulin.service.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/photo")
@Api(tags = "相册管理")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "照片列表", httpMethod = "GET", response = ResponseResult.class, notes = "照片列表")
    public ResponseResult list(Integer albumId) {
        return photoService.listPhoto(albumId);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/photo/info")
    @ApiOperation(value = "照片详情", httpMethod = "GET", response = ResponseResult.class, notes = "照片详情")
    public ResponseResult getPhotoById(Integer id) {
        return photoService.getPhotoById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/photo/add")
    @ApiOperation(value = "添加照片", httpMethod = "POST", response = ResponseResult.class, notes = "添加照片")
    @OperationLogger(value = "添加照片")
    public ResponseResult insertAlbum(@RequestBody Photo photo) {
        return photoService.insertAlbum(photo);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/photo/update")
    @ApiOperation(value = "修改照片", httpMethod = "POST", response = ResponseResult.class, notes = "修改照片")
    @OperationLogger(value = "修改照片")
    public ResponseResult updateAlbum(@RequestBody Photo photo) {
        return photoService.updatePhoto(photo);
    }

    @PostMapping(value = "/movePhoto")
    @SaCheckPermission("/system/photo/movePhoto")
    @ApiOperation(value = "移动照片", httpMethod = "POST", response = ResponseResult.class, notes = "移动照片")
    @OperationLogger(value = "移动照片")
    public ResponseResult movePhoto(@RequestBody Map<String, Object> map) {
        return photoService.movePhoto(map);
    }

    @DeleteMapping(value = "/deleteBatch")
    @SaCheckPermission("/system/photo/deleteBatch")
    @ApiOperation(value = "批量删除照片", httpMethod = "DELETE", response = ResponseResult.class, notes = "批量删除照片")
    @OperationLogger(value = "批量删除照片")
    public ResponseResult deleteBatch(@RequestBody List<Integer> ids) {
        return photoService.deleteBatch(ids);
    }

}

