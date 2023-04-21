package com.rulin.controller.api;


import com.rulin.common.ResponseResult;
import com.rulin.service.PhotoAlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 相册 前端控制器
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
@RestController
@RequestMapping("/web/album")
@Api(tags = "相册接口")
@RequiredArgsConstructor
public class ApiPhotoAlbumController {

    private final PhotoAlbumService albumService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "相册列表", httpMethod = "GET", response = ResponseResult.class, notes = "相册列表")
    public ResponseResult webAlbumList() {
        return albumService.webAlbumList();
    }

    @RequestMapping(value = "/listPhotos", method = RequestMethod.GET)
    @ApiOperation(value = "照片列表", httpMethod = "GET", response = ResponseResult.class, notes = "照片列表")
    public ResponseResult webListPhotos(Integer albumId) {
        return albumService.webListPhotos(albumId);
    }
}

