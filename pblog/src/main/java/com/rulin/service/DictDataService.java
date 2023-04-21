package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.DictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典数据表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface DictDataService extends IService<DictData> {

    ResponseResult listDictData(Integer dictId, Integer isPublish);

    ResponseResult insertDictData(DictData dictData);

    ResponseResult updateDictData(DictData dictData);

    ResponseResult deleteBatch(List<Long> ids);

    ResponseResult deleteDictData(Long id);

    ResponseResult getDataByDictType(List<String> types);

}
