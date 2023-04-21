package com.rulin.service;

import com.rulin.common.ResponseResult;
import com.rulin.entity.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author CodeScholar
 * @date 2023年4月9日
 */
public interface DictService extends IService<Dict> {

    ResponseResult listDict(String name, Integer isPublish, String descColumn, String ascColumn);

    ResponseResult insertDict(Dict dict);

    ResponseResult updateDict(Dict dict);

    ResponseResult deleteDict(int id);

    ResponseResult deleteBatch(List<Long> list);

}
