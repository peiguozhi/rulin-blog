package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.Photo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 照片 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface PhotoService extends IService<Photo> {

    ResponseResult listPhoto(Integer albumId);

    ResponseResult getPhotoById(Integer id);

    ResponseResult insertAlbum(Photo photo);

    ResponseResult updatePhoto(Photo photo);

    ResponseResult movePhoto(Map<String,Object> map);

    ResponseResult deleteBatch(List<Integer> ids);

}
