package com.rulin.controller.system;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.rulin.annotation.OperationLogger;
import com.rulin.common.ResponseResult;
import com.rulin.entity.PhotoAlbum;
import com.rulin.service.PhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/system/album")
@Api(tags = "相册管理")
@RequiredArgsConstructor
public class PhotoAlbumController {

    private final PhotoAlbumService albumService;


    @GetMapping(value = "/list")
    @SaCheckLogin
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ResponseResult.class, notes = "相册列表")
    public ResponseResult list(String name) {
        return albumService.listAlbum(name);
    }

    @GetMapping(value = "/info")
    @SaCheckPermission("/system/album/info")
    @ApiOperation(value = "相册详情", httpMethod = "GET", response = ResponseResult.class, notes = "相册详情")
    public ResponseResult getAlbumById(Integer id) {
        return albumService.getAlbumById(id);
    }

    @PostMapping(value = "/add")
    @SaCheckPermission("/system/album/add")
    @ApiOperation(value = "添加相册", httpMethod = "POST", response = ResponseResult.class, notes = "添加相册")
    @OperationLogger(value = "添加相册")
    public ResponseResult insertAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return albumService.insertAlbum(photoAlbum);
    }

    @PostMapping(value = "/update")
    @SaCheckPermission("/system/album/update")
    @ApiOperation(value = "修改相册", httpMethod = "POST", response = ResponseResult.class, notes = "修改相册")
    @OperationLogger(value = "修改相册")
    public ResponseResult updateAlbum(@RequestBody PhotoAlbum photoAlbum) {
        return albumService.updateAlbum(photoAlbum);
    }

    @DeleteMapping(value = "/delete")
    @SaCheckPermission("/system/album/delete")
    @ApiOperation(value = "删除相册", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除相册")
    @OperationLogger(value = "删除相册")
    public ResponseResult deleteAlbumById(Integer id) {
        return albumService.deleteAlbumById(id);
    }
}

